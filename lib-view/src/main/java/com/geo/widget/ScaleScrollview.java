package com.geo.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by liwei on 2018/4/11.
 * 头部图片缩放view
 */

public class ScaleScrollview extends ScrollView {
    private View mHeaderView;  //头部ImageView
    private int mHeaderWidth, mHeaderHeight; //头部ImageView的宽度和高度
    private float mLastY;//记录手指点击的位置
    private int mDistance;//手指滑动的距离
    private float factor = 0.3f;//缩放系数
    private boolean isReseting=false;//是否正在回缩动画
    Scroller mScroller;
    public ScaleScrollview(Context context) {
        super(context);
        init();
    }
    public ScaleScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ScaleScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        mScroller=new Scroller(getContext());
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
        Log.e("ScrollMethos","onSizeChanged()高和宽："+mHeaderWidth+"   "+mHeaderHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e("ScrollMethos","onLayout()高和宽："+mHeaderWidth+"   "+mHeaderHeight);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(isReseting){  //如果正在收缩就不处理滑动了
                    return super.onTouchEvent(ev);
                }
                mDistance = (int) ((ev.getY() - mLastY)*factor);
                scaleBy();
                break;
            case MotionEvent.ACTION_UP://手指离开屏幕就回到原处
                reset();
                break;
        }

        return super.onTouchEvent(ev);

    }

    private void scaleBy() {
        if(mDistance<=0){//小于0说明是手指向上滑动忽略
            ViewGroup.LayoutParams lp =  mHeaderView.getLayoutParams();
            lp.height = mHeaderHeight + mDistance;
            if(Math.abs(mDistance)<mHeaderHeight/2){
               setTranslationY(mDistance);
            }
            return;
        }
        ViewGroup.LayoutParams lp =  mHeaderView.getLayoutParams();
        lp.width = mHeaderWidth + mDistance;
        lp.height = mHeaderHeight + mDistance;
        Log.e("SScroll","宽度："+lp.width);
        mHeaderView.setLayoutParams(lp);

    }
    private void reset() {
        if(isReseting||mDistance<=0){  //这里如果正在回缩也要处理;如果距离小于0也不处理
            ValueAnimator anima = ValueAnimator.ofFloat(getTranslationY(),0);
            anima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float y = (float) animation.getAnimatedValue();
                    setTranslationY(y);
                }
            });
            anima.setDuration(200);
            anima.start();;
            return;
        }
        //判断滑动的距离，只有当滑动到顶部时才能执行缩放
        if(getScrollY()>0){
            return;
        }
        isReseting=true;
        ValueAnimator animator = ValueAnimator.ofInt(0, mDistance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int distance = (int) animation.getAnimatedValue();
                Log.e("ScaleScrollview","动画更新：  "+distance+"  --"+mDistance);
                ViewGroup.LayoutParams lp =mHeaderView.getLayoutParams();
                lp.width = mHeaderWidth+mDistance - distance;
                lp.height = mHeaderHeight+mDistance - distance;

                mHeaderView.setLayoutParams(lp);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isReseting=false;
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


}
