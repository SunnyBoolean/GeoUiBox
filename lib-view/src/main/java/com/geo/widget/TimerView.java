package com.geo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.geostar.liwei.lib_view.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liwei on 2018/9/20.
 * 秒表计时器
 * 重点就是扇形渐变SweepGradient、PathEffect、利用矩阵使画布不停旋转
 */

public class TimerView extends View {
    public TimerView(Context context) {
        super(context);
        init();
    }

    public TimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int mRadio = 300;//最外圈的半径
    private Paint mOutPaint;  //最外层画笔
    private Paint mOut2Paint;//倒数第二层画笔
    private Paint mAntiPaint;//第一圈锯齿
    private Paint mAnti2Paint;//第二圈锯齿
    private Paint mArcPaint;//扫描画笔
    private Paint mArc2Paint;//扫描头部线段画笔
    private Paint mTextPaint;//文字画笔


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

        mArcPaint = new Paint();
        mArcPaint.setStrokeWidth(35);
        mArcPaint.setColor(getContext().getColor(R.color.material_cyan_color_100));
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);

        mArc2Paint = new Paint();
        mArc2Paint.setStrokeWidth(35);
        mArc2Paint.setColor(getContext().getColor(R.color.material_cyan_color_600));
        mArc2Paint.setAntiAlias(true);
        mArc2Paint.setStyle(Paint.Style.STROKE);


        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(getContext().getColor(R.color.material_grey_color_600));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextSize(70);
        matrix = new Matrix();
    }

    int[] SWEEP_GRADIENT_COLORS = new int[]{Color.TRANSPARENT, getContext().getColor(R.color.material_cyan_color_100)};
    private int distance = 80;
    float drgee;
    ValueAnimator animator;

    private void initAnima() {
        animator = ValueAnimator.ofFloat(0, 360);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drgee = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setRepeatCount(-1);
        animator.setDuration(5000);
        animator.start();
    }

    boolean isStart = false;
    private int min = 0;
    private int sec = 0;
    Timer timer;
    Calendar calendar;
    String str_min="00";
    String str_sec="00";
    String str_minsec="00";

    public void start() {

        isStart = true;
        initAnima();
        timer = new Timer();
        calendar = Calendar.getInstance();
        final int min = calendar.get(Calendar.MINUTE);
        final int sec = calendar.get(Calendar.SECOND);
        final int minsec = calendar.get(Calendar.MILLISECOND);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                int temp_min = cal.get(Calendar.MINUTE)-min;
                int temp_sec = cal.get(Calendar.SECOND)-sec;
                int temp_minsec = cal.get(Calendar.MILLISECOND)/10;
                str_min=Math.abs(temp_min)<10?"0"+Math.abs(temp_min):""+Math.abs(temp_min);
                str_sec=Math.abs(temp_sec)<10?"0"+Math.abs(temp_sec):""+Math.abs(temp_sec);
                str_minsec=Math.abs(temp_minsec)<10?"0"+Math.abs(temp_minsec):""+Math.abs(temp_minsec);
            }
        }, 0, 10);

    }

    public void pause() {
        isStart = false;
        animator.cancel();
        animator.end();
        if(timer!=null){
            timer.cancel();
        }
    }

    Matrix matrix;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
        canvas.drawPath(path1, mAnti2Paint);

        canvas.save();
        canvas.rotate(-90, mRadio, mRadio);
        canvas.concat(matrix);

        //绘制扫描
        //添加渐变:
        SweepGradient gradient = new SweepGradient(mRadio, mRadio, SWEEP_GRADIENT_COLORS, new float[]{0.2f, 1f});  //
        RectF rect2 = new RectF(distance, distance, mRadio * 2 - distance, mRadio * 2 - distance);
        Path path2 = new Path();
        path2.addArc(rect2, 0, 360);
        mArcPaint.setShader(gradient);
        canvas.drawPath(path2, mArcPaint);

        Path path3 = new Path();
        RectF rect3 = new RectF(distance, distance, mRadio * 2 - distance, mRadio * 2 - distance);
        path3.addArc(rect3, 0, 2);
        canvas.drawPath(path3, mArc2Paint);
        matrix.postRotate(1, mRadio, mRadio);
        canvas.restore();

        //最后一步，绘制文字
        String text = str_min+":"+str_sec+":"+str_minsec;
        float theight = mTextPaint.measureText(text);
        canvas.drawText(text, mRadio - theight / 2, mRadio + 20, mTextPaint);
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
