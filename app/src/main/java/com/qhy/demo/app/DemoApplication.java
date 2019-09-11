package com.qhy.demo.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;



/**
 * Created by qhy on 2019/9/9.
 */
public class DemoApplication extends Application implements Application.ActivityLifecycleCallbacks {


    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this.getApplicationContext();

        registerActivityLifecycleCallbacks(this);


        AppManager.getAppManager().init(this);


    }


    public static Context getContext() {
        return mContext;
    }


    /**============================= ActivityLifecycleCallbacks start =========================**/
    /**
     * Activity统一管理
     *
     * @param activity
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        AppManager.getAppManager().removeActivity(activity);
    }
    /**============================= ActivityLifecycleCallbacks end =========================**/


}
