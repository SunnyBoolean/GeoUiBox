package com.geo.uibox;

import android.os.Bundle;

import com.geo.widget.TinkView;

import java.util.ArrayList;

/**
 * Created by liwei on 2017/6/3.
 */
public class TinkViewActivity extends BaseActivity{
    TinkView mTinkView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tink_layout);
        mTinkView = (TinkView) findViewById(R.id.tink_view);

        ArrayList<String> datas = new ArrayList<>();
        datas.add("临别，赠你一首诗吧，");
        datas.add("世界之大，一期一遇。");
        datas.add("尚未佩妥剑，");
        datas.add("转眼便江湖，");
        datas.add("愿历尽千帆，");
        datas.add("归来仍少年。");
        mTinkView.setText(datas);

    }

    @Override
    protected void initToolBar(boolean isShowBackArrow) {
        super.initToolBar(isShowBackArrow);
        mToolBar.setTitle("内容逐渐显示");
    }
}
