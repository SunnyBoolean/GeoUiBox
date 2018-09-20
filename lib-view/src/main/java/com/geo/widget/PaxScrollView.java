package com.geo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.geo.util.ScreenUtil;

/**
 * Created by liwei on 2018/3/31.
 */

public class PaxScrollView extends LinearLayout {
    Scroller mScroller;

    public PaxScrollView(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public PaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public PaxScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    private int mTotalHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childTotal = getChildCount();
        for (int i = 0; i < childTotal; i++) {
            View view = getChildAt(i);
            mTotalHeight = mTotalHeight + view.getMeasuredHeight();
            Log.e("LINE", "高度是：" + view.getMeasuredHeight());
        }
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height=mTotalHeight;
        setLayoutParams(lp);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, mTotalHeight);
        LayoutParams lp = (LayoutParams) getLayoutParams();
        lp.height = mTotalHeight;
        setLayoutParams(lp);

    }

    float mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mLastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getRawY();
                float scrollY = getScrollY();
                ScrollView sc;
                int screenHeight = ScreenUtil.getScreenHeight(getContext());
                float dy = mLastY - y;
                float lsheight = getMeasuredHeight() - screenHeight;
//                if (scrollY >Math.abs( lsheight)) {//如果滑动距离超过高度就不滑动了
//                    dy = 0;
//                }
                Log.e("SCROL",dy+"");
                scrollBy(0, (int) dy);
                mLastY = y;
                Log.e("SCROLL", "y=" + y + "  mTotalHeight=" + mTotalHeight + "  scrollY=" + scrollY + "  mLastY=" + mLastY + "   lsheight=" + lsheight + "  dy="+dy+"   getMeasuredHeight()="+getMeasuredHeight()+"   mTotalHeight="+mTotalHeight+"   screenHeight="+screenHeight);
                break;

        }
        return true;
    }
}
