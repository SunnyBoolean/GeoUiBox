package com.geo.widget.test;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liwei on 2018/9/21.
 */

public class BezierTestView extends View {
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
        drawSin();
    }

    Paint mPaint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

//        canvas.drawPath(mPath, mPaint);
        canvas.drawCircle(pointF.x,pointF.y,10,mPaint);
        canvas.drawPoint(dx,dy,mPaint);
        canvas.drawPath(mPath,mPaint);
        userQuadTo(canvas);
    }

    Path mPath = new Path();
    PointF pointF = new PointF();
    Point point=new Point();
    float dx,dy;
    private void drawSin() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 6);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) animation.getAnimatedValue();
                float y = (float) (2.58 * Math.cos((2 * Math.PI * x) / 4.65));
                dx=x;
                dy=y;
                point.set((int)x,(int)y);
//                mPath.rQuadTo(x,y,x,1);
//                mPath.rq;

                pointF.offset(x,y);
                invalidate();
            }
        });
        animator.setDuration(5000);
        animator.start();
    }

    /**
     * Path的rQuadTo和quadTo都是四个参数，并且前两个数子是控制点坐标，后两个数字是结束点坐标
     * 但是rQuadTo的坐标参数都是相对于上一个数据点的坐标的，因此实际坐标=上一个数据点的坐标+参数。
     *
     * @param canvas
     */
    private void userQuadTo(Canvas canvas) {

        Path p = new Path();
        p.moveTo(200, 250);
        //rQuadTo的坐标是相对于上一个数据点的坐标
        //在这里，上一个数据点坐标是（200，200），所以这里四个参数都是相对于（200，200）的坐标
        //也就是说实际的控制点坐标和终点坐标都是在（200，200）的基础上之和。即：dx1=200+100，dy1=300+250，dx2=200+200，dy2=0+250
        p.rQuadTo(100, 300, 200, 0);
        canvas.drawPath(p, mPaint);
    }
}
