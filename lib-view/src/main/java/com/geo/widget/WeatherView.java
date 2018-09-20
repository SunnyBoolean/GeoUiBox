package com.geo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.geo.util.ScreenUtil;
import com.geostar.liwei.lib_view.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liwei on 2018/4/24.
 */

public class WeatherView extends View {
    private List<Integer> mDatas = new ArrayList<Integer>();
    private Paint mPaint;
    private int mPaintColor;
    private int mHeight;
    private int mWidth = 50;
    private int mRadius=10;
    private int mLowestTem;

    public WeatherView(Context context) {
        super(context);
        init();
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WeatherView,0, 0);
        mPaintColor = a.getColor(R.styleable.WeatherView_line_color, Color.WHITE);
        a.recycle();
        init();
    }

    public WeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WeatherView, defStyleAttr, 0);
        mPaintColor = a.getColor(R.styleable.WeatherView_line_color, Color.WHITE);
        a.recycle();
        init();
    }

    private void init() {
        mDatas.add(21);
        mDatas.add(25);
        mDatas.add(13);
        mDatas.add(28);
        mDatas.add(24);
        mDatas.add(30);
        mDatas.add(10);
        mDatas.add(25);
        mDatas.add(36);
        mDatas.add(40);
        measureAviHeight();
        mPaint = new Paint();



        mPaint.setColor(mPaintColor);
    }

    public void setWeatherData(List<Integer> datas) {
        mDatas.addAll(datas);
        measureAviHeight();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0,mRadius);
        for (int i = 0;i < mDatas.size();i++){
            int startX = 20+i*mWidth;
            int startY = (mLowestTem - mDatas.get(i) )*mAviHeight;
            canvas.drawCircle(startX,startY,mRadius,mPaint);
            if(i < mDatas.size() - 1){
                int stopX = startX + mWidth;
                int stopY = (mLowestTem - mDatas.get(i+1) )*mAviHeight;
                canvas.drawLine(startX,startY,stopX,stopY,mPaint);
                Log.e("WEAR","startX="+startX+"  startY="+startY+"  stopX="+stopX+"  "+stopY);
            }
        }
    }
    //温度刻度
    private int mAviHeight;

    /**
     * 计算刻度
     */
    private void measureAviHeight() {
        List<Integer> tempDatas = new ArrayList<>();
        int size = mDatas.size();
        tempDatas.addAll(mDatas);
        Collections.sort(tempDatas);
        mAviHeight = Math.abs(tempDatas.get(0) - tempDatas.get(tempDatas.size() - 1));
        mAviHeight=getHeight()/mAviHeight;
        mLowestTem = tempDatas.get(size -1);

        mWidth = ScreenUtil.getScreenWidth(getContext())/mDatas.size();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measureAviHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int desired = ScreenUtil.getScreenWidth(getContext());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = ScreenUtil.getScreenHeight(getContext())/3+4*mRadius+(int)mPaint.measureText("21°");;
            height = desired;
        }

        setMeasuredDimension(width, height);
    }
}
