package com.qhy.demo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.qhy.demo.R;
import com.qhy.demo.app.AppManager;
import com.qhy.demo.base.BaseActivity;
import com.qhy.demo.utils.SystemBarHelper;
import com.qhy.demo.view.MainActivity;

/**
 * Created by qhy on 2019/9/9.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        hideCommonTitleBar(mTitleParentLayout);
        SystemBarHelper.immersiveStatusBar(this,0);
        SystemBarHelper.changeToLightStatusBar(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 模拟网络请求耗时
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                AppManager.getAppManager().killActivity(SplashActivity.class);
            }
        }, 1000);


    }


}
