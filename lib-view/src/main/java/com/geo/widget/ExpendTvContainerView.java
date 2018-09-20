package com.geo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geostar.liwei.lib_view.R;


/**
 * Created by liwei on 2018/3/28.
 */
public class ExpendTvContainerView extends LinearLayout {
    TextView textView;
    private int mDefaultLines = 2;//默认显示2行文字

    public ExpendTvContainerView(Context context) {
        super(context);
    }

    public ExpendTvContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ExpendTvContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(VERTICAL);
        float textSize = 16;
        int textColor = Color.BLACK;
        float lineSpace = 3;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.ExpendTvContainerView, defStyleAttr, 0);
            textSize = a.getDimension(R.styleable.ExpendTvContainerView_textSize, 16);
            textColor = a.getColor(R.styleable.ExpendTvContainerView_textColor, Color.BLACK);
            lineSpace = a.getDimension(R.styleable.ExpendTvContainerView_lineSpacingExtra, 3);
        }

        textView = new TextView(context);
        TextPaint paint = textView.getPaint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        textView.setLineSpacing(lineSpace, 1);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        lp.bottomMargin = 10;
        textView.setLines(mDefaultLines);
        textView.setLayoutParams(lp);
        addView(textView);
        genExTv();
        addView(expendBtn);
    }

    private TextView expendBtn;

    private boolean isExpand = false;//false表示收缩，true表示伸开

    /**
     * 设置文本内容的时候开始计算显示内容的高度
     *
     * @param content
     */
    public void setText(String content) {
        textView.setText(content);
        invalidate();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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
        width = widthSize;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            Log.e("EXT", "getLineCount=" + textView.getLineCount());
            int lines = textView.getLineCount();
            int desired = 0;
            if (mDefaultLines < lines && !isAnimatoin) {   //这里如果实际文本大于3行则只显示3行的内容，则只显示3行文本的内容，然后就点击展开显示。如果实际文本小于3行则直接显示实际文本的高度即可
                desired = mDefaultLines * textView.getLineHeight() + expendBtn.getLineHeight() + 50;
            } else {
                if (isAnimatoin) {
                    if (isExpand) {//展开
                        desired = lines * textView.getLineHeight() + expendBtn.getLineHeight() + 50;
                    } else {//收起
                        desired = mDefaultLines * textView.getLineHeight() + expendBtn.getLineHeight() + 50;
                    }
                } else {
                    desired = lines * textView.getLineHeight() + 20;
                }
            }
            if (mDefaultLines < lines) {
                expendBtn.setVisibility(View.VISIBLE);
            } else {
                expendBtn.setVisibility(View.GONE);
            }
            height = desired;
        }
        setMeasuredDimension(width, height);
    }

    private TextView genExTv() {
        expendBtn = new TextView(getContext());
        expendBtn.setText("展开");
        expendBtn.setTextColor(Color.parseColor("#2d8a7e"));
        LayoutParams lp = new LayoutParams(520, 120);
        lp.topMargin = 0;
        lp.bottomMargin = 0;
        expendBtn.setPadding(0, 5, 40, 5);//这里最好设置一下padding值，更容易点击到，不然用户半天没有点击到体验不好
        expendBtn.setLayoutParams(lp);
        //点击展开所有
        expendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                if (isExpand) {
                    expendBtn.setText("收起");
                } else {
                    expendBtn.setText("展开");
                }
                animaHeight();
            }
        });
        return expendBtn;
    }

    float readHeight;
    private boolean isAnimatoin = false;

    private void animaHeight() {
        float distance = 0;
        if (isExpand) {//如果是收缩状态
            distance = (textView.getLineCount() - mDefaultLines + 1) * textView.getLineHeight();
            textView.setLines(textView.getLineCount());
        } else {//
            distance = -(textView.getLineCount() - mDefaultLines + 1) * textView.getLineHeight();
            textView.setLines(mDefaultLines);
        }
        isAnimatoin = true;
        //开始平移的高度
        ValueAnimator animator = ValueAnimator.ofFloat(0, distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                readHeight = (float) valueAnimator.getAnimatedValue();
                Log.e("EXT", readHeight + "");
                ViewGroup.LayoutParams lp = getLayoutParams();
            }
        });
        animator.setDuration(3000);
        animator.start();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
