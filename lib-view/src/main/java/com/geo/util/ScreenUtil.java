package com.geo.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by liwei on 2018/3/26.
 */

public class ScreenUtil {
    public static int getScreenHeight(Context var0) {
        return var0.getResources().getDisplayMetrics().heightPixels;
    }
    public static int getScreenWidth(Context var0) {
        return var0.getResources().getDisplayMetrics().widthPixels;
    }
    //状态栏高度
    public static double getStatusBarHeight(Context context){
        double statusBarHeight = Math.ceil(25 * context.getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }
}
