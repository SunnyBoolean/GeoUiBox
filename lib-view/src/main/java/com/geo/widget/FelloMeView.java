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
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.geostar.liwei.lib_view.R;

/**
 * Created by liwei on 2018/4/2.
 * 关注我和取消关注
 */

public class FelloMeView extends View {
    Paint mPaint, mBgPaint;
    private int mPathColor = Color.WHITE;//路径颜色
    private int mBgColor = Color.parseColor("#FF7043");

    public FelloMeView(Context context) {
        super(context);
        init(null,0);
    }

    public FelloMeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public FelloMeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,0);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        if(attrs!=null){
            TypedArray a =  getContext().obtainStyledAttributes(attrs,
                    R.styleable.FelloMeView, defStyleAttr, 0);
            mPathColor=a.getColor(R.styleable.FelloMeView_icon_color,Color.WHITE);
            mBgColor = a.getColor(R.styleable.FelloMeView_bg_color,mBgColor);
            a.recycle();
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPathColor);
        mPaint.setTextSize(30);
        mPaint.setStrokeWidth(3);

        mTextLenght = mPaint.measureText("关注");

    }

    private float mTextLenght;
    private boolean isFocused = false;//是否关注
    private int left,top,right,bottom;

    //关注
    public void focused() {
        mText = "已关注";
        mTextLenght = mPaint.measureText("已关注");
        setPadding(getPaddingLeft(),getPaddingTop(),getPaddingRight()+2,getPaddingBottom());
        scaleAnima();
        isFocused = true;
        roateAnima();


    }

    private String mText = "关注";

    public void unfocused() {
        mText = "关注";
        mTextLenght = mPaint.measureText("关注");
        scaleAnima();
        isFocused = false;
        isTranslateStart = false;
//        setPadding(left,top,right,bottom);
        setPadding(13,11,10,11);
        invalidate();

    }

    public boolean isFocused() {
        return isFocused;
    }

    private void scaleAnima() {
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 1.2f, 1.2f, 1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 1.2f, 1.2f, 1f);
        scaleXAnim.setRepeatMode(ObjectAnimator.REVERSE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleXAnim, scaleYAnim);
        set.setDuration(500);
        set.start();
    }

    private float mCurDrgee = 0;

    private void roateAnima() {
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 45);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurDrgee = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                treansAnima();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.setDuration(800);
        animation.start();
    }

    private boolean isTranslateStart = false;
    private float mCurLengh;

    /**
     * 开始执行位移
     */
    private void treansAnima() {
        isTranslateStart = true;
        ViewGroup.LayoutParams lp = getLayoutParams();
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 8);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurLengh = (float) animation.getAnimatedValue();

                invalidate();
            }
        });
        animation.setDuration(800);
        animation.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isTranslateStart){
            canvas.translate((getWidth() - 40 - mTextLenght) / 2-10, (getHeight() - 30) / 2);
        }else{
            canvas.translate((getWidth() - 40 - mTextLenght) / 2, (getHeight() - 30) / 2);
        }

        canvas.drawColor(mBgColor);
        canvas.save();
        if (isFocused) {//如果已经关注
            canvas.rotate(mCurDrgee, 18, 15);
        }
        if (isTranslateStart) {
            canvas.translate(0, mCurLengh);
        }
            canvas.translate(0, 15);
        if (isTranslateStart) {
            canvas.drawLine(10, 0, 30, 0, mPaint);//第一条横线
        } else {
            canvas.drawLine(0, 0, 30, 0, mPaint);//第一条横线

        }
        canvas.restore();

        canvas.save();
        if (isFocused) {//如果已经关注
            canvas.rotate(mCurDrgee, 18, 15);
        }
        if (isTranslateStart) {
            canvas.translate(mCurLengh + 5, -mCurLengh + 1);
        }
        canvas.translate(15, 0);//第二条横线
        canvas.drawLine(0, 0, 0, 30, mPaint);
        canvas.restore();

        //绘制文字
        canvas.save();
        if (isTranslateStart) {
            canvas.translate(50, 10);
        } else {
            canvas.translate(40, 10);
        }
        float lenght = mPaint.measureText(mText);
        canvas.drawText(mText, 0, 15, mPaint);
        canvas.restore();


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        left=getPaddingLeft();
        top = getPaddingTop();
        right = getPaddingRight();
        bottom=getPaddingBottom();
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int desired = (int) (40 + mTextLenght);

            width = desired + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = 30 + getPaddingTop() + getPaddingBottom();
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

}
