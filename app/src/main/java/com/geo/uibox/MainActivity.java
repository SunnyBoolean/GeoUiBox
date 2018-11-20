package com.geo.uibox;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity {
    Button mShaderLineBtn, mAliPayBtn, mThumbBtn, mHwBtn, mRadarBtn, mExtendBtn, mClockBtn, mFanBtn, mWaveBtn, mSzBtn, mAutionWave;
    Button mBseBtn, mMoreListView, mLoginView, mYbpView;

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
        mRadarBtn = (Button) findViewById(R.id.main_text_rand_btn);
        mExtendBtn = (Button) findViewById(R.id.main_text_extend_btn);
        mBseBtn = (Button) findViewById(R.id.main_text_bse_btn);
        mClockBtn = (Button) findViewById(R.id.main_text_clock_btn);
        mFanBtn = (Button) findViewById(R.id.main_text_fan_btn);
        mWaveBtn = (Button) findViewById(R.id.main_text_wave_btn);
        mSzBtn = (Button) findViewById(R.id.main_text_clock_sz_btn);
        mAutionWave = (Button) findViewById(R.id.main_text_audionwave_btn);
        mMoreListView = (Button) findViewById(R.id.main_text_listview_btn);
        mLoginView = (Button) findViewById(R.id.main_text_login_btn);
        mYbpView = (Button) findViewById(R.id.main_text_ybp_btn);
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
        mMoreListView.setOnClickListener(this);
        mLoginView.setOnClickListener(this);
        mYbpView.setOnClickListener(this);

        mAutionWave.setText(Html.fromHtml("测试"));
    }

    private void dosth() {
        String str = getString(R.string.as);

        JSONObject json = null;
        try {
//            Object objs=JSONObject.parse();
            String newStr = JSONObject.quote(str);
            json = new JSONObject(newStr);
            JSONArray obj = json.getJSONArray("data");
            String uid = obj.getJSONObject(0).getString("UID");
            String sj = obj.getJSONObject(0).getString("sj");
            String xm = obj.getJSONObject(0).getString("xm");
            JSONArray array = obj.getJSONObject(0).getJSONArray("roles");
            String[] roles = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
//                String res = array.getString(i);
                JSONObject json1 = array.getJSONObject(i);
//                String ad = json1.getString("roleMark");

                JSONObject obj1 = json1.getJSONObject("roleMark");
                JSONArray array1 = obj1.getJSONArray("gwjs");
                for (int j = 0; j < array1.length(); j++) {
//                    String st = array1.getString(i);
                    JSONObject jb = array1.getJSONObject(j);
                    String id = jb.getString("gwid");
                    String name = jb.getString("desc");
                    Log.e("asxsa", id + "  " + name);
                }
//                roles[i] = res;
            }
            Log.e("asxsa", uid + "  " + sj + "  " + xm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            case R.id.main_text_listview_btn://多级菜单
                intent.setClass(mContext, MuiltListViewActivity.class);
                break;
            case R.id.main_text_login_btn: {//登录效果
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "登录按钮效果");
                intent.putExtra("type", 9);
                break;
            }
            case R.id.main_text_ybp_btn: {//仪表盘
                intent.setClass(mContext, CommonActivity.class);
                intent.putExtra("title", "温度仪表盘");
                intent.putExtra("type", 10);
                break;
            }
        }
//        dosth();
        startActivity(intent);
    }

    @Override
    public void initToolBar(boolean isArrow) {
        super.initToolBar(false);
        if (mToolBar != null)
            mToolBar.setTitle("UIBox");
    }
}
