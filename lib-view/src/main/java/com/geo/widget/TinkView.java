package com.geo.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geostar.liwei.lib_view.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by liwei on 2017/6/3.
 * 内容逐渐显示
 */

public class TinkView extends LinearLayout {
    public TinkView(Context context) {
        super(context);
        init(null, 0);
    }

    public TinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TinkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    float textSize = 16;
    int textColor = Color.BLACK;
    int lineMargin;

    private void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.TinkView, defStyleAttr, 0);
            textSize = a.getDimension(R.styleable.TinkView_text_size, 16);
            textColor = a.getColor(R.styleable.TinkView_text_color, Color.BLACK);
            lineMargin = (int) a.getDimension(R.styleable.TinkView_line_margin, 10);

            a.recycle();
        }


        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void setText(ArrayList<String> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        Random random = new Random();
        for (int i = 0; i < datas.size(); i++) {
            TextView textView = new TextView(getContext());
            TextPaint paint = textView.getPaint();
            paint.setTextSize(textSize);
            paint.setColor(textColor);
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = lineMargin;
            //生成[0,80]的随机数，不让文本对齐
            lp.leftMargin = random.nextInt(80);
            textView.setLines(1);
            textView.setLayoutParams(lp);
            textView.setTextColor(textColor);
            textView.setText(datas.get(i));
            textView.setAlpha(0);
            addView(textView);

            ObjectAnimator anim = ObjectAnimator.ofFloat(textView, "alpha", 0, 1f);
            anim.setDuration(1500);
            anim.setStartDelay(i * 1500);
            anim.start();
        }
        invalidate();
    }
}
