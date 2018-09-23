package com.geo.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.geostar.liwei.lib_view.R;

/**
 * Created by liwei on 2018/3/22.
 * 支付宝支付失败和成功的动画
 */

public class AlipayView extends View {
    private  Paint mPaint;
    private int mRadio = 180;
    private OkPathEntry mSuccessEntry;
    private CircleEntry mCircleEntry;
    private  float START_X ;//对勾的起始位置X坐标
    private  float START_Y ;//对勾的起始位置Y坐标，为了美观，y坐标偏移10个单位
    private Path mAnimaOkPath; //成功的动画路径
    private Path mAnimaFailedLeftPath,mAnimaFailedRightPath;
    private Path mLinPath;  //要绘制的路径
    private Path mLinPath1;
    private final int ARCWIDTH = 15;//环的宽度
    private int mSuccessColor;//成功的颜色
    private int mNormalColor;//正常颜色
    private int mFailedColor;//失败颜色
    
    int mViewType=0;  //0表示支付成功，1表示支付失败
    public AlipayView(Context context) {
        super(context);
        init(context);
    }

    
    
    public AlipayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.AlipayView, 0, 0);
        mViewType = a.getInt(R.styleable.AlipayView_viewType, 0);
        mNormalColor = a.getColor(R.styleable.AlipayView_pay_normal_color,Color.GRAY);
        mSuccessColor = a.getColor(R.styleable.AlipayView_pay_success_color,Color.BLUE);
        mFailedColor = a.getColor(R.styleable.AlipayView_pay_faled_color,Color.RED);
        mRadio =  (int) a.getDimension(R.styleable.AlipayView_pay_radio,180);
        START_X = mRadio / 2+10;//对勾的起始位置X坐标
       START_Y = mRadio;//对勾的起始位置Y坐标，为了美观，y坐标偏移10个单位
        a.recycle();
        init(context);
    }

    public AlipayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.AlipayView, defStyleAttr, 0);
        mViewType = a.getInt(R.styleable.AlipayView_viewType, 0);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(mNormalColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(ARCWIDTH);
        mPaint.setAntiAlias(true);

        //成功的路径
        mAnimaOkPath = new Path();
        mAnimaOkPath.moveTo(START_X, START_Y); //中间对勾的起始位置
        mAnimaOkPath.lineTo(mRadio, mRadio + mRadio * 2 / 4);  //第二个点位
        mAnimaOkPath.lineTo(mRadio + mRadio / 2, mRadio / 2 + 30);

        //失败左边线条路径
        mAnimaFailedLeftPath = new Path();
        mAnimaFailedLeftPath.moveTo(mRadio/2,mRadio/2);
        mAnimaFailedLeftPath.lineTo(mRadio+mRadio/2,mRadio+mRadio/2);

        //失败右边线条路径
        mAnimaFailedRightPath = new Path();
        mAnimaFailedRightPath.moveTo(mRadio/2,mRadio+mRadio/2);
        mAnimaFailedRightPath.lineTo(mRadio+mRadio/2,mRadio/2);

        //真正绘制的路径
        mLinPath = new Path();
        
    }
    //支付成功调用此方法
    public void paySuccess(){
        mViewType=0;
        mLinPath.reset();
        if(mLinPath1!=null){
            mLinPath1.reset();
        }
        mLinPath.moveTo(START_X, START_Y);
        mSuccessEntry = new OkPathEntry();
        mPaint.setColor(mNormalColor);
        mSuccessEntry.x=START_X;
        mSuccessEntry.y=START_Y;
        startCircleAnim();
    }
    //支付失败调用此方法
    public void payFailed(){
        mViewType=1;
        mLinPath.reset();
        if(mLinPath1!=null){
            mLinPath1.reset();
        }
        mLinPath.moveTo(mRadio/2,mRadio/2);
        mPaint.setColor(mNormalColor);
        mSuccessEntry = new OkPathEntry();
        mSuccessEntry.x=mRadio/2;
        mSuccessEntry.y=mRadio/2;
        startCircleAnim();
    }
    /**
     * 绘制圆圈
     */
    private void startCircleAnim(){
        mCircleEntry = new CircleEntry();
        ObjectAnimator animation =  ObjectAnimator.ofFloat(mCircleEntry,"drgee",0,360);
        animation.setDuration(1000);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(mViewType==0) {
                    //圆圈绘制完毕后就开始绘制对勾
                    animaOkLine();
                }else if(mViewType==1){
                    //绘制X
                    animaFailedLine();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.start();
    }
    /**
     * 对勾路径动画
     */
    private void animaOkLine() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mSuccessEntry, "x", "y", mAnimaOkPath);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLinPath.lineTo(mSuccessEntry.x, mSuccessEntry.y);
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画执行完毕,缩放一下
                startScaleAnim();
                //同时修改颜色
                mPaint.setColor(mSuccessColor);
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    /**
     * 失败路径
     */
    private void animaFailedLine(){

        ObjectAnimator animator = ObjectAnimator.ofFloat(mSuccessEntry, "x", "y", mAnimaFailedLeftPath);
       final  ObjectAnimator animator1 = ObjectAnimator.ofFloat(mSuccessEntry, "x", "y", mAnimaFailedRightPath);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLinPath.lineTo(mSuccessEntry.x, mSuccessEntry.y);
                Log.e("ASXD",mSuccessEntry.x+"   "+mSuccessEntry.y);
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLinPath1 = new Path();
                mLinPath1.moveTo(mRadio/2,mRadio+mRadio/2);
                mSuccessEntry.x=mRadio/2;
                mSuccessEntry.y=mRadio+mRadio/2;
                animator1.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(600);
        animator.start();



        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLinPath1.lineTo(mSuccessEntry.x, mSuccessEntry.y);
                Log.e("ASXD",mSuccessEntry.x+"   "+mSuccessEntry.y);
                invalidate();
            }
        });
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画执行完毕,缩放一下
                startScaleAnim();
                //同时修改颜色
                mPaint.setColor(mFailedColor);
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.setDuration(600);


    }

    /**
     * 绘制完成后放大一下，凸显效果
     * 此方法是成功和失败界面公用
     */
    private void startScaleAnim() {
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 1.2f, 1.2f, 1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 1.2f, 1.2f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleXAnim, scaleYAnim);
        set.setDuration(400);
        set.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mCircleEntry==null){
            return;
        }
        canvas.translate(50,50);
        //画圆圈
        canvas.drawArc(ARCWIDTH, ARCWIDTH, mRadio * 2 + ARCWIDTH, mRadio * 2 + ARCWIDTH, 0, mCircleEntry.drgee, false, mPaint);
        canvas.translate(ARCWIDTH,ARCWIDTH);
        //绘制动画
        canvas.drawPath(mLinPath, mPaint);
        if(mLinPath1!=null){
            canvas.drawPath(mLinPath1,mPaint);
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
            int desired = mRadio * 2 + ARCWIDTH * 2 + 100;
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = mRadio * 2 + ARCWIDTH * 2 + 100;
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    /**
     * 圆圈实体类
     */
    private class CircleEntry{
        private float drgee;

        public float getDrgee() {
            return drgee;
        }

        public void setDrgee(float drgee) {
            this.drgee = drgee;
        }
    }
    /**
     * 对勾动画辅助实体类
     */
    private class OkPathEntry {
        private float x ;  //注意哦，这个x、和y初始值必须默认改为对勾的起视坐标才行哈
        private float y ;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}
