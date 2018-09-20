package com.geo.widget.test;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liwei on 2017/6/3.
 */

public class PathShaderView extends View {
    int mPosition = 500;

    public PathShaderView(Context context) {
        super(context);
    }

    public PathShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        animaPath();
    }

    public PathShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        animaPath();
    }

    /**
     * 绘制一条圆弧路径
     *
     * @param canvas
     */
    private void drawPath(Canvas canvas) {
        canvas.save();
        Paint paint = getPaint();
        Path path = new Path();
        paint.setStrokeWidth(2);
        RectF rect = new RectF(10, 10, 310, 310);
        path.addArc(rect, 0, 360);
        canvas.drawPath(path, paint);

        canvas.translate(400, 50);
        paint.setStrokeWidth(mPaintLength);
        //为圆弧添加一条虚线
        paint.setPathEffect(new DashPathEffect(new float[]{5, 10}, 1));
        paint.setDither(true);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private float mPaintLength;
    private void animaPath() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPaintLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(1500);
        animator.start();
    }



    /**
     * 绘制
     *
     * @param canvas
     */
    private void drawPathWithCornerPathEffect(Canvas canvas) {
        canvas.translate(0, 400);
        canvas.save();
        Paint paint = getPaint();
        paint.setStrokeWidth(5);
        Path path = new Path();
        // 定义路径的起点
        path.moveTo(10, 400);
        // 定义路径的各个点
        for (int i = 0; i <= 30; i++) {
            path.lineTo(i * 35, (float) (Math.random() * 100));
        }
        //这是没有加任何特效的折线
        canvas.drawPath(path, paint);
        canvas.restore();
        canvas.save();


        //+++++++++++++++   CornerPathEffect 这是加圆角的效果，拐点出会比较圆滑,唯一的参数就是一个半径   ++++++++++++
        canvas.translate(0, 100);
        paint.setPathEffect(new CornerPathEffect(150));
        canvas.drawPath(path, paint);
        canvas.restore();


        //+++++++++++++++ DiscretePathEffect:离散路径效果  ，其会在路径上绘制很多“杂点”的突出来模拟一种类似生锈铁丝的效果 ++++++++++++++
        canvas.save();
        canvas.translate(0, 200);
        //第一个参数是这些突出的点的密度，值越小杂点越密集
        //第二个参数呢则是“杂点”突出的大小，值越大突出的距离越大反之反之
        paint.setPathEffect(new DiscretePathEffect(1.0F, 5.0F));
        canvas.drawPath(path, paint);
        canvas.restore();
        canvas.save();

        //++++++++++++++  DashPathEffect  虚线路径
        canvas.translate(0, 300);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 1));
        canvas.drawPath(path, paint);
        canvas.restore();

        //++++++++++++++ PathDashPathEffect:这个也是虚线路线，但是我们可以自定义虚线的样式！
        canvas.save();
        canvas.translate(0, 400);

        Path tempPath = new Path();
        tempPath.addCircle(0, 0, 10, Path.Direction.CCW);
        //第一个参数是距离，第二个参数是啥玩意
        PathEffect pathEffect = new PathDashPathEffect(tempPath, 32, 28, PathDashPathEffect.Style.ROTATE);
        paint.setPathEffect(pathEffect);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    /**
     * @param canvas
     */
    private void drawCricleWithDash(Canvas canvas) {
        Paint paint = getPaint();
        canvas.translate(30, 800);
        paint.setStrokeWidth(60);
        //创建一个Path
        Path path = new Path();
        RectF rect = new RectF(10, 20, 410, 420);
        path.addArc(rect, -180, 180);
        //矩形的虚线，这个和上边默认的DashPathEffecty有一点区别的
        Path rectPath = new Path();
        rectPath.addRect(0, 0, 5, 40, Path.Direction.CCW);
        PathEffect pathEffect1 = new PathDashPathEffect(rectPath, 20, 8, PathDashPathEffect.Style.ROTATE);
        paint.setPathEffect(pathEffect1);
        canvas.drawPath(path, paint);


        //首先绘制一个蓝色的半圆背景
        canvas.translate(500, 0);
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(90);
        canvas.drawPath(path, paint);
        //然后添加虚线圆点
        Path cyclePath = new Path();
        paint.setColor(Color.WHITE);
        cyclePath.addCircle(0, 0, 15, Path.Direction.CCW);
        //第一个参数是距离，第二个参数是啥玩意
        //创建一个Path虚线
        PathEffect pathEffect = new PathDashPathEffect(cyclePath, 50, 8, PathDashPathEffect.Style.ROTATE);
        paint.setPathEffect(pathEffect);
        canvas.drawPath(path, paint);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制圆弧路径
        drawPath(canvas);
        //路径
        drawPathWithCornerPathEffect(canvas);
        //绘制虚线圆
        drawCricleWithDash(canvas);

    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        PathEffect as;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        return paint;
    }

    private int getColor(int id) {
        return getResources().getColor(id);
    }
}
