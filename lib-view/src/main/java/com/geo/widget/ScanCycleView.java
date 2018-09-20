package com.geo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.geostar.liwei.lib_view.R;

/**
 * Created by liwei on 2017/5/26.
 * 扫描View
 */

public class ScanCycleView extends View {
    //半径，也就是控件的高和宽，可以通过属性文件自定义
    private int mRadio = 280;
    //小球半径
    private int mTitleRadio = 10;
    //圆环颜色
    private int mColor = Color.BLUE;
    //小球颜色
    private int mTitleColor = Color.BLACK;
    //是否缺口
    private boolean isSweep = false;
    //是否需要渐变
    private boolean isGradlint = false;
    //缺省角度
    private int mSweepAngel = 90;
    //普通画笔，背景通用
    private Paint mPaint;
    //前景深色画笔
    private Paint mFgPaint;
    CirclePoint currentPoint=new CirclePoint(46);
    //渐变颜色数组
    public static final int[] SWEEP_GRADIENT_COLORS = new int[]{ Color.GREEN,Color.BLUE, Color.YELLOW};
    public ScanCycleView(Context context) {
        super(context);
        initPaint();
    }

    public ScanCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initArrt(context, attrs, 1);
        initPaint();
    }

    public ScanCycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initArrt(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void initArrt(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ScanCycleView, defStyle, 0);
        mRadio = (int) a.getDimension(R.styleable.ScanCycleView_s_radio, mRadio);// 半径
        mColor = (int) a.getColor(
                R.styleable.ScanCycleView_s_color, Color.parseColor("#fcfefe"));//
        mTitleRadio = (int) a.getDimension(
                R.styleable.ScanCycleView_s_titleradio, mTitleRadio);//小球半径

        mTitleColor = (int) a.getColor(
                R.styleable.ScanCycleView_s_titlecolor, Color.parseColor("#FF78909C"));// 小球颜色
        isSweep = a.getBoolean(R.styleable.ScanCycleView_issweep, false);
        //缺口角度
        mSweepAngel = a.getInt(R.styleable.ScanCycleView_sweepangle, mSweepAngel);
        //是否需要渐变
        isGradlint = a.getBoolean(R.styleable.ScanCycleView_isgradlint, false);
        a.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);


        mFgPaint = new Paint();
        mFgPaint.setAntiAlias(true);
        mFgPaint.setColor(mTitleColor);
        mFgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        if(!isSweep)//如果是有缺口就不要动画
        initAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int desired = mRadio * 2 + getPaddingRight() + (int) (getPaddingLeft());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = mRadio * 2 + (int) (getPaddingTop() + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCycle(canvas);

    }

    /**
     * 初始化动画
     */
    private void initAnimation() {

        final ValueAnimator animator = ValueAnimator.ofFloat(0, 360);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = new CirclePoint((Float) animation.getAnimatedValue());
                invalidate();

            }
        });
        //去掉默认插值器，使小球能匀速运动
        animator.setInterpolator(null);
        animator.setRepeatCount(-1);//无限重复
        animator.setDuration(2000);
        animator.start();
    }

    /**
     * 开始绘制
     * @param canvas
     */
    private void drawCycle(Canvas canvas) {
        //首先构建一个Path
        Path path = new Path();
        RectF rect = new RectF(0, 0, mRadio * 2, mRadio * 2);
        //圆弧形
        if (isSweep) {//需要留缺口
            path.addArc(rect, -90, 360 - mSweepAngel);
            int roteAngle = 180 - mSweepAngel/2;
            canvas.rotate(-roteAngle,getMeasuredWidth()/2,getMeasuredHeight()/2);

        } else { //不需要留缺口
            path.addArc(rect, 0, 360);
        }
        //是否需要渐变
        if (isGradlint) {//需要渐变
            SweepGradient gradient = new SweepGradient(mRadio, mRadio, SWEEP_GRADIENT_COLORS, null);
            mPaint.setShader(gradient);
        } else {//不需要渐变

        }
        Path rectPath = new Path();
        rectPath.addRect(0, 0, 5, 65, Path.Direction.CCW);
        //第一个参数16就是控制实体线的间距
        PathEffect pathEffect1 = new PathDashPathEffect(rectPath, 16, 8, PathDashPathEffect.Style.ROTATE);
        mPaint.setPathEffect(pathEffect1);
        canvas.drawPath(path, mPaint);
        //绘制小球
        float inX = mRadio + (mRadio - 85) * (float) Math.sin(currentPoint.angle / 360 * 2 * Math.PI);
        float inY = mRadio - (mRadio - 85) * (float) Math.cos(currentPoint.angle / 360 * 2 * Math.PI);
        canvas.drawCircle(inX, inY, mTitleRadio, mFgPaint);
    }
    private class CirclePoint {
        float angle;
        public CirclePoint(float angle) {
            this.angle = angle;
        }
    }
}
