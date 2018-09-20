package com.geo.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by liwei on 2018/4/11.
 * 头部图片缩放view
 */

public class QQScaleScrollview extends ScrollView {
    private View mHeaderView;  //头部ImageView
    private int mHeaderWidth, mHeaderHeight; //头部ImageView的宽度和高度
    private float mLastY;//记录手指点击的位置
    private int mDistance;//手指滑动的距离
    private float factor = 0.3f;//缩放系数
    private boolean isReseting = false;//是否正在回缩动画
    private VelocityTracker mVelocityTracker;//速度检测，速度超过一定值就让View直接滚动到头
    int mTouchSlop;//滑动的最小距离
    public QQScaleScrollview(Context context) {
        super(context);
        init();
    }

    public QQScaleScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QQScaleScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        mVelocityTracker = VelocityTracker.obtain();
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        //这里需要添加一个滚动监听，用来控制Toolbar是透明还是显色
        setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                handleToolbarFactor(-mDistance);
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewGroup childContainer = (ViewGroup) getChildAt(0);
        mHeaderView = childContainer.getChildAt(0);//获得头部
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeaderWidth = mHeaderView.getMeasuredWidth();
        mHeaderHeight = mHeaderView.getMeasuredHeight();
        Log.e("ScrollMethos", "onSizeChanged()高和宽：" + mHeaderWidth + "   " + mHeaderHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e("ScrollMethos", "onLayout()高和宽：" + mHeaderWidth + "   " + mHeaderHeight);
    }

    //    private float mTotalDistance;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isReseting) {  //如果正在收缩就判断
                    return super.onTouchEvent(ev);
                }

                //这里不断放大ImageView
                mDistance = (int) ((ev.getY() - mLastY) * factor);
                scaleBy();
                //处理滑动Toolbar的背景色
                float distance = mLastY - ev.getY();//滑动距离
                handleToolbarFactor(distance);
                break;
            case MotionEvent.ACTION_UP://手指离开屏幕就回到原处
                //回弹
                reset();
                break;
        }

        return super.onTouchEvent(ev);

    }

    /**
     * 处理放大效果
     */
    private void scaleBy() {
        if (mDistance <= 0) {//小于0说明是手指向上滑动忽略
            //最多只能压缩一半
            if(Math.abs(mDistance)<mHeaderHeight/2){
                setTranslationY(mDistance);
            }
            return;
        }
        //判断滑动的距离，只有当滑动到顶部时才能执行缩放
        if (getScrollY() > 0) {

            return;
        }
        ViewGroup.LayoutParams lp = mHeaderView.getLayoutParams();
        lp.width = mHeaderWidth + mDistance;
        lp.height = mHeaderHeight + mDistance;
        mHeaderView.setLayoutParams(lp);
    }

    /**
     * 以动画的形式让View弹回原来的位置
     */
    private void reset() {
        if (isReseting || mDistance <= 0) {  //这里如果正在回缩也要处理;如果距离小于0也不处理
            //处理向上压缩后回弹
            ValueAnimator anima = ValueAnimator.ofFloat(getTranslationY(),0);
            anima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float y = (float) animation.getAnimatedValue();
                    setTranslationY(y);
                }
            });
            anima.setDuration(200);
            anima.start();
            return;
        }
        //判断滑动的距离，只有当滑动到顶部时才能执行缩放
        if (getScrollY() > 0) {
            return;
        }

        isReseting = true;
        ValueAnimator animator = ValueAnimator.ofInt(0, mDistance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int distance = (int) animation.getAnimatedValue();
                Log.e("ScaleScrollview", "动画更新：  " + distance + "  --" + mDistance);
                ViewGroup.LayoutParams lp = mHeaderView.getLayoutParams();
                lp.width = mHeaderWidth + mDistance - distance;
                lp.height = mHeaderHeight + mDistance - distance;

                mHeaderView.setLayoutParams(lp);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isReseting = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(200);
        animator.start();
    }

    /**
     * 根据手势的滑动来处理Toolbar
     *
     * @param distance
     */
    private void handleToolbarFactor(float distance) {
        if (isReseting) {
            return;
        }
        Log.e("SDCF", getScrollY() + "   " + distance + "   ");
        if (distance > 0) {//手指向上滑动
            int height = mHeaderHeight / 2;
            if (getScrollY() > height && mOnScallCallback != null) {//只要滚动超过一半就开始显示Toolbar
                float factor = (float) (mHeaderHeight - (mHeaderHeight - getScrollY())) / (float) mHeaderHeight;
                Log.e("SDCF", " fact=  " + factor);
                factor = factor > 1 ? 1 : factor;
                mOnScallCallback.onScallFactor(factor);  //0表示显示Toolbar
            }
        } else {//手指向下滑动
            int height = mHeaderHeight / 2;
            Log.e("QQS", getScrollY() + " ");
            if (getScrollY() < height && mOnScallCallback != null) {//滚动小于1半就开始隐藏了
                float factor = (float) (mHeaderHeight - (mHeaderHeight - getScrollY())) / (float) height;
                mOnScallCallback.onScallFactor(factor);   //1表示不显示Toolbar
            }
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            mLastY= ev.getY();
        }
        mVelocityTracker.addMovement(ev);
        if(ev.getAction()==MotionEvent.ACTION_MOVE){   //要处理滑动和其子View的点击事件
            float distance = mLastY-ev.getY();
            if (Math.abs(distance) < mTouchSlop) {//如果滑动的距离小于最小距离就不拦截事件
                return false;
            }else{  //如果大于阈值就需要拦截事件进行滚动
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }
    private OnScaleCallback mOnScallCallback;

    public void setOnScallCallback(OnScaleCallback callback) {
        mOnScallCallback = callback;
    }

    /**
     * 缩放回调
     */
    public interface OnScaleCallback {
        //0表示完全收缩，1表示完全展开
        public void onScallFactor(float factor);
    }
}
