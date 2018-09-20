package com.geo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by liwei on 2018/3/29.
 * 仿华为天气的曲线图
 */

public class ProgressViewV3View extends View {
    private Paint mPaint,mArcPaint,mDotPaint,mALinePaint;
    float leftPositoin[] = new float[2];  //坐标值
    float leftTan[] = new float[2];//正切值
    private float leftDistance;
    Path mPath;
    public ProgressViewV3View(Context context) {
        super(context);
    }

    public ProgressViewV3View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ProgressViewV3View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }
    PathMeasure measure;
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.WHITE);


        mArcPaint=new Paint();
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStrokeWidth(2);
        mArcPaint.setColor(Color.WHITE);

        mDotPaint =new Paint();
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setAntiAlias(true);
        mDotPaint.setStrokeWidth(2);
        mDotPaint.setColor(Color.YELLOW);

        mALinePaint = new Paint();
        mALinePaint.setStyle(Paint.Style.FILL);
        mALinePaint.setAntiAlias(true);
        mALinePaint.setStrokeWidth(1);
        mALinePaint.setColor(Color.parseColor("#fc6472"));


        mPath=new Path();
        mPath.moveTo(0,500);
        mPath.quadTo(500,-110,1000,500);
        mArcPaint.setPathEffect(new DashPathEffect(new float[]{5, 10}, 1));
         measure=new PathMeasure();
        measure.setPath(mPath,false);
        measure.getPosTan(leftDistance, leftPositoin, leftTan);

        animaPath();
    }

    private void animaPath(){
        ValueAnimator animator = ValueAnimator.ofFloat(0, measure.getLength());  //这里要减去球的半径
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //左侧动画
                leftDistance = (float) valueAnimator.getAnimatedValue();
                Log.e("PV3",leftDistance+"");
                measure.getPosTan(leftDistance, leftPositoin, leftTan);
                invalidate();
            }
        });
        animator.setDuration(4500);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(0,500);
        //首先绘制一条线
        canvas.drawLine(0,0,1000,0,mPaint);
        canvas.restore();
        canvas.save();
        //绘制弧线
        canvas.drawPath(mPath, mArcPaint);
        //绘制圆球
        float cx = leftPositoin[0];
        float cy = leftPositoin[1];
        float radio = 10;
        canvas.drawCircle(cx ,cy,radio,mDotPaint);
        //绘制射线

        Path path = new Path();
        RectF rect = new RectF(cx-35,cy-35,cx+35,cy+35);
        path.addArc(rect, 0, 360);

        Path rectPath = new Path();
        rectPath.addRect(0, 0, 5, 15, Path.Direction.CCW);
        //第一个参数16就是控制实体线的间距
        PathEffect pathEffect1 = new PathDashPathEffect(rectPath, 30, 10, PathDashPathEffect.Style.ROTATE);
        mALinePaint.setPathEffect(pathEffect1);
        canvas.drawPath(path, mALinePaint);
        canvas.restore();

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
            int desired = 1010;
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = 510;
            height = desired;
        }

        setMeasuredDimension(width, height);
    }
}
