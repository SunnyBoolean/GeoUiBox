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
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.geo.util.ScreenUtil;

/**
 * Created by liwei on 2018/4/12.
 */

public class QQSvpiProfileView extends LinearLayout {
    private ViewGroup mHeaderView;
    private ViewGroup mRootChild;//此View的第一个子View
    private int mMaxTranxy;//最大滑动距离
    private View mQQTelTv;//qq电话文字提示
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;//速度检测，速度超过一定值就让View直接滚动到头
    int mTouchSlop;//滑动的最小距离
    private int mToolbarHeight = 240;

    public QQSvpiProfileView(Context context) {
        super(context);
        init();
    }

    public QQSvpiProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QQSvpiProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);
        init();
    }


    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
//        setOrientation(VERTICAL);
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        // 获取TouchSlop值
        mTouchSlop = configuration.getScaledPagingTouchSlop();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRootChild = (ViewGroup) getChildAt(0);
        mHeaderView = (ViewGroup) mRootChild.getChildAt(0);//获得头部
        mQQTelTv = mHeaderView.getChildAt(1);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int screenHeight = (int) (ScreenUtil.getScreenHeight(getContext()) - ScreenUtil.getStatusBarHeight(getContext()));
        mMaxTranxy = screenHeight - mHeaderView.getMeasuredHeight();
        mRootChild.scrollTo(0, -mMaxTranxy);
        inZoom();
    }

    /**
     * 进来动一下
     */
    private void inZoom() {
        ValueAnimator animator = ValueAnimator.ofInt(-mMaxTranxy, -mMaxTranxy + 100, -mMaxTranxy + 90, -mMaxTranxy);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (int) animation.getAnimatedValue();
                mRootChild.scrollTo(0, y);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mQQTelTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    private float mLastY;
    private boolean isDown = false;
    private int mLastDistance;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        mVelocityTracker.addMovement(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float distance = ev.getY() - mLastY;
                if (Math.abs(distance) < mTouchSlop) {//如果滑动的距离小于最小距离就不算是滚动了
                    return false;
                }
                if (distance < 0) {
                    isDown = false;
                } else {
                    isDown = true;
                }
                if (Math.abs(distance) > mTouchSlop) {//防止手指触摸时抖动导致view抖动
                    int dy = (int) (mLastDistance - Math.abs(distance));
                    if (!isDown)
                        mRootChild.scrollBy(0, -dy);
                    if (isDown)
                        mRootChild.scrollBy(0, dy);
                    mLastDistance = (int) Math.abs(distance);
                    //处理状态栏，要判断没有滑出边界才处理
                    if(!isOverScroll()){
                        handToolbar();
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                mLastDistance = 0;
                mVelocityTracker.computeCurrentVelocity(1000);
                boolean isOverScroller = isOverScroll();
                if (isOverScroller) {//如果滑动超出范围就回退
                    reset();
                    return true;
                }
                int speed = (int) mVelocityTracker.getYVelocity();
                Log.e("Splle","速度="+speed);
                if (Math.abs(speed) > 600) {  //速度超过600才自动滑动
                    int scrollY = mRootChild.getScrollY();
                    //手指离开屏幕后平滑的滚动一小段距离：50个单位
                    int dy = mMaxTranxy - Math.abs(scrollY);
                    if (isDown) {  //手指上滑动
                        //这个dy表示的是要滑动的距离
                        mScroller.startScroll(0, scrollY, 0, -dy, 1000);
                    } else {
                        mScroller.startScroll(0, scrollY, 0, mMaxTranxy - dy - mToolbarHeight, 1000);
                    }
                    invalidate();
                }
                break;
        }
        return true;
    }


    /**
     * 判断是否是滚动超出限制
     *
     * @return
     */
    private boolean isOverScroll() {
        int scrollY = mRootChild.getScrollY();
        if (!isDown) {//向上滑动
            if (scrollY > -mToolbarHeight) {
                return true;
            }
        } else {
            if (Math.abs(scrollY) > mMaxTranxy) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当滚动超出范围就回退
     */
    private void reset() {
        if (!isDown) {
            ValueAnimator animator = ValueAnimator.ofInt(mRootChild.getScrollY(), -mToolbarHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    mRootChild.scrollTo(0, value);
                }
            });
            animator.setDuration(500);
            animator.start();
        } else {
            ValueAnimator animator = ValueAnimator.ofInt(mRootChild.getScrollY(), -mMaxTranxy);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    mRootChild.scrollTo(0, value);
                }
            });
            animator.setDuration(500);
            animator.start();
        }
    }

    @Override
    public void computeScroll() {
        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            //处理状态栏，判断没有滑出边界才处理状态栏
            if(!isOverScroll()){
                handToolbar();
            }
            int distance = mScroller.getCurrY();
            mRootChild.scrollTo(0, distance);
            postInvalidate();
        }
        super.computeScroll();
    }

    private void handToolbar() {
        int scrollY = mRootChild.getScrollY();
        int dy = -mMaxTranxy / 2;
        if (scrollY > dy &&!isDown) {//开始显示Toolbar   :
            float y = mMaxTranxy - Math.abs(scrollY);
            float factor = y / (float)(mMaxTranxy)-100 ;
            factor=factor>1?1:factor;
            if (mOnToolbarCallback != null) {
                mOnToolbarCallback.onCallback(Math.abs(factor));
            }
        } else if(scrollY<=dy &&isDown){//隐藏Toolbar
            float y = mMaxTranxy - Math.abs(scrollY)-50;
            float factor = y / (float) mMaxTranxy;
            if (mOnToolbarCallback != null) {
                mOnToolbarCallback.onCallback(Math.abs(factor));
            }
        }
    }

    OnToolbarCallback mOnToolbarCallback;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_MOVE){
            return true;
        }
        return super.onInterceptTouchEvent(ev);

    }

    public void setOnToolbarCallback(OnToolbarCallback callback) {
        this.mOnToolbarCallback = callback;
    }

    public interface OnToolbarCallback {
        public void onCallback(float factor);
    }


}
