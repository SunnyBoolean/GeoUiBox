package com.geo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by liwei on 2018/6/27.
 */

public class WaveCircleView extends View {
    Paint mPaint,mCirclePaint;
    private Paint mTextPaint,mProgressPaint;
    private int mRadio = 0;//圆半径  默认初始值是80
    private int mMaxRadio=150;//最大圆半径  150
    private float mAlpha = 255;
    private boolean mIsStart=false;//是否开始了
    private int mValue=8;//倒计时秒数，默认是15秒
    private float mCurAngle=0;//当前进度角度
    private int mCircleRadio=80;//实心圆半径
    public WaveCircleView(Context context) {
        super(context);
        init();
    }

    public WaveCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);


        //实体圆
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setColor(Color.GRAY);
        mCirclePaint.setAntiAlias(true);

        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(32);
        mTextPaint.setAntiAlias(true);

        //进度条
        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(6);
        mProgressPaint.setColor(Color.parseColor("#00B2EE"));
        mProgressPaint.setAntiAlias(true);
        radioAnima();
    }


    /**
     * 半径渐变
     */
    private void radioAnima() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, mMaxRadio);
        animator.setDuration(2500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (0 + (Float) animation.getAnimatedValue());
                mRadio = (int) alpha;
                mAlpha = (mMaxRadio-alpha/(float)mMaxRadio)*255;
                Log.e("TANIMA","当前透明度："+"  ===  "+mAlpha);
                invalidate();
            }
        });
        animator.start();
    }
    private int mCurTime;
    private void animaProgress(){
        ValueAnimator animator =ValueAnimator.ofFloat(0,360);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurAngle= (float) animation.getAnimatedValue();
                long lng=animation.getCurrentPlayTime();
                mCurTime = (int) (lng/1000);
                Log.e("ANIMA","当前时间："+lng+"  ===  "+mCurTime);
                invalidate();
            }
        });
        animator.setDuration(mValue*1000);
        animator.start();
    }

    public void start(){
        mIsStart = true;
        animaProgress();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        int tempRadio = (int) (mRadio + 50);
        mPaint.setAlpha((int) mAlpha);
        int x = getMeasuredWidth() / 2;
        int y = getMeasuredHeight() / 2;
        canvas.drawCircle(x, y, mRadio, mPaint);
        for(int i=1;i<15;i++){
            if(i%3==0)  //半径以30的等差递增：4个圈
            canvas.drawCircle(x, y, mRadio + i*10, mPaint);
        }
        //实心圆的半径为60
        canvas.drawCircle(x,y,mCircleRadio,mCirclePaint);

        //绘制蓝色进度条
        if(mIsStart){
            RectF rect = new RectF(x-mCircleRadio, y-mCircleRadio, x+mCircleRadio, y+mCircleRadio);
            canvas.drawArc(rect,0,mCurAngle,false,mProgressPaint);
            //绘制倒计时
            int temp = mValue-mCurTime;
            String text = temp+"s";
            float textLength = mTextPaint.measureText(text);
            canvas.drawText(text,x-textLength/2,y+10,mTextPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int desired = (int) (getPaddingLeft() + getPaddingRight()) + mMaxRadio * 2 + mMaxRadio * 2;
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + mMaxRadio * 2 + getPaddingBottom()) + mMaxRadio * 2;
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
}
