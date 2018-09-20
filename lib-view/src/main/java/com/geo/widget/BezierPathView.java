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
import android.view.MotionEvent;
import android.view.View;

import com.geo.util.ScreenUtil;
import com.geostar.liwei.lib_view.R;

/**
 * Created by liwei on 2018/3/22.
 * 贝塞尔曲线学习
 */

public class BezierPathView extends View {
    private Paint mPaint;
    private final int V_WIDTH = 800;
    float mSupX = 200, mSupY = 200;
    Matrix mMatrix;




    private int  width = 0;
    private int height = 0;
    private int baseLine = 0;// 基线，用于控制水位上涨的，这里是写死了没动，你可以不断的设置改变。
    private int waveHeight = 100;// 波浪的最高度
    private int waveWidth  ;//波长
    private float offset =0f;//偏移量
    private Bitmap mBoatBitmap;

    public BezierPathView(Context context) {
        super(context);
        init(context);
    }

    public BezierPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BezierPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private float mCurDia, mCurY;

    private void init(Context context) {
        initPaint(context);
        animaWave();
    }


    private void initPaint(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#0097A7"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);


        mMatrix=new Matrix();
        mBoatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_boat);
    }

    private float mDHeight;
    Path mPath;
    /**
     * 波浪动画
     */
    private float mDy = 200,mDrgee=180;

    private void animaWave() {
        mPath = new Path();
        int width = ScreenUtil.getScreenWidth(getContext());
        ValueAnimator animator = ValueAnimator.ofFloat(0,waveWidth);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatorValue = (float)valueAnimator.getAnimatedValue() ;
                offset = animatorValue;//不断的设置偏移量，并重画
                postInvalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animaWave();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setRepeatCount(1);//
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(1000);
        animator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.save();
//        Path path = new Path();
//        path.lineTo(mCurDia, mCurDia);
//        canvas.drawPath(path, mPaint);
//        canvas.restore();
//        canvas.save();
//        canvas.translate(200, 0);
//        Path path1 = new Path();
//        path1.moveTo(0, 200);
//        path1.quadTo(mSupX, mSupY, 600, 200);
//        canvas.drawPath(path1, mPaint);
//        canvas.restore();
//        canvas.save();
//
//        canvas.translate(0, 300);
        Path wavePath = getPath();
        //绘制波浪
        canvas.drawPath(wavePath,mPaint);
//        float position[] = new float[2];  //坐标值
//        float tan[] = new float[2];//正切值
//        PathMeasure measure =new PathMeasure();
//        measure.setPath(wavePath,false);
//        float distance=measure.getLength()/2;
//        measure.getPosTan(distance, position, tan);
//        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
//        mMatrix.reset();
//        mMatrix.postTranslate(getWidth()/2,height);   // 将图片绘制中心调整到与当前点重合
////        mMatrix.postRotate(degrees, mBoatBitmap.getWidth() / 2, mBoatBitmap.getHeight() / 2);   // 旋转小船
//        //绘制小船
//        canvas.drawBitmap(mBoatBitmap, mMatrix, mPaint);
//        PathMeasure measure = new PathMeasure();

    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();//获取屏幕宽度
        height = getMeasuredHeight();//获取屏幕高度
        waveWidth = width;
        baseLine = height/2;
        animaWave();
    }

    /**
     * 核心代码，计算path
     * @return
     */
    private Path  getPath(){
        int itemWidth = waveWidth/2;//半个波长
        Path mPath = new Path();
        mPath.moveTo(-itemWidth * 3, baseLine);//起始坐标
        //核心的代码就是这里
        for (int i = -3; i < 2; i++) {
            int startX = i * itemWidth;
            mPath.quadTo(
                    startX + itemWidth/2 + offset,//控制点的X,（起始点X + itemWidth/2 + offset)
                    getWaveHeigh( i ),//控制点的Y
                    startX + itemWidth + offset,//结束点的X
                    baseLine//结束点的Y
            );//只需要处理完半个波长，剩下的有for循环自已就添加了。
        }
        //下面这三句话很重要，它是形成了一封闭区间，让曲线以下的面积填充一种颜色，大家可以把这3句话注释了看看效果。
        mPath.lineTo(width,height);
        mPath.lineTo(0,height);
        mPath.close();
        return  mPath;
    }
    //奇数峰值是正的，偶数峰值是负数
    private int getWaveHeigh(int num){
        if(num % 2 == 0){
            return baseLine + waveHeight;
        }
        return baseLine - waveHeight;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int width;
//        int height;
//        if (widthMode == MeasureSpec.EXACTLY) {
//            width = widthSize;
//        } else {
//            int desired = V_WIDTH;
//            width = desired;
//        }
//
//        if (heightMode == MeasureSpec.EXACTLY) {
//            height = heightSize;
//        } else {
//            int desired = V_WIDTH;
//            height = desired;
//        }
//
//        setMeasuredDimension(width, height);
    }
}
