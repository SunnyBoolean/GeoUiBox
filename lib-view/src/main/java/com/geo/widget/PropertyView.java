package com.geo.widget;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.geostar.liwei.lib_view.R;

import static android.animation.ValueAnimator.AnimatorUpdateListener;
import static android.animation.ValueAnimator.ofInt;

/**
 * Created by liwei on 2017/5/29.
 */

public class PropertyView extends View {
    Paint mPaint;
    int intcurrentValue = 100;
    CirclePoint currentPoint;

    public PropertyView(Context context) {
        super(context);
        initPaint();
    }

    public PropertyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public PropertyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    int outX, outY;

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        initAnimation();
    }

    private void initAnimation() {
        final ValueAnimator animator = ValueAnimator.ofObject(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                CirclePoint start = (CirclePoint) startValue;
                CirclePoint end = (CirclePoint) endValue;
                float startAngle = start.angle;
                float endAngle = end.angle;
                float currentAngle = startAngle + (endAngle - startAngle) * fraction;
                return new CirclePoint(currentAngle);
            }
        }, new CirclePoint(0), new CirclePoint(360));


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentPoint = (CirclePoint) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        animator.setRepeatCount(-1);//无限重复
        animator.setDuration(1000);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(intcurrentValue, 100, 50, mPaint);
        outX = getWidth() / 2;
        outY = getHeight() / 2;
        int distance = 120;
        canvas.drawCircle(outX, outY, 120, mPaint);
        float inX = outX + distance * (float) Math.sin(currentPoint.angle / 360 * 2 * Math.PI);
        float inY = outY - distance * (float) Math.cos(currentPoint.angle / 360 * 2 * Math.PI);
        canvas.drawCircle(inX, inY, 10, mPaint);

    }

    private class CirclePoint {
        float angle;

        public CirclePoint(float angle) {
            this.angle = angle;
        }
    }
}
