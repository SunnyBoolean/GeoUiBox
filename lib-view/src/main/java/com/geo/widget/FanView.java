package com.geo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.geostar.liwei.lib_view.R;

/**
 * Created by liwei on 2018/9/20.
 * 风力发电机--风扇
 */

public class FanView extends View {
    private int mLeafHeight=150;//每一片叶子的长度
    private int mLeafWidth=17;//每一篇叶子的宽度
    private int mDotRadio=6;//中间小球半径
    Paint mPaint;

    public FanView(Context context) {
        super(context);
        init(null,0);
    }

    public FanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public FanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }
    Matrix matrix;
    private void init( AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.FanView, defStyleAttr, 0);
            mLeafHeight = (int) a.getDimension(R.styleable.FanView_fan_leaf_height, 150);
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.WHITE);
        matrix=new Matrix();
        anima();
    }
    private void anima(){
        ValueAnimator animator = ValueAnimator.ofFloat(1,11);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        animator.setRepeatCount(-1);
        animator.setDuration(5000);
        animator.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.translate(0,2);
        canvas.save();
        //画中间的小圆圈
        canvas.drawCircle(width/2,height/3+10,mDotRadio,mPaint);
        canvas.concat(matrix);
        matrix.postRotate(1, width/2,height/3+10);
        //画叶子
        for(int i=0;i<3;i++){
            canvas.save();
            canvas.rotate(120*i,width/2,height/3+10);
            Path path = new Path();
            path.moveTo(width/2,0);
            path.quadTo(width/2-mLeafWidth,mLeafHeight/2,width/2,mLeafHeight);
            path.quadTo(width/2+mLeafWidth,mLeafHeight/2,width/2,0);
            canvas.drawPath(path,mPaint);
            canvas.restore();
        }




        canvas.restore();
        float lt=(float) (mLeafHeight*2.5);
        //画电线杆子
        Path path = new Path();
        path.moveTo(width/2-3,height/3+25);
        path.lineTo(width/2-10,lt);
        path.quadTo(width/2,lt+10,width/2+10,lt);//控制点的Y坐标+10个值创造弧线，梅花效果
        path.lineTo(width/2+3,height/3+25);
        path.close();
        canvas.drawPath(path,mPaint);
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
            int desired = (int) (getPaddingLeft() + getPaddingRight()) + mLeafHeight * 3 + mDotRadio*2;
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + getPaddingBottom()) + mLeafHeight * 3 + mDotRadio*2;
            height = desired;
        }
        setMeasuredDimension(width, height);
    }

}
