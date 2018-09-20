package com.geo.widget.test;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.geostar.liwei.lib_view.R;

/**
 * Created by liwei on 2017/6/3.
 */

public class PaintShaderView extends View{
    int mPosition = 500;
    Bitmap mBitmap;
    BitmapShader mShader;
    public PaintShaderView(Context context) {
        super(context);
    }

    public PaintShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimation();
    }

    public PaintShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimation();
    }

    private void initAnimation() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
        mShader=new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        final ValueAnimator animator = ValueAnimator.ofInt(0,1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int positoin= (int) valueAnimator.getAnimatedValue();
                mPosition = positoin;
                invalidate();
            }
        });

        animator.setRepeatCount(-1);//-1无限重复,这里只执行3次
        animator.setDuration(2000);
        animator.start();
    }
    /**
     * BitmapShader的用法
     * @param canvas
     */
    private void drawBitmapWithBitmapShader(Canvas canvas){
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Paint paint = new Paint();

        //使用Materix进行图片缩放
        Matrix matrix = new Matrix();
        float scale = 200/mBitmap.getHeight();
        matrix.setScale(0.9f,0.9f);
//        shader.setLocalMatrix(matrix);
        paint.setShader(mShader);
        canvas.drawCircle(200,200,200,paint);
    }

    /**
     * 线性渐变的使用
     * @param canvas
     */
    private void drawLinearGradint(Canvas canvas){
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //创建线性渲染对象
        int mColorLinear[] = {Color.BLACK,Color.WHITE,Color.BLACK};
        //控制前四个参数就可以控制渐变渲染的方向
        LinearGradient gradlint = new LinearGradient(mPosition/6, mPosition/6, mPosition, mPosition/6, mColorLinear, null,
             Shader.TileMode.CLAMP);
        paint.setShader(gradlint);
        paint.setTextSize(80);
        canvas.drawText("这不是一个跑马灯啊",30,500,paint);
    }

    /**
     * 复合渲染
     * @param canvas
     */
    private void drawComposeShader(Canvas canvas){
//        Paint paint = getPaint();
//        ComposeShader composeShader = new ComposeShader(mLinearGradient, mRadialGradient,
//              PorterDuff.Mode.DARKEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //使用BitmapShader
//        drawBitmapWithBitmapShader(canvas);
        //使用Linear Gradient；
        drawLinearGradint(canvas);
    }
    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        PathEffect as;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        return paint;
    }

    private int getColor(int id) {
        return getResources().getColor(id);
    }
}
