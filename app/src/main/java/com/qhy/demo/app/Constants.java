package com.qhy.demo.app;

import com.amap.api.maps.model.LatLng;

/**
 * Created by qhy on 2019/9/9.
 */
public class Constants {

    //-----------------------------User------------------------------------
    public static final String USER_LOGIN = "user_login";//用户登录
    public static final String USER_JSON_INFO = "user_json_info";//用户信息

    public static final String AMAP_APPKEY = "9dc12c25a4d752439be93e189ea2b6f0";// 高德地图appkey


    //-----------------------------H5------------------------------------
    public static final String H5_LINK = "h5_link";// h5链接地址
    public static final String H5_TITLE = "h5_title";// h5标题


    //-----------------------------搜索------------------------------------
    public static String DEFAULT_CITY = "上海";
    public static final LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// 上海市经纬度
    public static final String EXTRA_TIP = "ExtraTip";
    public static final String KEY_WORDS_NAME = "KeyWord";
    public static final String AMAP_LATLNG_LATITUDE = "amap_latlng_latitude";// 纬度
    public static final String AMAP_LATLNG_LONGITUDE = "amap_latlng_longitude";// 经度


}
