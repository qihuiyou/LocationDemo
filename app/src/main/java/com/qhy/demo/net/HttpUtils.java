package com.qhy.demo.net;


import android.support.annotation.NonNull;


import com.qhy.demo.app.DemoApplication;
import com.qhy.demo.net.api.NetCode;
import com.qhy.demo.net.api.NetHandler;
import com.qhy.demo.net.api.NetException;
import com.qhy.demo.net.interfs.CommonCallback;
import com.qhy.demo.net.interfs.FileCallback;

import java.io.File;
import java.util.HashMap;


/**
 * Created by qhy on 2019/9/9.
 */
public class HttpUtils extends NetHandler {


    private static final class HttpHolder {
        private static final HttpUtils mInstance = new HttpUtils();
    }

    public static HttpUtils getInstance() {
        return HttpHolder.mInstance;
    }


    /**
     * 普通无参网络请求
     */
    public <P> void request(@NonNull String api,
                            CommonCallback<P> callback) {
        request(api, new HashMap<String, Object>(), callback);
    }


    /**
     * 普通带参网络请求
     *
     * @param api      API接口url
     * @param request  请求参数 Map
     * @param callback 回调
     * @param <Q>      请求类型
     * @param <P>      返回类型
     */
    public <Q, P> void request(@NonNull String api,
                               @NonNull final Q request,
                               CommonCallback<P> callback) {
        try {
            execute(api, request, callback);
        } catch (NetException e) {
            e.printStackTrace();
            onFailure(callback, e);
            if (e.getCode() == NetCode.S99.getCode())
                againLogin(DemoApplication.getContext());
        }

    }


    /**
     * 上传图片
     *
     * @param file     文件地址
     * @param callback 回调
     */
    public void uploadPicture(@NonNull File file, FileCallback callback) {
        executeUpload(file, callback);
    }






}
