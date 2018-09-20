package com.geo.uibox;

import android.os.Bundle;
import android.view.View;

import com.geo.widget.AlipayView;

/**
 * Created by liwei on 2017/6/22.
 */

public class AliPayActivity extends BaseActivity {
    AlipayView mAliPayview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alipay_layout);
        mAliPayview = (AlipayView) findViewById(R.id.alipay_success);
    }

    public void paySuccess(View view) {
        mAliPayview.paySuccess();
    }


    public void payFailed(View view){
        mAliPayview.payFailed();
    }
    @Override
    protected void initToolBar(boolean isShowBackArrow) {
        super.initToolBar(isShowBackArrow);
        mToolBar.setTitle("支付动画");
    }

}
