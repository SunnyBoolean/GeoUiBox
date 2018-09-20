package com.geo.widget;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.geostar.liwei.lib_view.R;

import static android.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * Created by liwei on 2017/5/29.
 */

public class ProgressView extends View {
    //进度条画笔
    Paint mPaint;
    //小球画笔
    Paint mTitlePaint;
    CirclePoint currentPoint;
    //进度条半径，默认是120
    private int mRadio = 120;
    //进度条颜色
    private int mColor = Color.GREEN;
    //小球半径
    private int mTitleRadio = 10;
    //小球颜色
    private int mTitleColor = Color.BLACK;

    public ProgressView(Context context) {
        super(context);
        initPaint();
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initArrt(context, attrs, 0);
        initPaint();
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initArrt(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initArrt(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ProgressView, defStyle, 0);
        mRadio = (int) a.getDimension(R.styleable.ProgressView_p_radio, mRadio);// 半径
        mColor = (int) a.getColor(
                R.styleable.ProgressView_p_color, Color.parseColor("#FF78909C"));
        mTitleRadio = (int) a.getDimension(
                R.styleable.ProgressView_titleradio, mTitleRadio);

        mTitleColor = (int) a.getColor(
                R.styleable.ProgressView_titlecolor, Color.parseColor("#FF78909C"));
        a.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(3);
        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTitlePaint.setColor(mTitleColor);
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
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentPoint = (CirclePoint) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        animator.setRepeatCount(-1);//无限重复
        animator.setDuration(1200);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getMeasuredWidth() / 2;
        int y = getMeasuredHeight() / 2;
        canvas.drawCircle(x, y, mRadio, mPaint);
        float inX = x + mRadio * (float) Math.sin(currentPoint.angle / 360 * 2 * Math.PI);
        float inY = y - mRadio * (float) Math.cos(currentPoint.angle / 360 * 2 * Math.PI);
        canvas.drawCircle(inX, inY, 10, mTitlePaint);
    }

    private class CirclePoint {
        float angle;

        public CirclePoint(float angle) {
            this.angle = angle;
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
            int desired = (int) (getPaddingLeft() + getPaddingRight()) + mRadio * 2 + mTitleRadio * 2;
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + mRadio * 2 + getPaddingBottom()) + mTitleRadio * 2;
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
}
