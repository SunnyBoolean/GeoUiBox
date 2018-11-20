package com.geo.uibox;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.geo.bean.LDataBean;
import com.geo.widget.AlipayView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwei on 2018/10/17.
 * 可以参考博客实现：【https://www.jianshu.com/p/b76572fb4e60】
 */

public class MuiltListViewActivity extends BaseActivity{
    ListView mListView;
    private List<LDataBean> datas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muilt_listview);
        mListView = (ListView) findViewById(R.id.muilt_listview);
    }





    @Override
    protected void initToolBar(boolean isShowBackArrow) {
        super.initToolBar(isShowBackArrow);
        mToolBar.setTitle("多级列表");
    }
}
