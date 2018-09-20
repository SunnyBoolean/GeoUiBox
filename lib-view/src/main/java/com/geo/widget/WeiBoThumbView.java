package com.geo.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.geostar.liwei.lib_view.R;

/**
 * Created by liwei on 2018/3/26.
 * 仿微博点赞效果
 */

public class WeiBoThumbView extends View {
    private Bitmap mThumbbp, mThumbHigBp;
    private Paint mThumbPaint;
    private Paint mLinePaint;//射线画笔
    private int mColor;

    Path path = null;
    RectF rect = null;
    private float mLineLength = 20;
    public WeiBoThumbView(Context context) {
        super(context);
        init(context, null);
    }

    public WeiBoThumbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WeiBoThumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.WeiBoThumbView, 0, 0);
            mColor = a.getColor(R.styleable.WeiBoThumbView_light_color, Color.BLACK);
            Drawable normal = a.getDrawable(R.styleable.WeiBoThumbView_drawable_normal);
            Drawable hightLight = a.getDrawable(R.styleable.WeiBoThumbView_drawable_highlight);
            BitmapDrawable bd = (BitmapDrawable) normal;
            mThumbbp = bd.getBitmap();
            BitmapDrawable bd1 = (BitmapDrawable) hightLight;
            mThumbHigBp = bd1.getBitmap();
            a.recycle();
        }
        mThumbPaint = new Paint();
        mThumbPaint.setColor(Color.parseColor("#fc6472"));
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setStrokeCap(Paint.Cap.ROUND);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(mColor);
        mLinePaint.setStrokeWidth(10);

        path = new Path();
        rect = new RectF(0, 0, mThumbbp.getWidth(), mThumbbp.getWidth());
        path.addArc(rect, 160, 220);
    }

    private boolean isStartAnima = false, isThumb = false;

    public void thumb() {
        isThumb = true;
        invalidate();
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 1.5f, 1.5f, 1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 1.5f, 1.5f, 1f);
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "rotation", 15f, 0, -15f, 0);
        scaleYAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animLine();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleXAnim, anim, scaleYAnim);
        set.setDuration(300);
        set.start();
    }

    public void unThumb() {
        isThumb = false;
        isStartAnima = false;
        invalidate();
    }

    public boolean isThumb() {
        return isThumb;
    }
    private void animLine() {
        isStartAnima = true;
        ValueAnimator animator = ValueAnimator.ofFloat(20, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLineLength = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(500);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isThumb) {  //绘制已点赞的图片
            canvas.drawBitmap(mThumbHigBp, 0, mThumbbp.getWidth() / 2, mThumbPaint);
        } else {  //绘制未点在图标
            canvas.drawBitmap(mThumbbp, 0, mThumbbp.getWidth() / 2, mThumbPaint);
        }
        //缩放抖动之后菜绘制射线
        if (isStartAnima) {
            //绘制射线
            Path rectPath = new Path();

            rectPath.addRect(0, 0, 5, mLineLength, Path.Direction.CCW);
            //第一个参数16就是控制实体线的间距
            PathEffect pathEffect1 = new PathDashPathEffect(rectPath, 35, 30, PathDashPathEffect.Style.ROTATE);
            mLinePaint.setPathEffect(pathEffect1);

            canvas.drawPath(path, mLinePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int desired = mThumbbp.getWidth();
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = mThumbbp.getHeight();
            height = 2 * desired;
        }

        setMeasuredDimension(width, height);
    }

}
