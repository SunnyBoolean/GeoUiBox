package com.geo.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.geostar.liwei.lib_view.R;

import java.util.Random;

/**
 * Created by liwei on 2018/9/26.
 * 根据录音时的分贝显示录音的状态
 */

public class AudionWaveView extends View {
    public AudionWaveView(Context context) {
        super(context);
        init();
    }

    public AudionWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudionWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Paint mPaint;
    Path mPath;

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getContext().getColor(R.color.material_teal_300));
        mPaint.setAntiAlias(true);
        mPath = new Path();
        mPath.moveTo(0, 0);
//        startAnima();
    }

    float x, y=0;
    float dy;
    final Random random = new Random();
    public void setAudio(int audio) {
        y = audio;
        invalidate();
    }
    private void startAnima(){
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1080);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                x = (float) animation.getAnimatedValue();
                Log.e("assax",x+"");

                if(x>=900){
                    mPath.reset();
                    mPath.moveTo(0,0);
                }else{
                    int ty = (int)y;
                    if(y!=0){
                    }
                    mPath.lineTo(x,-ty);
                }
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mPath = new Path();
                mPath.moveTo(0,0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(3000);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0,300);
        int org= 5;
        for(int i=0;i<20;i++){
            canvas.translate(10,0);
            if(i<10){
                org=org+5;
            }else{
                org=org-5;
            }
            if(i>7&&i<10){
                org= (int) (org+y);
            }
            canvas.drawLine(0,-org/2,0,org/2,mPaint);
        }
//        canvas.drawLine(0,0,1080,0,mPaint
//        );
//        canvas.drawPath(mPath, mPaint);

    }
}
