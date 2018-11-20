package com.geo.widget;

import android.animation.TimeInterpolator;
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
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

import com.geo.util.ScreenUtil;
import com.geostar.liwei.lib_view.R;

/**
 * Created by liwei on 2018/11/15.
 */

public class DashBoradView extends View {
    private int mRadio = 280; //半径
    //普通画笔，背景通用
    private Paint mPaint;

    public DashBoradView(Context context) {
        super(context);
        initPaint();
    }

    public DashBoradView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public DashBoradView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private Path mOutPath;
    private PathMeasure mMeasure, mPointMeasure;
    private float mLength; //最外圈路径总长度
    private float mSegment;//每一个刻度之间的路径长度
    private final int MAXVALUE = 50;//刻度最大值
    private Path mNumPath;//绘制数字的Path
    private Path mPointPath;//绘制指针画笔
    private Paint mBlodPaint;//粗刻度画笔
    private Paint mThinPaint;//细刻度画笔
    private final int mYoffset = 90;  //数字和最外圈之间的偏移距离
    private final int mXofsset = 40;
    private final int mYoffset1 = 30;  //指针偏移的距离
    private final int mXofsset1 = 20;
    private Paint mNumPaint;//数字画笔
    private Path mBgPath;//背景色
    private Paint mBgPaint;
    private Paint mPointPaint;

    private void initPaint() {
        mPaint = new Paint();  //最外层画笔
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#616161"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(48);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.WHITE);
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(Color.parseColor("#FF7043"));
        mPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mBlodPaint = new Paint();  //粗刻度画笔
        mBlodPaint.setAntiAlias(true);
        mBlodPaint.setColor(Color.parseColor("#616161"));
        mBlodPaint.setStrokeWidth(5);
        mBlodPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mThinPaint = new Paint();  //细刻度画笔
        mThinPaint.setAntiAlias(true);
        mThinPaint.setColor(Color.parseColor("#616161"));
        mThinPaint.setStrokeWidth(2);
        mThinPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mNumPaint = new Paint();
        mNumPaint.setAntiAlias(true);
        mNumPaint.setColor(Color.parseColor("#00BCD4"));
        mNumPaint.setStyle(Paint.Style.STROKE);
        mNumPaint.setTextSize(48);

        mRadio = ScreenUtil.getScreenWidth(getContext()) / 2;


        mOutPath = new Path();
        mOutPath.moveTo(0, mRadio / 2);
        mOutPath.quadTo(mRadio, 0, mRadio * 2, mRadio / 2);

        mBgPath = new Path();
        mBgPath.moveTo(0, mRadio / 2);
        mBgPath.quadTo(mRadio, 0, mRadio * 2, mRadio / 2);
        mBgPath.lineTo(mRadio * 2, mRadio );
        mBgPath.lineTo(0, mRadio );
        mBgPath.lineTo(0, mRadio / 2);

        mNumPath = new Path();
        mNumPath.moveTo(mXofsset, mRadio / 2 + mYoffset);
        mNumPath.quadTo(mRadio + mXofsset, mYoffset, mRadio * 2 - mXofsset, mRadio / 2 + mYoffset);

//        mOutPath.moveTo(0, mRadio / 2);
//        mOutPath.quadTo(mRadio, 0, mRadio * 2, mRadio / 2);
        mPointPath = new Path();
        mPointPath.moveTo(mXofsset1, mRadio / 2 + mYoffset1);
        mPointPath.quadTo(mRadio + mXofsset1, mYoffset1, mRadio * 2 - mXofsset1, mRadio / 2 + mYoffset1);


        mMeasure = new PathMeasure();
        mMeasure.setPath(mNumPath, false);
        mLength = mMeasure.getLength();
        mSegment = mLength / MAXVALUE;

        mPointMeasure = new PathMeasure();
        mPointMeasure.setPath(mPointPath, false);
        mPointLength = mPointMeasure.getLength();

//        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_point);
    }

    private Bitmap mBitmap;
    float mPointLength;

    float positoin[] = new float[2];  //坐标值
    float rightTan[] = new float[2];//正切值
    private Matrix mMatrix = new Matrix();
    float mPositoin[] = new float[2];  //坐标值
    float mRightTan[] = new float[2];//正切值

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //绘制最外层圈
        canvas.translate(0, -mRadio / 4);
        canvas.drawPath(mBgPath, mBgPaint);
        canvas.drawPath(mOutPath, mPaint);

        //开始绘制刻度
        PathMeasure measure = new PathMeasure();
        measure.setPath(mOutPath, false);
        for (int i = 0; i <= MAXVALUE; i++) {
            float length = measure.getLength();
            float segment = length / MAXVALUE;
            float dis1 = segment * i;
            //绘制细刻度
            measure.getPosTan(dis1, positoin, rightTan);
            float degrees1 = (float) (Math.atan2(rightTan[1], rightTan[0]) * 180.0 / Math.PI);
            canvas.save();
            canvas.rotate(degrees1, positoin[0], positoin[1]);
            canvas.drawLine(positoin[0], positoin[1], positoin[0], positoin[1] + 20, mThinPaint);
            canvas.restore();
            //绘制粗刻度
            if (i % 10 == 0) {
                float dis = segment * i;
                measure.getPosTan(dis, positoin, rightTan);
                float degrees = (float) (Math.atan2(rightTan[1], rightTan[0]) * 180.0 / Math.PI);
                canvas.save();
                canvas.rotate(degrees, positoin[0], positoin[1]);
                canvas.drawLine(positoin[0], positoin[1], positoin[0], positoin[1] + 40, mBlodPaint);
                canvas.restore();
            }
        }
        //开始绘制刻度数字
        for (int i = 0; i <= MAXVALUE / 10; i++) {
            int index=i-1;
            float dis = mSegment * i * 10;
            mMeasure.getPosTan(Math.abs(dis), positoin, rightTan);
            float degrees = (float) (Math.atan2(rightTan[1], rightTan[0]) * 180.0 / Math.PI);
            int value = index * 10;
            canvas.drawText(value + "C°", (int) positoin[0] - 60, (int) positoin[1], mNumPaint);
        }
        canvas.save();
        canvas.drawCircle(mPositoin[0],mPositoin[1],15,mPointPaint);
        canvas.restore();
        //绘制今日气温字样
        float length = mNumPaint.measureText("今日气温");
        canvas.drawText("今日气温",mRadio-length/2,mRadio-90,mNumPaint);

    }

    private float mDrgee;

    /**
     * 设置温度
     * @param drgee
     */
    public void setDrgee(float drgee) {
        drgee=drgee+10;
        float length=drgee/50f*mPointLength;
        ValueAnimator animator = ValueAnimator.ofFloat(0, length);
        animator.setInterpolator(new BounceInterpolator());
        animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                mPointMeasure.getPosTan(val, mPositoin, mRightTan);

                mDrgee = (float) (Math.atan2(mRightTan[1], mRightTan[0]) * 180.0 / Math.PI);
                Log.e("AXSD", val + "     " + mDrgee + "");
                mMatrix.reset();
                invalidate();
            }
        });
        animator.setDuration(5000);
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
            int desired = mRadio + (int) (getPaddingTop() + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

}
