package com.geo.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.geostar.liwei.lib_view.R;

/**
 * Created by liwei on 2018/3/27.
 * A计划的头部View
 */

public class AplanHeaderView extends View {
    private Paint mBgPaint;
    private Paint mDotPaint;
    private float mPathLength;
    private Path mPath;
    private int mDotRadio = 20;
    private Bitmap mLeftBitmap, mRightBitmap,mEyeBitmap;
    private Matrix mMatrix, mMatrix1,mMatrix2;
    float leftPositoin[] = new float[2];  //坐标值
    float leftTan[] = new float[2];//正切值

    float rightPositoin[] = new float[2];  //坐标值
    float rightTan[] = new float[2];//正切值
    private float leftDistance, rightDistance;
    PathMeasure measure;

    public AplanHeaderView(Context context) {
        super(context);
        init(context);
    }

    public AplanHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AplanHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.parseColor("#fc6472"));
        mBgPaint.setStyle(Paint.Style.FILL);

        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setStyle(Paint.Style.FILL);

        mMatrix = new Matrix();
        mMatrix1 = new Matrix();
        mMatrix2 = new Matrix();

        mLeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_aplan_left);
        mRightBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_aplan_left);
        mEyeBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo_eye);
    }

    private void initPath() {
        int height = getHeight();
        int width = getWidth();
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(0, height - 260);
        mPath.quadTo(width / 2, height, width, height - 260);
        mPath.lineTo(width, 0);
        mPath.lineTo(0, 0);
        measure = new PathMeasure();
        measure.setPath(mPath, false);

        mPathLength = measure.getLength();

        measure.getPosTan(leftDistance, leftPositoin, leftTan);
        measure.getPosTan(rightDistance, rightPositoin, rightTan);
        startLeftAnimation();
//        startRightAnima();
    }

    /**
     * 开始左边的动画
     */
    private void startLeftAnimation() {
        final int height = getHeight() - 260;
        final int width = getWidth();
        ValueAnimator animator = ValueAnimator.ofFloat(height, height + (mPathLength - 2 * height - width) / 2);  //这里要减去球的半径
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //左侧动画
                leftDistance = (float) valueAnimator.getAnimatedValue();
                Log.e("PATH", "总长度：" + mPathLength + "  长度：" + height + "  宽度：" + width + "  距离：" + leftDistance);
                measure.getPosTan(leftDistance, leftPositoin, leftTan);

                //右侧动画
                rightDistance = 2*height + (mPathLength - 2 * height - width) - leftDistance;
                measure.getPosTan(rightDistance, rightPositoin, rightTan);
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRightEnd = true;
                startSlideAnima();
//                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setRepeatCount(2);//
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(1000);
        animator.start();
    }

    private boolean isRightEnd = false;


    private float mCurDrgee;
    private void startSlideAnima() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 115);  //这里要减去球的半径
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurDrgee = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRightEnd = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(800);
        animator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPath();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawPath(mPath, mBgPaint);
        canvas.restore();

        if (!isRightEnd) {  //执行滑动动画
            //计算旋转角度
            float degrees = (float) (Math.atan2(leftTan[1], leftTan[0]) * 180.0 / Math.PI);
            mMatrix.reset();
            mMatrix.postTranslate(leftPositoin[0] - mLeftBitmap.getWidth() / 2 - 10, leftPositoin[1] - mLeftBitmap.getHeight() / 2 - 20);   // 将图片绘制中心调整到与当前点重合
            canvas.drawBitmap(mLeftBitmap, mMatrix, mBgPaint);            mMatrix.postRotate(degrees, mLeftBitmap.getWidth() / 2, mLeftBitmap.getHeight() / 2);   // 旋转图片



            //计算旋转角度
            float degrees1 = (float) (Math.atan2(rightTan[1], rightTan[0]) * 180.0 / Math.PI);
            mMatrix1.reset();
            mMatrix1.postRotate(degrees1, mRightBitmap.getWidth() / 2, mRightBitmap.getHeight() / 2);   // 旋转图片
            mMatrix1.postTranslate(rightPositoin[0] - mRightBitmap.getWidth() / 2 - 10, rightPositoin[1] - mRightBitmap.getHeight() / 2 - 20);   // 将图片绘制中心调整到与当前点重合
            canvas.drawBitmap(mRightBitmap, mMatrix1, mBgPaint);
        } else {//滑动结束执行竖起动画
            //左侧滑板竖起动画
            mMatrix.reset();
            mMatrix.postRotate(-mCurDrgee, mLeftBitmap.getWidth() / 2, mLeftBitmap.getHeight() / 2);   // 旋转图片
            mMatrix.postTranslate(leftPositoin[0] - mLeftBitmap.getWidth() / 2+55 , leftPositoin[1] - mLeftBitmap.getHeight() / 2 - mCurDrgee*2);   // 将图片绘制中心调整到与当前点重合
            canvas.drawBitmap(mLeftBitmap, mMatrix, mBgPaint);

            //右侧滑板竖起动画
            mMatrix1.reset();
            mMatrix1.postRotate(mCurDrgee, mRightBitmap.getWidth() / 2, mRightBitmap.getHeight() / 2);   // 旋转图片
            mMatrix1.postTranslate(rightPositoin[0] - mRightBitmap.getWidth() / 2-55 , rightPositoin[1] - mRightBitmap.getHeight() / 2 - mCurDrgee*2);   // 将图片绘制中心调整到与当前点重合
            canvas.drawBitmap(mRightBitmap, mMatrix1, mBgPaint);


            mMatrix2.reset();
            mMatrix2.postTranslate(getWidth()/2 -45, rightPositoin[1] - mRightBitmap.getHeight() / 2 - mCurDrgee*2);   // 将图片绘制中心调整到与当前点重合
            canvas.drawBitmap(mEyeBitmap, mMatrix2, mBgPaint);

        }
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
            int desired = getScreenWidth(getContext());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = getScreenHeight(getContext());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    private int getScreenHeight(Context var0) {
        return var0.getResources().getDisplayMetrics().heightPixels;
    }

    private int getScreenWidth(Context var0) {
        return var0.getResources().getDisplayMetrics().widthPixels;
    }
}
