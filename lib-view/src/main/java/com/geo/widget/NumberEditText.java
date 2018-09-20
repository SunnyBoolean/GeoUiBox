package com.geo.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.geostar.liwei.lib_view.R;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by liwei on 2018/9/7.
 */

public class NumberEditText extends LinearLayout {
    Paint mHightLinePaint;  //高亮画笔
    Paint mGreyPaint;    //灰色画笔
    int hithLightColor = Color.GREEN;//高亮颜色
    int normalColor = Color.GRAY;//正常颜色
    int numTotal = 6;//密码位数，默认6位
    int lineHeight = 20;//下划线的高度，默认20
    private int itemWidth;//每一个输入框的宽度
    private HashMap<Integer, InnerEditText> mEditMap = new HashMap<>();

    public NumberEditText(Context context) {
        super(context);
    }

    public NumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.NumberEditText, defStyleAttr, 0);
            lineHeight = (int) a.getDimension(R.styleable.NumberEditText_et_line_height, 6);
            hithLightColor = a.getColor(R.styleable.NumberEditText_et_highlinght_color, Color.GREEN);
            normalColor = a.getColor(R.styleable.NumberEditText_et_normal_color, Color.GRAY);
            numTotal = a.getInteger(R.styleable.NumberEditText_et_num_total, 20);
        }
        mEditMap.clear();
        //水平显示
        setOrientation(HORIZONTAL);
        mHightLinePaint = new Paint();
        mHightLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mHightLinePaint.setColor(Color.GREEN);
        mHightLinePaint.setStrokeWidth(30);

        mGreyPaint = new Paint();
        mGreyPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mGreyPaint.setColor(Color.GRAY);
        mGreyPaint.setStrokeWidth(30);

        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                itemWidth = (getWidth() - numTotal * 10) / numTotal;
                addEditText();
            }
        });

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = getWidth();


        Log.e("NumEt", "onSizeChanged()宽度：" + width);
    }

    /**
     * 添加输入框
     */
    private void addEditText() {
        for (int i = 0; i < numTotal; i++) {
            InnerEditText editText = genEditText(i);
            mEditMap.put(i, editText);
            addView(editText);
        }
        invalidate();
    }

    /**
     * 在这里创建EditText
     *
     * @param index
     */
    private InnerEditText genEditText(final int index) {
       final InnerEditText editText = new InnerEditText(getContext());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (!TextUtils.isEmpty(str)) {
                    editText.paint = mHightLinePaint;
//                    invalidate();
                    int tag = (int) editText.getTag();
                    if(tag<numTotal-1){//不是最后一个
                        final InnerEditText editText=mEditMap.get(tag+1);
                        if(str.equals(editText.getText())){
                            return;
                        }
                        editText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                editText.setText("2");
                                editText.setCursorVisible(true);
                                editText.setFocusable(true);
                                editText.setFocusableInTouchMode(true);
                                editText.requestFocus();//让editText2获取焦点
                                editText.setSelection(editText.getText().length());
                            }
                        },200);

                        }else if(tag==numTotal-1){  //是最后一个

                    }
                } else {
                    editText.paint = mGreyPaint;
                }
            }
        });
//        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // 这里是关键代码了
//                    editText.setTextIsSelectable(true);
//                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
////                    inputMethodManager.showSoftInput(editText,0);
////                    PhoneUtils.showKeyBoard(SendAlbumActivity.this,editText);
//                }
//            }
//        });
        editText.setTag(index);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        editText.setLayoutParams(lp);
        return editText;
    }

    private class InnerEditText extends AppCompatEditText {
        public Paint paint;

        public InnerEditText(Context context) {
            super(context);
            init();
        }

        public InnerEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public InnerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            paint = mGreyPaint;
            //不要背景下划线
            setBackground(null);
            //设置只允许填写数字
            setInputType(InputType.TYPE_CLASS_NUMBER);
            //单行显示，不允许空格换行
            setLines(1);

            //最大输入长度
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            //设置为密码输入框
            setTransformationMethod(PasswordTransformationMethod.getInstance());
            //内容居中
            setGravity(Gravity.CENTER);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int scrollx = getScrollX();
            int scrollY = getScrollY();
            int tag = (int) getTag();
            int starX = +scrollx;
            int stopX = itemWidth + scrollx;
            canvas.drawLine(starX, getHeight() + scrollY, stopX, getHeight() + scrollY, paint);
        }

    }
}
