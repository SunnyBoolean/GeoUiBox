package com.geo.uibox;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.geo.widget.ClockView;
import com.geo.widget.ExpendTvContainerView;
import com.geo.widget.FelloMeView;
import com.geo.widget.ProgressViewV2View;
import com.geo.widget.WeiBoThumbView;

/**
 * Created by liwei on 2018/9/18.
 */

public class CommonActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra("type", 0);
        initView(type);
    }

    private void initView(int type) {
        switch (type) {
            case 0: { //点赞和关注！
                setContentView(R.layout.thumb_fellow_layout);
                //点赞
                final WeiBoThumbView thumbView = (WeiBoThumbView) findViewById(R.id.weibo_thumb);
                thumbView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(thumbView.isThumb()){
                            thumbView.unThumb();
                        }else{
                            thumbView.thumb();
                        }

                    }
                });
                //关注
                final FelloMeView felloMeView = (FelloMeView) findViewById(R.id.fello_me_view);
                felloMeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(felloMeView.isFocused()){
                            felloMeView.unfocused();
                        }else{
                            felloMeView.focused();
                        }
                    }
                });
                break;
            }
            case 1: {//华为控件
                    setContentView(R.layout.huawei_layout);
                ProgressViewV2View progress = (ProgressViewV2View) findViewById(R.id.progress_v2);
                progress.setValue(76);
                break;
            }
            case 2:{//雷达
                setContentView(R.layout.radaw_layout);
            }
            case 3:{
                setContentView(R.layout.expend_textview_layout);
                ExpendTvContainerView mExpendTextView = (ExpendTvContainerView) findViewById(R.id.expend_container);
                String content="该产品利用MQTT把病人的即时更新信息传给医生/医院，然后医院进行保存。这样的话，病人就不用亲自去医院检查心脏仪器了，医生可以随时查看病人的数据，给出建议，病人在家里就可以自行检查。产品利用MQTT把病人的即时更新信息传给医生/医院，然后医院进行保存。这样的话，病人就不用亲自去医院检查心脏仪器了，医生可以随时查看病人的数据，给出建议，病人在家里就可以自行检查。";
                mExpendTextView.setText(content);
                break;
            }
            case 4:
                setContentView(R.layout.bezier_path_layout);
                break;
            case 5:{ //秒表计时器
                setContentView(R.layout.clock_view);
                final ClockView clockView = (ClockView) findViewById(R.id.clock_view);
                Button start = (Button) findViewById(R.id.clock_start);
                Button pause = (Button) findViewById(R.id.clock_pause);
                Button end = (Button) findViewById(R.id.clock_end);
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clockView.start();
                    }
                });
                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clockView.pause();
                    }
                });
                end.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clockView.pause();
                    }
                });
                break;
            }
            case 6:{//发电机风扇
                setContentView(R.layout.fan_layout);
            }

        }
    }

    @Override
    protected void initToolBar(boolean isShowBackArrow) {
        super.initToolBar(isShowBackArrow);
        String title = getIntent().getStringExtra("title");
        mToolBar.setTitle(title + "");
    }
}
