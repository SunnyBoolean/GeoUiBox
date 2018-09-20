package com.geo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by liwei on 2018/4/2.
 * 测试滑动
 */

public class ScrollTestView extends FrameLayout {
    Scroller mScroller;

    public ScrollTestView(Context context) {
        super(context);
        initScroller();
    }

    public ScrollTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScroller();
    }

    public ScrollTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScroller();
    }

    private void initScroller() {
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void smoothScrollTo(int destX,int destY) {
        int scrollX=getScrollX();
        int scrollY = getScrollY();
        int delta = destX - scrollX;
        int delY = destY-scrollY;
        mScroller.startScroll(scrollX,0,delta,delY,1000);
        invalidate();

    }
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }
    float mLastMove;
    float downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (event.getX()-downY);
                int dx = (int) (event.getY()-downX);
                Log.e("MOVE","downX="+downX+"   downY="+downY+"  getRawX()="+event.getRawX()+"   getRowY()="+event.getRawY()+"  dx="+dx+"   dy="+dy);
                mLastMove = getScrollY();
//                scrollBy(-dx,-dy);
                smoothScrollTo(-dx,-dy);
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }
}
