package com.geo.uibox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.geo.widget.AudionWaveView;

public class MainActivity extends BaseActivity {
    Button mShaderLineBtn, mAliPayBtn, mThumbBtn,mHwBtn,mRadarBtn,mExtendBtn,mClockBtn,mFanBtn,mWaveBtn,mSzBtn,mAutionWave;
    Button mBseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mShaderLineBtn = (Button) findViewById(R.id.main_text_shader_btn);
        mAliPayBtn = (Button) findViewById(R.id.main_text_alipay_btn);
        mThumbBtn = (Button) findViewById(R.id.main_text_thumb_btn);
        mHwBtn = (Button) findViewById(R.id.main_text_huawei_btn);
        mRadarBtn =(Button) findViewById(R.id.main_text_rand_btn);
        mExtendBtn = (Button)findViewById(R.id.main_text_extend_btn);
        mBseBtn = (Button)findViewById(R.id.main_text_bse_btn);
        mClockBtn = (Button) findViewById(R.id.main_text_clock_btn);
        mFanBtn = (Button) findViewById(R.id.main_text_fan_btn);
        mWaveBtn = (Button)findViewById(R.id.main_text_wave_btn);
        mSzBtn = (Button)findViewById(R.id.main_text_clock_sz_btn);
        mAutionWave = (Button)findViewById(R.id.main_text_audionwave_btn);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mShaderLineBtn.setOnClickListener(this);
        mAliPayBtn.setOnClickListener(this);
        mThumbBtn.setOnClickListener(this);
        mHwBtn.setOnClickListener(this);
        mRadarBtn.setOnClickListener(this);
        mExtendBtn.setOnClickListener(this);
        mBseBtn.setOnClickListener(this);
        mClockBtn.setOnClickListener(this);
        mFanBtn.setOnClickListener(this);
        mWaveBtn.setOnClickListener(this);
        mSzBtn.setOnClickListener(this);
        mAutionWave.setOnClickListener(this);
    }

    @Override
    public void handlOnClickListener(View v) {
        super.handlOnClickListener(v);
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.main_text_shader_btn://内容逐渐显示效果
                intent.setClass(mContext, TinkViewActivity.class);
                break;
            case R.id.main_text_alipay_btn://支付效果
                intent.setClass(mContext, AliPayActivity.class);
                break;
            case R.id.main_text_thumb_btn://点赞和关注效果
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "点赞和关注");
                intent.putExtra("type", 0);
                break;
            case R.id.main_text_huawei_btn://华为
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "华为控件");
                intent.putExtra("type", 1);
                break;
            case R.id.main_text_rand_btn://雷达
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "雷达");
                intent.putExtra("type", 2);
                break;
            case R.id.main_text_extend_btn://可收缩展开Tv、、
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "收起展开Textview");
                intent.putExtra("type", 3);
                break;
            case R.id.main_text_bse_btn:
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "贝塞尔曲线");
                intent.putExtra("type", 4);
                break;
            case R.id.main_text_clock_btn://秒表计时器
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "秒表计时器");
                intent.putExtra("type", 5);
                break;
            case R.id.main_text_fan_btn://风力发电风扇
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "天气---风扇");
                intent.putExtra("type", 6);
                break;
            case R.id.main_text_wave_btn:
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "波浪");
                intent.putExtra("type", 7);
                break;
            case R.id.main_text_clock_sz_btn://时钟
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "时钟");
                intent.putExtra("type", 8);
                break;
            case R.id.main_text_audionwave_btn://录音波浪
                intent.setClass(mContext, AudionWaveActivity.class);
                break;

        }
        startActivity(intent);
    }

    @Override
    public void initToolBar(boolean isArrow) {
        super.initToolBar(false);
        if (mToolBar != null)
            mToolBar.setTitle("UIBox");
    }
}
