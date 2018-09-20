package com.geo.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.geostar.liwei.lib_view.R;


/**
 * Created by liwei on 2018/4/3.
 * 提示
 */

public class ToastUtil {
    View containerView;
    private TextView textView;
    Context mContext;

    public ToastUtil(Context context) {
        mContext = context;
        containerView = View.inflate(context, R.layout.toast_layout, null);
        textView = (TextView) containerView.findViewById(R.id.toast_text);
    }

    /**
     * 完成
     *
     * @param text
     */
    public void toastDone(String text) {
        Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        textView.setText(text);
        setTextDrawable(R.drawable.icon_toast_done);
        toast.setView(containerView);
        toast.show();
    }

    public void toastFailed(String text) {
        Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        setTextDrawable(R.drawable.icon_toast_failed);
        textView.setText(text);
        toast.setView(containerView);
        toast.show();
    }


    private void setTextDrawable(int sourceId) {
        Drawable drawable = mContext.getResources().getDrawable(sourceId);
/**这一步必须要做,否则不会显示.*/
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//对图片进行压缩
        /**设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片*/
        textView.setCompoundDrawables(null, drawable, null, null);
    }
}
