package com.geo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.geostar.liwei.lib_view.R;

import java.util.List;

/**
 * Created by liwei on 2018/3/30.
 * 仿淘宝上下滚动Banner
 */

public class TaoBaoBannerView extends LinearLayout {
    private View mView;
    private List<BannerData> mDatas;
    ViewFlipper mViewFlipper;

    public TaoBaoBannerView(Context context) {
        super(context);
        init();
    }

    public TaoBaoBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaoBaoBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        mView = View.inflate(getContext(), R.layout.taobao_banner_item, null);
        mViewFlipper = (ViewFlipper) mView.findViewById(R.id.taobao_banner_viewflipper);
        addView(mView);
    }

    public void setDatas(List<BannerData> datas) {
        this.mDatas = datas;
        for (int i = 0; i < datas.size(); i++) {
            View view = View.inflate(getContext(), R.layout.item_taobao_banner_tv, null);
            TextView tv = (TextView) view.findViewById(R.id.item_banner_tv);
            TextView tv1 = (TextView) view.findViewById(R.id.item_banner_tv_a);

            TextView lable1 = (TextView) view.findViewById(R.id.taobao_banner_label_1);
            TextView lable2 = (TextView) view.findViewById(R.id.taobao_banner_label_2);
            BannerData data = datas.get(Math.abs(i-1));
            BannerData data1 = datas.get(i);
            tv1.setText(data1.title);
            tv.setText(data.title);
            lable1.setText(data.label);
            lable2.setText(data1.label);
            mViewFlipper.addView(view);
        }
        invalidate();
    }

    private void startAnimaBanner() {

    }

    /**
     * 滚动栏的数据结构
     */
    public static class BannerData {
        public String label;//标签
        public String title;//标题内容
        public int resId;//资源id，可以定义成网络图片地址

        public BannerData(String label, String title) {
            this.label = label;
            this.title = title;
        }

        public BannerData() {
        }
    }
}
