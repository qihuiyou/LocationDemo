package com.qhy.demo.net;

/**
 * Created by qhy on 2019/9/9.
 */
public class HttpConfig {

    public static int DEV_TYPE = 4; // 0 -- 开发; 1 -- 测试; 2 -- UAT; 3 -- 生产; 4 -- VIP;

    public static String VERSION = "v2.3/";// 当前API版本号

    private static final String BASE_URL_ORG = "http://api.jieyuanbao.org/" + VERSION;
    private static final String BASE_URL_NET = "http://api.jieyuanbao.net/" + VERSION;
    private static final String BASE_URL_LIVE = "http://api.jieyuanbao.live/" + VERSION;
    private static final String BASE_URL_PRD = "http://api.jieyuanbao.com/" + VERSION;
    private static final String BASE_URL_VIP = "https://api.jieyuanbao.vip/" + VERSION;


    private final static String SECRET_KEY_ORG = "1S4K9ijfGfRtXEyr8a3cHx8a3ncE7qzK";
    private final static String SECRET_KEY_NET = "9uxxPbd3jvgE4gTB1LD5RlDwKNj9eFBX";
    private final static String SECRET_KEY_UAT = "MfxyjhLjTIxnPHvarMqcqOFuIfuDBlFD";
    private final static String SECRET_KEY_PRD = "3nTLENo7TjAt1oGiTNWgwKtq5huk3Jom";


    public static final String BASE_URL = getBaseUrl();
    public static final String BASE_H5_URL = getH5BaseUrl();
    public final static String SECRET_KEY = getSecretKey();


    public static final long READ_TIME_OUT = 60;    //读取超时时间
    public static final long WRITE_TIME_OUT = 30;   //写入超时时间
    public static final long CONNECT_TIME_OUT = 20; //连接超时时间



    /**
     * 获取各环境的API的域名
     *
     * @return
     */
    private static String getBaseUrl() {
        if (DEV_TYPE == 0) { // API为开发环境
            return BASE_URL_ORG;
        } else if (DEV_TYPE == 1) { // API为测试环境
            return BASE_URL_NET;
        } else if (DEV_TYPE == 2) {
            return BASE_URL_LIVE;
        } else if (DEV_TYPE == 3) {
            return BASE_URL_PRD;
        } else if (DEV_TYPE == 4) {
            return BASE_URL_VIP;
        } else { // 其他调试地址
            return "172.16.10.125:8837/";
        }
    }

    /**
     * 获取各环境的H5域名
     *
     * @return
     */
    private static String getH5BaseUrl() {
        if (DEV_TYPE == 0) {
            return "http://m.jieyuanbao.org/" + VERSION; // dev
        } else if (DEV_TYPE == 1) { // API为测试环境
            return "http://m.jieyuanbao.net/" + VERSION; // TEST
        } else if (DEV_TYPE == 2) {
            return "http://m.jieyuanbao.live/" + VERSION; // UAT
        } else if (DEV_TYPE == 3) {
            return "http://m.jieyuanbao.com/" + VERSION; // PRD
        }else if (DEV_TYPE == 4) {
            return "https://m.jieyuanbao.vip/" + VERSION; // vip;
        } else { // 其他调试地址
            return "172.16.10.125:8837/";
        }
    }


    private static String getSecretKey() {
        if (DEV_TYPE == 3) {
            return SECRET_KEY_PRD;
        } else if(DEV_TYPE == 0) {
            return SECRET_KEY_ORG;
        } else if(DEV_TYPE == 1) {
            return SECRET_KEY_NET;
        } else if(DEV_TYPE == 2) {
            return SECRET_KEY_UAT;
        } else if(DEV_TYPE == 4) {
            return SECRET_KEY_NET;
        }
        return SECRET_KEY_ORG;
    }




}
