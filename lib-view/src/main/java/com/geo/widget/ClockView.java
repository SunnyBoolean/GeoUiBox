package com.geo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.geostar.liwei.lib_view.R;

import java.util.Calendar;

/**
 * Created by liwei on 2018/9/25.
 */

public class ClockView extends View {
    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int mRadio = 300;//最外圈的半径
    private Paint mOutPaint;  //最外层画笔
    private Paint mOut2Paint;//倒数第二层画笔
    private Paint mAntiPaint;//第一圈锯齿
    private Paint mAnti2Paint;//第二圈锯齿
    private Paint mMinPaint;//秒格
    private Paint mHourPaint;//时格
    private Paint mTextPaint;//数字
    private Path mTextPath;
    private Paint mHourPointPaint;//时针
    private Paint mMinPointPaint;//分针
    private Paint mSecondPointPaint;//秒针
    PathMeasure measure = new PathMeasure();
    private float segment;
    float positoin[] = new float[2];  //坐标值
    float rightTan[] = new float[2];//正切值

    private void init() {
        mOutPaint = new Paint();
        mOutPaint.setStrokeWidth(1);
        mOutPaint.setColor(getContext().getColor(R.color.material_grey_color_300));
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setAntiAlias(true);

        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mOut2Paint = new Paint();
        mOut2Paint.setStrokeWidth(8);
        mOut2Paint.setColor(getContext().getColor(R.color.material_grey_color_300));
        mOut2Paint.setStyle(Paint.Style.STROKE);
        mOut2Paint.setAntiAlias(true);
        //设置模糊效果,第一个参数是模糊的半径，半径越大，模糊的范围就越大，第二个参数是模糊的样式，总共有三种样式，大家可以自己尝试下
        //NORMAL会模糊里外两边
        MaskFilter mBlur = new BlurMaskFilter(6, BlurMaskFilter.Blur.SOLID);
        mOut2Paint.setMaskFilter(mBlur);

        mAntiPaint = new Paint();
        mAntiPaint.setStrokeWidth(1);
        mAntiPaint.setColor(getContext().getColor(R.color.material_grey_color_300));
        mAntiPaint.setStyle(Paint.Style.STROKE);
        mAntiPaint.setAntiAlias(true);
        mAntiPaint.setMaskFilter(mBlur);

        mAnti2Paint = new Paint();
        mAnti2Paint.setStrokeWidth(1);
        mAnti2Paint.setColor(getContext().getColor(R.color.material_grey_color_500));
        mAnti2Paint.setStyle(Paint.Style.STROKE);
        mAnti2Paint.setAntiAlias(true);

        mMinPaint = new Paint();
        mMinPaint = new Paint();
        mMinPaint.setStrokeWidth(15);
        mMinPaint.setColor(getContext().getColor(R.color.material_grey_color_400));
        mMinPaint.setStyle(Paint.Style.STROKE);
        mMinPaint.setAntiAlias(true);

        mHourPaint = new Paint();
        mHourPaint.setStrokeWidth(18);
        mHourPaint.setColor(getContext().getColor(R.color.material_grey_color_700));
        mHourPaint.setStyle(Paint.Style.STROKE);
        mHourPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setColor(getContext().getColor(R.color.material_grey_color_700));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(36);

        //-------数字相关
        mTextPath = new Path();
        int distance = 74;
        RectF rectF = new RectF(distance, distance, mRadio * 2 - distance, mRadio * 2 -distance);
        mTextPath.addArc(rectF, -90, 360);
        measure.setPath(mTextPath, true);
        float length = measure.getLength();
        segment = length/12;

        mHourPointPaint = new Paint();
        mHourPointPaint.setStrokeWidth(20);
        mHourPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mHourPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mHourPointPaint.setColor(getContext().getColor(R.color.material_grey_color_700));
        mHourPointPaint.setAntiAlias(true);

        mMinPointPaint= new Paint();
        mMinPointPaint.setStrokeWidth(10);
        mMinPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mMinPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mMinPointPaint.setColor(getContext().getColor(R.color.material_grey_color_700));
        mMinPointPaint.setAntiAlias(true);

        mSecondPointPaint= new Paint();
        mSecondPointPaint.setStrokeWidth(3);
        mSecondPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mSecondPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSecondPointPaint.setColor(getContext().getColor(R.color.colorControlActivated));
        mSecondPointPaint.setAntiAlias(true);

        startSecondAnima();
    }
    private int curAngle;
    private void startSecondAnima(){
        ValueAnimator animator= ValueAnimator.ofInt(0,360);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curAngle= (int) animation.getAnimatedValue();
                long time=animation.getCurrentPlayTime();
                invalidate();
            }
        });
        animator.setDuration(60*1000);
        animator.setRepeatCount(-1);
        animator.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(1, 1);
        //绘制最外层圈
        canvas.drawCircle(mRadio, mRadio, mRadio, mOutPaint);
        //绘制倒数第二层圈,半径需要减10
        canvas.drawCircle(mRadio, mRadio, mRadio - 20, mOut2Paint);


        //绘制第一层ju锯齿
        Path path = new Path();
        RectF rect = new RectF(20, 20, mRadio * 2 - 20, mRadio * 2 - 20);
        path.addArc(rect, 0, 360);
        Path rectPath = new Path();
        //第四个参数决定齿轮的每一根刺的长度
        rectPath.addRect(0, 0, 1, 13, Path.Direction.CCW);
        //第一个参数16就是控制实体线的间距
        PathEffect pathEffect = new PathDashPathEffect(rectPath, 3, 8, PathDashPathEffect.Style.ROTATE);
        mAntiPaint.setPathEffect(pathEffect);
        canvas.drawPath(path, mAntiPaint);


        //绘制第二圈锯齿

        Path path1 = new Path();
        RectF rect1 = new RectF(20, 20, mRadio * 2 - 20, mRadio * 2 - 20);
        path1.addArc(rect1, 0, 360);
        Path rectPath1 = new Path();
        //第四个参数决定齿轮的每一根刺的长度
        rectPath1.addRect(0, 0, 1, 13, Path.Direction.CCW);
        //第一个参数16就是控制实体线的间距
        PathEffect pathEffect1 = new PathDashPathEffect(rectPath1, 35, 8, PathDashPathEffect.Style.ROTATE);
        mAnti2Paint.setPathEffect(pathEffect1);
//        canvas.drawPath(path1, mAnti2Paint);


        int distance = 30;
        //绘制秒格
        for (int i = 0; i <= 60; i++) {
            canvas.save();
            int drgee = i * 6;
            RectF rectF = new RectF(distance, distance, mRadio * 2 - distance, mRadio * 2 - distance);
            canvas.rotate(6, mRadio, mRadio);
            canvas.drawArc(rectF, drgee, 0.5f, false, mMinPaint);
            canvas.restore();
        }
        //绘制时格
        for (int i = 0; i <= 12; i++) {
            canvas.save();
            int drgee = i * 30;
            RectF rectF = new RectF(distance, distance, mRadio * 2 - distance, mRadio * 2 - distance);
            canvas.rotate(30, mRadio, mRadio);
            canvas.drawArc(rectF, drgee, 0.8f, false, mHourPaint);
            canvas.restore();
        }
        //绘制数字
        for (int i = 1; i <= 12; i++) {
            float dis = segment * i;
            measure.getPosTan(dis,positoin,rightTan);
            //这里x和y坐标需要设置12个单位的偏移，不然会有偏差。
            canvas.drawText(i + "", (int)positoin[0]-12,(int)positoin[1]+12, mTextPaint);
        }
        canvas.restore();

        //绘制时针
        drawHourPoint(canvas);
        //绘制分针
        drawMinPoint(canvas);
        //绘制秒针
        drawSecPoint(canvas);
        //绘制一个圆心
        canvas.drawCircle(mRadio,mRadio,6,mSecondPointPaint);
    }

    /**
     * 绘制时针
     * @param canvas
     */
    private void drawHourPoint(Canvas canvas){
        canvas.save();
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        float drgee = (float) (30*hour+min*0.5);
        canvas.rotate(drgee,mRadio,mRadio);
        canvas.drawLine(mRadio,mRadio,mRadio,120,mHourPointPaint);
        canvas.restore();
    }

    /**
     * 绘制分针
     * @param canvas
     */
    private void drawMinPoint(Canvas canvas){
        canvas.save();
        Calendar cal = Calendar.getInstance();
        int min = cal.get(Calendar.MINUTE);
        float drgee = (float) (6*min);
        canvas.rotate(drgee,mRadio,mRadio);
        canvas.drawLine(mRadio,mRadio,mRadio,60,mMinPointPaint);
        canvas.restore();
    }

    /**
     * 绘制秒针
     */
    private void drawSecPoint(Canvas canvas){
        canvas.save();
        Calendar cal = Calendar.getInstance();
        int sec = cal.get(Calendar.SECOND);
        float drgee = (float) (6*sec);
        canvas.rotate(drgee,mRadio,mRadio);
        canvas.drawLine(mRadio,mRadio+28,mRadio,30,mSecondPointPaint);
        canvas.restore();
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
            int desired = (int) (getPaddingLeft() + getPaddingRight()) + mRadio * 2 + 2;
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + getPaddingBottom()) + +mRadio * 2 + 2;
            height = desired;
        }
        setMeasuredDimension(width, height);
    }


}
