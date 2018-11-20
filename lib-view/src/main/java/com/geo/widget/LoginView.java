package com.geo.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.geostar.liwei.lib_view.R;

import java.security.acl.Group;

/**
 * Created by liwei on 2018/11/14.
 */

public class LoginView extends View {
    private Paint mBgPaint;
    private int mWidth = 1000;//宽度
    private int mHeight = 160;//高度
    private int mBgColor = Color.BLUE;
    private float mTextSize;//字体大小
    private String mText;//文字内容
    private int mTextColor = Color.WHITE;//文字颜色
    private Paint mTextPaint;
    private int mPressColor;//按下时的背景颜色
    private Paint mRectFPaint;
    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LoginView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private Path mPath;
    boolean isInit=false;
    private void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.LoginView, defStyleAttr, 0);
            mBgColor = a.getColor(R.styleable.LoginView_bg, mBgColor);
            mTextSize = a.getDimension(R.styleable.LoginView_textsize, 16);
            mText = a.getString(R.styleable.LoginView_text);
            mTextColor = a.getColor(R.styleable.LoginView_ltextColor, mTextColor);
            mPressColor = a.getColor(R.styleable.LoginView_pressbg,mBgColor);
            mText = TextUtils.isEmpty(mText) ? "" : mText;
            a.recycle();
        }
        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBgPaint.setStrokeWidth(1);
        mBgPaint.setColor(mBgColor);
        mBgPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mRectFPaint= new Paint();
        mRectFPaint.setStyle(Paint.Style.STROKE);
        mRectFPaint.setColor(Color.TRANSPARENT);

        mPath = new Path();
        initPath(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, mHeight);
        canvas.drawPath(mPath, mBgPaint);
        if(!isInit) {  //以下内容只需要绘制一次
            //绘制文字，这里要解决文字居中方案
            Rect targetRect = new Rect(0, 0, mWidth, mHeight);
            canvas.drawRect(targetRect, mRectFPaint);
            Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
            int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mText, targetRect.centerX(), baseline, mTextPaint);
        }
    }

    private int flag = 0;//0表示按下，1表示松开
    private long lastmin;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                flag = 0;
                lastmin = System.currentTimeMillis();
                mBgPaint.setColor(mPressColor);
                if (isAnimaend) {
                    startPress();
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                flag = 1;
                long dis = System.currentTimeMillis() - lastmin;
                mBgPaint.setColor(mBgColor);
                if (isAnimaend) {
                    startUp();
                }
            }
        }
        return true;
    }

    private int lastY;

    /**
     * 手指按下
     */
    private void startPress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                boolean isOk=true;
                while (flag == 0&&isOk) {
                    try {
                        Thread.sleep(5);
                        lastY = count<<2;
                        if (lastY < mHeight) {
                            count++;
                            initPath(lastY);
                            mBgPaint.setColor(mPressColor);
                            postInvalidate();
                        }else{
                            isOk=false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    boolean isAnimaend = true; //反弹动画是否执行完毕

    public void startUp() {
        ValueAnimator animator = ValueAnimator.ofFloat(lastY, -lastY, -lastY, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                isAnimaend = false;
                float value = (float) animation.getAnimatedValue();
                mBgPaint.setColor(mBgColor);
                initPath(value);
                postInvalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimaend = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(500);
        animator.start();
        if(mListener!=null){
            mListener.onLogin();
        }
    }

    private void initPath(float cy) {
        int radio = 20;//圆角半径
        Path path = new Path();
        path.moveTo(radio, 0);
        path.quadTo(0, 0, 0, radio); //左上圆角 控制点
        path.lineTo(0, mHeight - radio);
        path.quadTo(0, mHeight, radio, mHeight); //左下圆角 控制点
        path.quadTo(mWidth/2,mHeight-cy,mWidth - radio, mHeight);//下中控制点
        path.quadTo(mWidth, mHeight, mWidth, mHeight - radio); //右下下圆角 控制点
        path.lineTo(mWidth, radio);
        path.quadTo(mWidth, 0, mWidth - radio, 0); //右上圆角 控制点
        path.quadTo(mWidth / 2, cy, radio, 0);  //上中控制点
        path.lineTo(radio, 0);
        mPath = path;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top-lastY, right+lastY, bottom);
        Log.e("ASKOPD","onLayout()");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        Log.e("ASKOPD","onMeasure()");
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int desired = mWidth;
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = mHeight*3;
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
    OnLoginListener mListener;
    public void setOnLoginListener(OnLoginListener onLoginListener){
        this.mListener = onLoginListener;
    }
    public interface  OnLoginListener{
        public void onLogin();
    }
}
