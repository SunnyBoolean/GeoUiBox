package com.geo.widget.test;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by liwei on 2018/9/21.
 */

public class BezierTestView extends View {
    Paint mPaint;
    Path mSinPath;
    public BezierTestView(Context context) {
        super(context);
        init();
    }

    public BezierTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mSinPath=new Path();
        mSinPath.moveTo(0,0);
        drawSinAnima();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制sin曲线
        drawSin(canvas);
        drawBsr(canvas);
    }
    private void drawBsr(Canvas canvas){
        canvas.save();
        canvas.translate(100,100);
        Path path = new Path();
        path.moveTo(100,100);
        path.quadTo(200,-20,300,100);//(200,0;300,100)
        path.quadTo(350,180,200,300);
        path.quadTo(50,180,100,100);
        canvas.drawPath(path,mPaint);
        canvas.restore();
    }

    /**
     * 绘制sin曲线
     * @param canvas
     */
    private void drawSin(Canvas canvas){
        canvas.save();
        canvas.translate(0,300);
        canvas.drawPath(mSinPath,mPaint);
        canvas.restore();
    }
    /**
     * 动态绘制一条Sin()函数曲线
     */
    private void drawSinAnima() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1080);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) animation.getAnimatedValue();
                float y = (float) (50 * Math.cos((2 * Math.PI * x) / 180));  //这个就是计算sin的值函数
                mSinPath.lineTo(x,y);
                invalidate();
            }
        });
        animator.setDuration(5000);
        animator.start();
    }
}
