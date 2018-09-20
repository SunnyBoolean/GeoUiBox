package com.geo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by liwei on 2018/3/29.
 * 雷达图
 */

public class RadarView extends View{
    Paint mPaint;
    Paint mPointPaint;
    Matrix matrix;

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(false);
        mPaint.setStrokeWidth(2);

        mPointPaint=new Paint();
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setStrokeWidth(3);
        mPointPaint.setColor(Color.WHITE);
        mPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        matrix = new Matrix();
        initAnimation();
    }
    int curAngle=0;
    private void initAnimation() {

        final ValueAnimator animator = ValueAnimator.ofInt(0, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curAngle = (int) animation.getAnimatedValue();
                invalidate();

            }
        });
        //去掉默认的加速插值器
        animator.setInterpolator(null);
        animator.setRepeatCount(-1);//无限重复
        animator.setDuration(9000);
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    int[] SWEEP_GRADIENT_COLORS = new int[]{Color.parseColor("#C0C0C0"),Color.TRANSPARENT};
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //首先绘制背景
        // canvas.drawBitmap(mBackgroundBitmap, 0, 0, mPaint);
        int cx = getMeasuredWidth() / 2;
        int cy = getMeasuredHeight() / 2;
        //绘制内外三圈
        mPaint.setColor(Color.WHITE);
        //绘制内1圈
        canvas.drawCircle(cx, cy, 200, mPaint);
        //绘制内2圈
        canvas.drawCircle(cx, cy, 360, mPaint);
        //绘制内3圈
        canvas.drawCircle(cx, cy, 580, mPaint);
        int desWidth = 780*2-getMeasuredWidth();
        int desHeight = 780*2-getMeasuredHeight();
        //绘制最外圈
        canvas.drawCircle(cx, cy, 780, mPaint);

        canvas.save();
        //添加渐变:
        SweepGradient gradient = new SweepGradient(cx, cy, SWEEP_GRADIENT_COLORS, new float[]{0.001f,1.5f});  //此处1.5决定了雷达的尾巴有多长...
        mPointPaint.setShader(gradient);
        canvas.concat(matrix);
        matrix.postRotate(-1, cx, cy);
        RectF rect = new RectF(-desWidth/2,-desHeight/2,780*2-desWidth/2,780*2-desHeight/2);
        canvas.drawArc(rect,0,360,true,mPointPaint);
    }
}