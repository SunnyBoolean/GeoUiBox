package com.geo.widget;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.geostar.liwei.lib_view.R;

import static android.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * Created by liwei on 2017/5/29.
 * 进度条样式1
 */

public class ProgressV1View extends View {
    //背景框画笔
    Paint mPaint;
    //进度条画笔画笔
    Paint mTitlePaint;
    CirclePoint currentPoint;
    //进度条半径，默认是120
    private int mRadio = 120;
    //进度条颜色
    private int mColor = Color.GREEN;
    //画笔宽度
    private int mPaintWidth = 20;
    //小球颜色
    private int mTitleColor = Color.BLACK;

    public ProgressV1View(Context context) {
        super(context);
        initPaint();
    }

    public ProgressV1View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initArrt(context, attrs, 0);
        initPaint();
    }

    public ProgressV1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initArrt(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initArrt(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ProgressV1View, defStyle, 0);
        mRadio = (int) a.getDimension(R.styleable.ProgressV1View_pv1_radio, mRadio);// 半径
        mColor = (int) a.getColor(
                R.styleable.ProgressV1View_pv1_color, Color.parseColor("#FF78909C"));// 背景色
        mPaintWidth = (int) a.getDimension(R.styleable.ProgressV1View_pv1_titleradio, mRadio);//半径
        mTitleColor = (int) a.getColor(
                R.styleable.ProgressV1View_pv1_titlecolor, Color.parseColor("#FF78909C"));// 进度条颜色
        a.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mPaintWidth);

        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setStyle(Paint.Style.STROKE);
        mTitlePaint.setColor(mTitleColor);
        mTitlePaint.setStrokeWidth(mPaintWidth);
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
        animator.setupStartValues();
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getMeasuredWidth() / 2;
        int y = getMeasuredHeight() / 2;
        canvas.drawCircle(x, y, mRadio, mPaint);
        RectF rect = new RectF(mPaintWidth,mPaintWidth,x*2-mPaintWidth,y*2-mPaintWidth);
        canvas.drawArc(rect,currentPoint.angle,currentPoint.angle,false,mTitlePaint);
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
            int desired = (int) (getPaddingLeft() + getPaddingRight()) + mRadio * 2 + mPaintWidth * 2;
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + mRadio * 2 + getPaddingBottom()) + mPaintWidth * 2;
            height = desired;
        }

        setMeasuredDimension(width, height);
    }
}
