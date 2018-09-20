package com.geo.uibox;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;



/**
 * Created by 李伟 on 2017/05/16.
 * 所有Activity的基类
 * 这里封装好了Toolbar，所有基类可以直接拿出来用
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public Context mContext;
    private View mContent;
    public Toolbar mToolBar;
    public final String LOG_TAG="ATM";
    /**
     * 是否显示Toolbar
     */
    private boolean mIsAddToolbar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    final public void setContentView(int layoutResID) {
        //如果不需要添加Toolbar就直接调用父类方法完成布局填充
        if (!mIsAddToolbar) {
            super.setContentView(layoutResID);
            mContent = View.inflate(this, layoutResID, null);
            return;
        }

        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        LinearLayout parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parentLinearLayout);
        //首先将Toolbar添加的界面
        ViewGroup toolBarRoot = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.tool_bar, parentLinearLayout, true);
        //然后将内容界面添加到以Toolbar为根布局的界面
        View content = View.inflate(this, layoutResID, toolBarRoot);
        mContent = content;
        mToolBar = (Toolbar) toolBarRoot.findViewById(R.id.toolbar);
        super.setContentView(content);
        initToolBar(true);
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * @param layoutResID  布局文件
     * @param isAddToolbar 是否添加toolbar，false表示不显示toolbar，true显示
     */
    final public void setContentView(int layoutResID, boolean isAddToolbar) {
        mIsAddToolbar = isAddToolbar;
        setContentView(layoutResID);
    }

    public void showToast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        initListener();
    }


    protected void initListener() {
    }


    /**
     * 所有的单击事件在这里处理
     *
     * @param v
     */
    public void handlOnClickListener(View v) {

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        handlOnClickListener(v);
    }

    /**
     * ToolBar初始化，及其操作
     *
     * @param isShowBackArrow 是否显示返回箭头，true就显示，false就不显示，默认是显示的
     */
    protected void initToolBar(boolean isShowBackArrow) {
        if (mToolBar == null) {
            return;
        }
        mToolBar.setTitleTextColor(getResources().getColor(R.color.material_cyan_color_50));  //设置标题字体颜色
        if (!isShowBackArrow) {
            return;
        }
        mToolBar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp); //设置导航按钮，典型的就是返回箭头
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mToolBar!=null) {
            Drawable background = mToolBar.getBackground();
            if (background != null) {
                background.setAlpha(255);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

