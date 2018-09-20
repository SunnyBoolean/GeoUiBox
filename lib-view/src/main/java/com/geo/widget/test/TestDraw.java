package com.geo.widget.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.geostar.liwei.lib_view.R;

/**
 * Created by liwei on 2017/5/29.
 */

public class TestDraw extends View {
    Paint mPaint;

    public TestDraw(Context context) {
        super(context);
        initPaint();
    }

    public TestDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public TestDraw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        return paint;
    }

    private int getColor(int id) {
        return getResources().getColor(id);
    }

    /**
     * 绘制一个圆
     *
     * @param canvas
     * @param isfill 是否是实心圆
     */
    private void drawCircle(Canvas canvas, boolean isfill) {
        Paint paint = getPaint();
        paint.setColor(getColor(R.color.material_cyan_color_900));
        if (isfill) {
            paint.setStyle(Paint.Style.STROKE);
        }
        paint.setStrokeWidth(10);
        paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.UNDERLINE_TEXT_FLAG|Paint.LINEAR_TEXT_FLAG);//设置删除线下划线
//        paint.setUnderlineText(true);//设置绘制下划线
//        paint.setStrikeThruText(true);//设置删除线
        canvas.drawCircle(200, 200, 100, paint);
        //用来测量文本的长度，返回文本的长度
        float length=paint.measureText("这个文字长度是多少");
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(56);
        paint.setStrokeWidth(2);
        canvas.drawText("测试",160,230,paint);
    }

    /**
     * 绘制椭圆、圆形等
     *
     * @param canvas
     */
    private void drawOval(Canvas canvas) {
        Paint paint = getPaint();
        paint.setColor(getColor(R.color.material_cyan_color_700));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //四个参数分辨是：左边距离Y轴距离（X坐标）、上边距离X轴距离（Y坐标），右边距离Y轴距离（X坐标）、下边距离X轴距离（Y坐标）
        RectF rect = new RectF(300, 10, 600, 210);
        canvas.drawOval(rect, paint);
    }

    /**
     * 绘制一条线段
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        Paint paint = getPaint();
        paint.setStrokeWidth(10);
        paint.setColor(getColor(R.color.material_cyan_color_800));
        //前四个参数分辨是：x起始坐标，y起始坐标，x终点坐标，y终点坐标
        canvas.drawLine(600, 100, 880, 200, paint);
    }

    /**
     * 绘制一个方形
     * 可以是正方形或长方形，控制边长即可
     *
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        Paint paint = getPaint();
        paint.setStrokeWidth(5);
        paint.setColor(getColor(R.color.material_cyan_color_500));
        RectF rect = new RectF(10,410,220,510);
        canvas.drawRect(rect,paint);
    }

    /**
     * 画圆弧，也可以是封闭的扇形
     * @param canvas
     */
    private void drawArc(Canvas canvas,boolean isclose){
        Paint paint = getPaint();
        paint.setStrokeWidth(3);
        RectF rect = new RectF(230,410,420,610);
        canvas.drawArc(rect,-90,120,isclose,paint);
    }

    /**
     * 绘制Bitmap
     * @param canvas
     */
    private void drawBitmap(Canvas canvas){
        Paint paint = getPaint();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_rooboot);
        //中间两个参数是左边距离Y轴距离，顶部距离X轴距离
        canvas.drawBitmap(bitmap, 430, 410, paint);
    }

    /**
     * 绘制文字，包括下划线、删除线
     * @param canvas
     */
  private void drawText(Canvas canvas){
      Paint paint = getPaint();
      //画笔宽度
      paint.setStrokeWidth(1);
      //设置字体大小
      paint.setTextSize(64);
      //填充央视
      paint.setStyle(Paint.Style.FILL_AND_STROKE);
      //设置下划线
      paint.setUnderlineText(true);
      //设置删除线
      paint.setStrikeThruText(true);
      //中间两个参数粉表示X和Y的坐标
      canvas.drawText("Hello World", 270, 350, paint);
  }

    /**
     * 画笔设置模糊效果：需要关闭硬件加速！
     * @param canvas
     */
    private void drawTextWithBlur(Canvas canvas){
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Paint paint = getPaint();
        //画笔宽度
        paint.setStrokeWidth(1);
        //设置字体大小
        paint.setTextSize(80);
        //填充样式
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        //设置模糊效果,第一个参数是模糊的半径，半径越大，模糊的范围就越大，第二个参数是模糊的样式，总共有三种样式，大家可以自己尝试下
        //NORMAL会模糊里外两边
        MaskFilter mBlur = new BlurMaskFilter(30, BlurMaskFilter.Blur.NORMAL);
        paint.setMaskFilter(mBlur);
        //中间两个参数粉表示X和Y的坐标
        canvas.drawCircle( 270, 750,40, paint);
        //INNER只会模糊里边
        paint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.INNER));
        canvas.drawCircle( 270, 850, 49,paint);
        //OUTER只会模糊外边
        paint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.OUTER));
        canvas.drawCircle( 270, 950,40, paint);
        //里边加粗，外边模糊
        paint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));
        canvas.drawCircle(270, 1050,40, paint);
    }

    /**
     * 浮雕效果:在使用此效果前要在注册文件中将Activity的硬件加速关闭
     * @param canvas
     */
    private void drawTextWithEmboss(Canvas canvas){
       //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Paint paint = getPaint();
        //画笔宽度
        paint.setStrokeWidth(3);
        //设置字体大小
        paint.setTextSize(128);
        //填充样式
        paint.setStyle(Paint.Style.STROKE);
        float[] direction = new float[] { 1, 1, 1 };
        // 设置环境光亮度
        float light = 0.1f;
        // 选择要应用的反射等级
        float specular = 8;
        // 向mask应用一定级别的模糊
        float blur = 3;
        EmbossMaskFilter emboss=new EmbossMaskFilter(direction,light,specular,blur);
        paint.setMaskFilter(emboss);
        canvas.drawText("Hello World", 270, 1250, paint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //画圆
        drawCircle(canvas, true);
        //画椭圆
        drawOval(canvas);
        //画线段
        drawLine(canvas);
        //绘制矩形
        drawRect(canvas);
        //绘制弧形
        drawArc(canvas,false);
        //绘制Bitmap
        drawBitmap(canvas);
        //绘制文字
        drawText(canvas);
        //绘制模糊效果的文字
        drawTextWithBlur(canvas);
        //浮雕效果
        drawTextWithEmboss(canvas);
    }
}
