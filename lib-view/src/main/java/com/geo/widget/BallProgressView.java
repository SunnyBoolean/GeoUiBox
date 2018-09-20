package com.geo.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by liwei on 2018/9/19.
 */

public class BallProgressView extends View {
    private Paint mPaint;  //画笔
    private int mRadio=200; //球的半径：默认是200
    private int mPaintWidth=2;//画笔宽度
    private float mDrgee;//当前需要绘制的角度，根据设置的值计算
    public BallProgressView(Context context) {
        super(context);
        init();
    }

    public BallProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BallProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        setValue(70);
    }
    float mValue;
    /**
     * 设置值，范围是 [0,100]
     * @param value
     */
    private void setValue(float value){
        mDrgee = value*3.6f;
        mValue = value;
//        invalidate();
        anima();
    }
    float getmDrgee;
    private void anima(){
        ValueAnimator animator = ValueAnimator.ofFloat(-10,10);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                getmDrgee=value;
                invalidate();;
            }
        });
        animator.setDuration(1000);
        animator.setRepeatCount(-1);
//        animator.start();

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mPaintWidth,+mPaintWidth);//这里要平移画笔宽度个距离，不然的话圆圈不能居中
        canvas.drawCircle(mRadio,mRadio,mRadio,mPaint);
        Path path = new Path();
        RectF rect = new RectF(0,0,mRadio*2,2*mRadio);
        path.addArc(rect,90-mDrgee/2+getmDrgee,mDrgee);  //给路径添加一个圆弧，用于填充圆的实体部分

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        PathMeasure measure = new PathMeasure();
        measure.setPath(path,false);

        float sx = (float) (mRadio-mRadio*Math.cos(mDrgee));
        float sy= (float) (mRadio-mRadio*Math.sin(mDrgee));

        float ex= (float) (mRadio+mRadio*Math.cos(mDrgee));
//
        float length = (float) Math.sqrt(mRadio*mRadio*2-2*mRadio*mRadio*Math.cos(mDrgee));
        Log.e("ASSD","多长a:"+length);
//        Path p = new Path();
//        p.moveTo(sx,sy);
//        p.quadTo(length/4,sy,length/2,sy-15);
//        p.quadTo(length*3/4,sy+15,ex,sy);
        canvas.drawPath(path,mPaint);
//        canvas.drawPath(p,mPaint);
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
            int desired = (int) (getPaddingLeft() + getPaddingRight()) + mRadio * 2 +mPaintWidth*2;
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + getPaddingBottom()) +  + mRadio * 2 +mPaintWidth*2;
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
}
