package com.qhy.demo.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.qhy.demo.R;
import com.qhy.demo.app.AppManager;
import com.qhy.demo.app.Constants;
import com.qhy.demo.base.BaseActivity;
import com.qhy.demo.base.BaseWebView;
import com.qhy.demo.utils.Logs;
import com.qhy.demo.web.ParkJavascriptInterface;
import com.qhy.demo.utils.CommonUtils;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by qhy on 2019/9/9.
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends BaseActivity {


    @BindView(R.id.basewebview)
    BaseWebView mWebView;
    @BindView(R.id.tv_web_title)
    TextView mWebTitle;
    @BindView(R.id.view_web_title_line)
    View mWebLine;

    private String mUrl, mTitle;
    private WebSettings mWebSettings;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mUrl = getIntent().getStringExtra(Constants.H5_LINK);
        mTitle = getIntent().getStringExtra(Constants.H5_TITLE);
        initWebview();
        initTitle();
    }

    private void initTitle() {
        hideCommonTitleBar(mTitleParentLayout);
        mWebTitle.setText(mTitle);
    }

    private void initWebview() {


        mWebSettings = mWebView.getSettings();

        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);//是否可以缩放，默认true
        mWebSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebSettings.setAppCacheEnabled(true);//是否使用缓存
        mWebSettings.setDomStorageEnabled(true);//开启本地DOM存储
        mWebSettings.setLoadsImagesAutomatically(true); // 加载图片


        mWebView.loadUrl(mUrl);
        Logs.d(TAG,"link: " + mUrl);


        mWebView.addJavascriptInterface(new ParkJavascriptInterface(this), "PAATESSAYJS");
        mWebView.addProgress(WebViewActivity.this);


        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logs.d(TAG,"shouldOverrideUrlLoading: " + url);
                view.loadUrl(url);
                return true;
            }

        });


        mWebView.setOnScrollChangedCallback(new BaseWebView.OnScrollChangedCallback() {
            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {
                mWebTitle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {
                mWebTitle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                if (t > CommonUtils.dp2px(WebViewActivity.this, 45)) {
                    mWebTitle.setVisibility(View.VISIBLE);
                    mWebLine.setVisibility(View.VISIBLE);
                } else {
                    mWebTitle.setVisibility(View.INVISIBLE);
                    mWebLine.setVisibility(View.INVISIBLE);
                }
            }
        });


    }


    @OnClick({R.id.iv_web_back, R.id.iv_web_share})
    public void onTitleclick(View view) {
        switch (view.getId()) {
            case R.id.iv_web_back:// 返回
                AppManager.getAppManager().killActivity(WebViewActivity.class);
                break;
            case R.id.iv_web_share:// 分享

                break;
        }
    }


    public static void startActivityByFragment(Activity activity,
                                               Fragment fragment,
                                               String url,
                                               String title) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(Constants.H5_LINK, url);
        intent.putExtra(Constants.H5_TITLE, title);
        fragment.startActivity(intent);
    }


    public static void startActivityByActivity(Activity activity,
                                               String url,
                                               String title) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(Constants.H5_LINK, url);
        intent.putExtra(Constants.H5_TITLE, title);
        activity.startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            AppManager.getAppManager().killActivity(WebViewActivity.class);
        }
    }


}
