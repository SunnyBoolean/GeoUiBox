package com.geo.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by liwei on 2018/4/27.
 */

public class RaingView extends View {
    private Paint mPaint;

    public RaingView(Context context) {
        super(context);
    }

    public RaingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public RaingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(6);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置为圆角

        genLength();
        animation();
    }

    //存放X和Y的线数据，key值为X坐标，value为Y值坐标
    private HashMap<Integer, Integer> mLineDor = new HashMap();

    /**
     * 随机生成50个长度不同的雨条，最长100
     */
    private void genLength() {
        for (int i = 0; i < 30; i++) {   //个数空值雨点的疏密
            int x = mRandom.nextInt(1080);
            int Min = -3920;
            int Max = 3900;
            int y = Min + (int) (Math.random() * ((Max - Min) + 1));
            mLineDor.put(x, y);
        }
    }

    Random mRandom = new Random();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Set<Integer> keyX = mLineDor.keySet();
        Iterator<Integer> iterator = keyX.iterator();
//        int length = mRandom.nextInt(80); //雨滴的长短变化
        while (iterator.hasNext()) {
            int x = iterator.next();
            int startY = mLineDor.get(x)+mSpeed;
            int stopY = startY +60;
//                Log.e("ERROD","startX= "+x+"  starty=  "+startY+"   stopy=  "+stopY);
            canvas.drawLine(x, startY, x, stopY, mPaint);
        }

    }

    private int mSpeed;//下降的距离

    private void animation() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 1920);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSpeed = (int) animation.getAnimatedValue();
                genLength();
                postInvalidate();
            }
        });

        animator.setDuration(2000);
        animator.setRepeatCount(-1);
        animator.start();
    }
}
