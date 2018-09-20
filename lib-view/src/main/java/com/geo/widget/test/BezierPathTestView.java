package com.geo.widget.test;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liwei on 2018/9/19.
 */

public class BezierPathTestView extends View {
    Paint mPaint;
    Path mPath;
    int WAVE_WIDTH = 180; //波纹距离
    int WAVE_HEIGHT = 20;  //波纹高度

    public BezierPathTestView(Context context) {
        super(context);
        init();
    }

    public BezierPathTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierPathTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#0097A7"));
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPath = new Path();
        startAnimation();
    }

    private void startAnimation() {
        ValueAnimator animator = ObjectAnimator.ofFloat(0, WAVE_WIDTH*4+WAVE_WIDTH/2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPath.reset();
                addPoint(value);
                invalidate();
            }
        });
        animator.setDuration(1500);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(-1);
        animator.start();
    }

    /**
     * 根据动画的值添加控制点和终点
     *
     * 【波浪效果的原理其实就是改变控制点和终点的X坐标来实现】
     *
     * @param value
     */
    private void addPoint(float value) {
        for (int i = -3; i < 5; i++) {
            int index = i * 2;
            if (i % 2 == 0) {
                mPath.quadTo(value + index * WAVE_WIDTH, +WAVE_HEIGHT, value + (index + 1) * WAVE_WIDTH, 0);
            } else {
                mPath.quadTo(value + index * WAVE_WIDTH, -WAVE_HEIGHT, value + (index + 1) * WAVE_WIDTH, 0);
            }
        }
        //这两句是让path形成封闭，填充颜色
        mPath.lineTo(1200, 2400);
        mPath.lineTo(0, 2400);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, 400);
        //画一个圆
        canvas.drawPath(mPath, mPaint);
    }
}
