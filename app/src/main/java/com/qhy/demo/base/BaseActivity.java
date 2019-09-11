package com.qhy.demo.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.qhy.demo.R;
import com.qhy.demo.app.AppManager;
import com.qhy.demo.view.CheckPermissionsActivity;
import com.qhy.demo.widget.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by qhy on 2019/9/9.
 */
public abstract class BaseActivity extends CheckPermissionsActivity {

    protected final String TAG = getClass().getName();


    private LinearLayout parentLinearLayout;
    private LoadingDialog loadingDlg;


    @BindView(R.id.common_title_back)
    public ImageView mTitleBack;// 返回键
    @BindView(R.id.common_title_content)
    public TextView mTitleName;// 标题名
    @BindView(R.id.common_title)
    public LinearLayout mTitleParentLayout;// 通用标题栏


    protected abstract int getLayoutId();

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarState(); // 设置系统状态栏
        initContentView(R.layout.common_title_bar);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initData(savedInstanceState);
    }


    private void initContentView(@LayoutRes int layoutResID) {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parentLinearLayout);

        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);

    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);
    }


    @OnClick(R.id.common_title_back)
    public void commonTitleBack() {
        // 标题栏返回
        AppManager.getAppManager().killActivity(this.getClass());
    }

    private void setStatusBarState() {


    }


    /**
     * 隐藏顶部标题栏
     */
    public void hideCommonTitleBar(LinearLayout view) {
        if (null != view) {
            view.setVisibility(View.GONE);
        }

    }


    /**
     * 设置标题名称
     *
     * @param content
     */
    public void setCommonTitle(String content) {
        if (mTitleName != null) {
            mTitleName.setText(content);
        }
    }

    /**
     * 退出当前应用
     */
    public void exitApp(Context context) {
        Intent intenn = new Intent();
        intenn.setAction("android.intent.action.MAIN");
        intenn.addCategory("android.intent.category.HOME");
        context.startActivity(intenn);
        Process.killProcess(Process.myPid());
    }


    /**
     * 显示加载弹框
     */
    protected void showLoadingDialog() {
        loadingDlg = new LoadingDialog(this, R.style.LoadingDialog, "加载中...", false, false);
        loadingDlg.show();
    }


    /**
     * 销毁加载弹框
     */
    protected void dimissLoadingDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadingDlg != null && loadingDlg.isShowing()) {
                    loadingDlg.dismiss();
                    loadingDlg = null;
                }
            }
        }, 300);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
