package com.geo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.geo.util.ScreenUtil;
import com.geostar.liwei.lib_view.R;

import java.util.ArrayList;

/**
 * Created by liwei on 2018/5/15.
 * 定制符合要求的Tablayout
 */

public class VPTabLayout extends HorizontalScrollView {
    TabContainer mTabContainer;
    ViewPager mViewPager;
    private LinearLayout mTabParent;
    private int mItemWidth;//每一个Tab的宽度
    private int mIndicatorWidth;//指示器的宽度
    private int mIndicatorHeight = 6;//指示器的高度
    private float mTextSize = 16;//标题字体大小
    private int mTextNormalColor;//文字正常颜色
    private int mtextSelectedColor;//文字被选中的颜色
    private int mIndicatorColor;//指示器颜色

    public VPTabLayout(Context context) {
        super(context);
    }

    public VPTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VPTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.VPTabLayout, defStyleAttr, 0);
            mTextSize = a.getDimension(R.styleable.VPTabLayout_vpTextSize, 16);
            mIndicatorHeight = (int) a.getDimension(R.styleable.VPTabLayout_vpLineHeight, 6);
            mTextNormalColor = a.getColor(R.styleable.VPTabLayout_vpTextNormalColor, Color.WHITE);
            mtextSelectedColor = a.getColor(R.styleable.VPTabLayout_vpTextSelectedColor, Color.WHITE);
            mIndicatorColor = a.getColor(R.styleable.VPTabLayout_vpIndicatorColor, Color.WHITE);
            a.recycle();

        }
        //默认是屏幕的三分之一，具体的会以标题个数为准
        mItemWidth = ScreenUtil.getScreenWidth(context) / 4;
        mTabContainer = new TabContainer(context);
        mTabContainer.setLayoutParams(new ViewGroup.LayoutParams(HorizontalScrollView.LayoutParams.MATCH_PARENT, HorizontalScrollView.LayoutParams.MATCH_PARENT));
        mTabContainer.setGravity(Gravity.CENTER_VERTICAL);

        mTabParent = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTabParent.setLayoutParams(lp);
        mTabParent.setGravity(Gravity.CENTER);
        mTabParent.setOrientation(LinearLayout.HORIZONTAL);
        mTabContainer.addView(mTabParent);
        addView(mTabContainer);
    }


    //设置VP
    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        initViewPager();
    }


    /**
     * 根容器
     */
    private class TabContainer extends LinearLayout {
        Paint mIndicatorPaint;

        public TabContainer(Context context) {
            super(context);
            init();
        }

        public TabContainer(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public TabContainer(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            setWillNotDraw(false);
            mIndicatorPaint = new Paint();
            mIndicatorPaint.setColor(mIndicatorColor);
            mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);
            mIndicatorPaint.setStrokeWidth(mIndicatorHeight);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            int sx = 0, sy;
            if (mTabs != null && mTabs.size() > 0) {
                Tab tab = mTabs.get(0);
                sx = tab.titleView.getWidth() / 2;
                sy = tab.titleView.getHeight() / 2;
            } else {
                return;
            }
            int y = getHeight() / 2 + sy + 10; //指示器和文字之间加上10个单位的间距
            float x = sx - mIndicatorWidth / 2 + mCurOffset * mItemWidth + mCurPosition * mItemWidth;
            float stopX = x + mIndicatorWidth;
            Log.e("ASD", stopX + "  " + "  mCurPosition= " + mCurPosition + "   mCurOffset=" + mCurOffset);
            canvas.drawLine(x, y, stopX, y, mIndicatorPaint);
        }
    }

    private int mCurPosition = 0;
    private float mCurOffset;
    private int currentPosition;

    private void initViewPager() {
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int dx = (int) (positionOffset * mIndicatorWidth);
                    int tposition = Math.round(position + positionOffset);
                    //改变标题选中状态
                    changeState(tposition);
                    //如果标题大于4个则需要滚动标题栏
                    if(mCurOffset-positionOffset<0){//右滑
                        if (position > 1) {
                            smoothScrollBy(mIndicatorWidth, 0);
                        }
                    }else{//左滑
                        if (position <3) {
                            smoothScrollBy(-mIndicatorWidth, 0);
                        }
                    }
                    mCurOffset = positionOffset;
                    //以下逻辑是绘制指示器的位置参数
                    if (position > currentPosition) {
                        currentPosition = position;
                        mCurPosition++;
                    } else if (position < currentPosition) {
                        mCurPosition--;
                        currentPosition = position;
                    }
                    if (positionOffset != 0)
                        mTabContainer.invalidate();
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }

    private void changeState(int position) {
        for (int i = 0; i < mTabs.size(); i++) {
            Tab tab = mTabs.get(i);
            LinearLayout lin = tab.titleView;
            TextView tv = (TextView) lin.getChildAt(0);
            TextPaint tp = tv.getPaint();

            if (position == i) {
                tv.setTextColor(mtextSelectedColor);
                tp.setFakeBoldText(true);
            } else {
                tv.setTextColor(mTextNormalColor);
                tp.setFakeBoldText(false);
            }
        }
    }

    private static ArrayList<Tab> mTabs = new ArrayList<>();


    public void setTitle(String[] titles) {
        if (titles != null) {
            if (titles.length > 4) {
                //如果标题超过4个，为了良好体验，宽度不要平分屏幕，这样用户知道标题不止4个
                mItemWidth = ScreenUtil.getScreenWidth(getContext()) / 4-30;
            }else if(titles.length==4){
                mItemWidth = ScreenUtil.getScreenWidth(getContext()) / 4;
            }
            else {
                mItemWidth = ScreenUtil.getScreenWidth(getContext()) / 3;
            }
            for (int i = 0; i < titles.length; i++) {
                String s = titles[i];
                final Tab tab = new Tab(s, i);
                tab.titleView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(tab.position);
                    }
                });
                //需要保存每一个Tab
                mTabs.add(tab);
                mTabParent.addView(tab.titleView);
            }
            mTabContainer.invalidate();
        }
    }

    public class Tab {
        private LinearLayout titleView;
        private int position;

        public Tab(String title, int position) {
            titleView = genTabItem(title);
            this.position = position;
        }
    }

    private LinearLayout genTabItem(String title) {
        LinearLayout frameLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mItemWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(lp);
        frameLayout.setGravity(Gravity.CENTER);
        frameLayout.addView(genTextView(title));
        return frameLayout;
    }

    /**
     * 生成一个TextView
     *
     * @param title
     * @return
     */
    private TextView genTextView(String title) {
        TextView textView = new TextView(getContext());

        textView.setText(title);
        textView.setTextColor(Color.WHITE);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER);
        TextPaint paint = textView.getPaint();
        paint.setTextSize(mTextSize);
//        paint.setColor(textColor);
        mIndicatorWidth = (int) paint.measureText(title);
        return textView;
    }

}
