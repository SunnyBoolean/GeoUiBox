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
 * 本例用于演示属性动画来绘制一个不断改变半径的圆，类似缩放效果
 */

public class PropertyPointAnimView extends View {
    private VPoint mPoint;
    //画笔
    Paint mPaint;

    public PropertyPointAnimView(Context context) {
        super(context);
        initAttr(context);
    }

    public PropertyPointAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context);
    }

    public PropertyPointAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mPoint.getRadio(), mPoint.getRadio(), mPoint.getRadio(), mPaint);
    }

    /**
     * 开始执行动画
     */
    private void startAnimation() {
        //这个圆的半径将会在50~200之间不断变换，从而让圆不断缩小-放大-缩小-放大...
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mPoint, "radio", 50, 200);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPoint.setRadio((int) value);
                //这里用requestLayout（）因为需要重新测量布局的高度，如果用invalidate（）的话不会重新测量布局高度
                requestLayout();
            }
        });
        objectAnimator.setRepeatCount(2);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }

    /**
     * 创建一个圆实体类，里面有圆的半径
     */
    private class VPoint {
        private int radio = 50;//圆圈半径,默认半径是50

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
            int desired = (int) (getPaddingLeft() + getPaddingRight()) + mPoint.getRadio() * 2;
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + mPoint.getRadio() * 2 + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
}
