package com.geo.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by liwei on 2018/3/28.
 *  可收缩的TextView
 */

public class ExpendTextView extends TextView{
    private float mHeight;
    private float mCurHeight;
    private float readHeight;
    public ExpendTextView(Context context) {
        super(context);
    }

    public ExpendTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpendTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onAnimation(){
        ValueAnimator animator = ValueAnimator.ofFloat(0, +mHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                readHeight= (float) valueAnimator.getAnimatedValue();
                Log.e("ANIMA",readHeight+"");
                ((View)getParent()).invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(3000);
        animator.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取每一行的高度
        mHeight = measureLine();
        Log.e("ExTextview","onDraw()"+"---> 还有多高：="+mHeight);
    }

    private float measureLine(){
        int width = getWidth();
        float length=getPaint().measureText(getText().toString());
        //获取当前行数
        float lines = getLineCount();
        //计算真实的应该显示的行数
        float realLines = length/width;
        mCurHeight=lines*getLineHeight();
        //计算还有多少行没有显示
        return (realLines-lines)*getLineHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.e("ExTextview","onLayout()-->"+changed+"  left="+left +"   top="+top+"  right="+right+"  bottom="+bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int realHeight = (int) ((getLineCount()*getLineHeight())+Math.abs(readHeight));
        setMeasuredDimension(widthSize, realHeight);
        Log.e("ExTextview","onMeasure()-->"+"  widthSize="+widthSize +"   heightSize="+realHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        String str = getText().toString();
        float length=getPaint().measureText(str);
        Log.e("ExTextview","onFinishInflate()-->"+length+" -->"+str);
    }
}
