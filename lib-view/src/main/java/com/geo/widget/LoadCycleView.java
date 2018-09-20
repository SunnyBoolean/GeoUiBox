package com.geo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liwei on 2017/5/26.
 * 系统更新的控件
 */

public class LoadCycleView extends View{
    //半径，也就是控件的高和宽，可以通过属性文件自定义
    private int mRadio=280;
    //刻度的长度
    private int mAngleWidth=65;
    //颜色 可以通过属性文件自定义
    private int mColor= Color.WHITE;
    //刻度之间的间距度数
    private float mDistanceAngel =3f;
    //缺口度数 可以通过属性文件自定义
    private float mSweepAngle = 0;
    //控件类型:0表示已经完成的固定不变的控件,1表示绘制
    private int mCycleType=1;
    //普通画笔，背景通用
    private Paint mPaint;
    //前景深色画笔
    private Paint mFgPaint;
    public LoadCycleView(Context context) {
        super(context);
        initPaint();
    }

    public LoadCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public LoadCycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


   private void initPaint(){
       mPaint = new Paint();
       mPaint.setAntiAlias(true);
       mPaint.setColor(mColor);
       mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

       mFgPaint = new Paint();
       mFgPaint.setAntiAlias(true);
       mFgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
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
            int desired =  mRadio*2+mAngleWidth + getPaddingRight()+(int) (getPaddingLeft());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired =  mRadio*2+mAngleWidth +(int) (getPaddingTop() +  getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mCycleType==0){
            drawDoneCycle(canvas,true);
        }else if(mCycleType==1){
            drawDynamicCycle(canvas);
        }
    }
    private void drawDynamicCycle(Canvas canvas){
        mSweepAngle = 8;
        drawDoneCycle(canvas,false);
    }

    /**
     * 绘制已经完成控件，静态的，不会有变化
     * @param canvas
     * @param isDrawDot  是否绘制小圆点
     */
    private void drawDoneCycle(Canvas canvas,boolean isDrawDot){
        canvas.save();
        canvas.translate(mAngleWidth/2,mAngleWidth/2);
        float index=0 ;
        //绘制锯齿光圈，首先绘制一个小的矩形，然后旋转画布不停绘制即可
        if(mSweepAngle==0){
            index = 360/ mDistanceAngel;
        }else{
            index=360/ mDistanceAngel -mSweepAngle*mDistanceAngel;
        }
        float roatAngele = 180-(360-index* mDistanceAngel)/2;
        canvas.rotate(-roatAngele,mRadio,mRadio);
        for(int i=0;i<index;i++){
            canvas.drawRect(mRadio,0,mRadio+5,mAngleWidth,mPaint);
            canvas.rotate(mDistanceAngel,mRadio,mRadio);
        }
        //如果不需要绘圆点就不绘制
        if(!isDrawDot){
            return;
        }
        //
        int x = mRadio;
        //减去8是因为，8是小球的半径。减去3是因为小球和刻度之间有个间距
        int y = mRadio*2-mAngleWidth-6-8;
        canvas.restore();
        canvas.translate(mAngleWidth/2,mAngleWidth/2);
        canvas.drawCircle(x,y,10,mPaint);
    }
}
