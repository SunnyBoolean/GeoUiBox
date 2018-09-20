package com.geo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by liwei on 2018/3/29.
 */

public class ProgressViewV2View extends View {
    Paint mPaint, mProgressPaint,mTextPaint;

    public ProgressViewV2View(Context context) {
        super(context);
    }

    public ProgressViewV2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ProgressViewV2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }
    private int mValue;
    public void setValue(int value){
        this.mValue = value;
        animaProgress();
    }
    private float mCurAngle;
    private void animaProgress(){
        float progress = (float) (2.7*mValue);
        ValueAnimator animator =ValueAnimator.ofFloat(0,progress);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurAngle= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(1500);
        animator.start();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置为圆角
        mPaint.setStrokeWidth(20);


        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setColor(Color.YELLOW);
        mProgressPaint.setStrokeWidth(20);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND); //设置为圆角
        mProgressPaint.setTextSize(20);


        mTextPaint=new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(Color.YELLOW);
        mTextPaint.setTextSize(60);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(20,20);
        canvas.rotate(135,150,150);
        RectF rect = new RectF(0, 0, 300, 300);
        canvas.drawArc(rect, 0, 270, false, mPaint);
        //绘制进度条
        canvas.drawArc(rect,0,mCurAngle,false,mProgressPaint);
        canvas.restore();
        canvas.save();
        canvas.translate(20,50);
        //开始计算进度
        int textNum = (int) (mCurAngle/2.7);
        String text = textNum+"%";
        float length = mTextPaint.measureText(text);
        Log.e("CUR",text);
        canvas.drawText(text,150-length/2,150,mTextPaint);
        canvas.restore();
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
            int desired = 340;
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = 340;
            height = desired;
        }

        setMeasuredDimension(width, height);
    }
}
