package com.qhy.demo.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.qhy.demo.R;
import com.qhy.demo.app.Constants;
import com.qhy.demo.app.DemoApplication;
import com.qhy.demo.base.BaseActivity;
import com.qhy.demo.app.UserSession;
import com.qhy.demo.utils.Logs;
import com.qhy.demo.utils.SharedPreUtils;
import com.qhy.demo.view.fragment.HomeFragment;
import com.qhy.demo.view.fragment.MineFragment;
import com.qhy.demo.view.fragment.OrderFragment;
import com.qhy.demo.view.fragment.LocationFragment;
import com.qhy.demo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by qhy on 2019/9/9.
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.rg_main_tab)
    RadioGroup mRadioGroup;
    @BindViews({R.id.rb_main_home, R.id.rb_main_policy, R.id.rb_main_park, R.id.rb_main_mine})
    List<RadioButton> mRadioButtonList;


    private List<Fragment> mFsList = new ArrayList<>();
    private long exitTime = 0;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String locationAddress;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        //        ARouter.getInstance().inject(this);// ARouter inject 注入
        // 首页不需要标题栏，需要隐藏
        hideCommonTitleBar(mTitleParentLayout);
        initTabFragment();
        initLocation();
        startLocation();
    }


    private void initTabFragment() {

        mFsList.add(new HomeFragment());
        mFsList.add(new LocationFragment());
        mFsList.add(new OrderFragment());
        mFsList.add(new MineFragment());

        // 默认选中第一个
        changeFrag(0);

    }


    private void changeFrag(int curIndex) {
        for (int i = 0; i < mFsList.size(); i++) {
            if (i == curIndex) {
                showFragment(mFsList.get(i));
            } else {
                hideFragment(mFsList.get(i));
            }
        }
    }


    /**
     * 隐藏当前的fragment
     */
    protected void hideFragment(Fragment currFragment) {
        if (currFragment == null) {
            return;
        }
        FragmentTransaction currFragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        currFragment.onPause();
        if (currFragment.isAdded()) {
            currFragmentTransaction.hide(currFragment);
            currFragmentTransaction.commitAllowingStateLoss();
        }
    }

    /**
     * 显示当前的fragment
     */
    protected void showFragment(Fragment startFragment) {
        if (startFragment == null) {
            return;
        }
        FragmentTransaction startFragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (!startFragment.isAdded()) {
            startFragmentTransaction.add(R.id.fl_main_content, startFragment);
        } else {
            startFragment.onResume();
            startFragmentTransaction.show(startFragment);
        }
        startFragmentTransaction.commitAllowingStateLoss();

    }


    @OnClick(R.id.rb_main_home)
    public void rbMainHome() {
        // 首页
        changeFrag(0);
    }


    @OnClick(R.id.rb_main_policy)
    public void rbMainPolicy() {
        // 政策
        changeFrag(1);
    }

    @OnClick(R.id.rb_main_park)
    public void rbMainPark() {
        // 园区
        changeFrag(2);
    }

    @OnClick(R.id.rb_main_mine)
    public void rbMainMine() {
        // 我的
        changeFrag(3);
    }


    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(DemoApplication.getContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }


    /**
     * 获取AMapLocationClient
     *
     * @return
     */
    public AMapLocationClient getAMapLocationClient() {
        return locationClient;
    }


    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }


    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    locationAddress = location.getProvince();
                    Logs.d(TAG, "省：" + location.getProvince() +
                            "\n市：" + location.getCity() +
                            "\n区：" + location.getDistrict() +
                            "\n街道：" + location.getAddress());

                    Logs.d(TAG, "纬度：" + location.getLatitude() + "经度" + location.getLongitude());
                    SharedPreUtils.saveString(MainActivity.this, Constants.AMAP_LATLNG_LATITUDE, location.getLatitude() + "");
                    SharedPreUtils.saveString(MainActivity.this, Constants.AMAP_LATLNG_LONGITUDE, location.getLongitude() + "");

                } else {
                    locationAddress = "上海";// 定位失败，默认显示上海
                    Logs.d(TAG, "获取定位失败");
                }
            } else {
                locationAddress = "上海";// 定位失败，默认显示上海
                Logs.d(TAG, "获取定位失败");
            }

            HomeFragment homeFragment = (HomeFragment) mFsList.get(0);
            if (homeFragment != null) {
                homeFragment.setAddressTitle(locationAddress);
            }
        }
    };


    /**
     * 开始定位
     */
    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }


    /**
     * 重新设置定位参数
     */
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(false);
        // 设置是否单次定位
        locationOption.setOnceLocation(false);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(false);
        //设置是否使用传感器
        locationOption.setSensorEnable(false);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        try {
            // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
            locationOption.setInterval(60 * 60 * 24 * 1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            // 设置网络请求超时时间
            locationOption.setHttpTimeOut(20000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 3000) {
            ToastUtils.showShort("再按一次退出");
            exitTime = System.currentTimeMillis();
            return;
        }

        UserSession.getInstance().setLoginStatus(false);//退出登录
        exitApp(MainActivity.this);

    }


}
