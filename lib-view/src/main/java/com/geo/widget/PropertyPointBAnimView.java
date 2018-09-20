package com.geo.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liwei on 2018/3/22.
 * 本例用于演示属性动画来绘制不断改变坐标的圆，类似平移的效果
 */

public class PropertyPointBAnimView extends View {
    private VPoint mPoint;
    //画笔
    Paint mPaint;

    public PropertyPointBAnimView(Context context) {
        super(context);
        initAttr(context);
    }

    public PropertyPointBAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context);
    }

    public PropertyPointBAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context);
    }

    private void initAttr(Context context) {
        mPoint = new VPoint();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        startAnimation();
    }


    /**
     * 开始执行动画
     */
    private void startAnimation() {
        //这个圆的坐标会在50~200之间变换
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mPoint, "radio", 0, 360);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float temp = (float) (Math.cos(value)*50)+mPoint.getRadio();
                int x = getMeasuredWidth() / 2;
                float inX = x + mPoint.getRadio() * (float) Math.cos(value / 360 * 2 * Math.PI);
                mPoint.setPosition( inX );
                //这里用requestLayout（）因为需要重新测量布局的高度，如果用invalidate（）的话不会重新测量布局高度
                requestLayout();
            }
        });
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mPoint.getPosition(), mPoint.getPosition(), mPoint.getRadio(), mPaint);
    }
    /**
     * 创建一个圆实体类，里面有圆的半径
     */
    private class VPoint {
        private int radio = 50;//圆圈半径,默认半径是50
        private float position=200;//圆心的坐标

        public float getPosition() {
            return position;
        }

        public void setPosition(float position) {
            this.position = position;
        }

        public int getRadio() {
            return radio;
        }

        public void setRadio(int radio) {
            this.radio = radio;
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
            int desired = (int) (getPaddingLeft() + getPaddingRight()) +200 * 2;
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + 200 * 2 + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
}
