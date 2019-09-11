package com.qhy.demo.net.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qhy.demo.BuildConfig;
import com.qhy.demo.app.AppManager;
import com.qhy.demo.app.DemoApplication;
import com.qhy.demo.app.UserSession;
import com.qhy.demo.model.api.HttpApi;
import com.qhy.demo.net.HttpConfig;
import com.qhy.demo.net.interfs.CommonCallback;
import com.qhy.demo.net.interfs.CommonInterface;
import com.qhy.demo.net.interfs.FileCallback;
import com.qhy.demo.net.observer.ApiObserver;
import com.qhy.demo.utils.ToastUtils;
import com.qhy.demo.view.activity.LoginActivity;


import org.json.JSONException;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * Created by qhy on 2019/9/9.
 */
public class NetHandler {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 处理请求报文
     *
     * @param request 接口请求参数
     * @return 增加公共参数后的请求参数
     * @throws NetException 异常
     */
    @SuppressWarnings("unchecked")
    protected String handleRequest(Object request, Context context) throws NetException {

        Map requestMap;
        if (request instanceof Map)
            requestMap = (Map) request;
        else {
            try {
                requestMap = str2Map(obj2String(request));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new NetException(NetCode.E2001);
            } catch (IOException e) {
                e.printStackTrace();
                throw new NetException(NetCode.E2002);
            }
        }

        long time = System.currentTimeMillis();
        requestMap.put("sendTime", time);
        requestMap.put("deviceId", DeviceId(context));
        requestMap.put("sign", MD5(DeviceId(context) + time + HttpConfig.SECRET_KEY));

        // 登录所需公共参数
        if (UserSession.getInstance().isLogin()) {
            requestMap.put("userId", UserSession.getInstance().getUserId());
            requestMap.put("accessToken", UserSession.getInstance().getToken());
        }

        String requestStr;
        try {
            requestStr = obj2String(requestMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new NetException(NetCode.E2001);
        }

        return requestStr;
    }

    /**
     * 处理返回报文
     *
     * @param response 完整报文
     * @return resultInfo 报文
     * @throws NetException 异常
     */
    protected String handleResponse(String response) throws NetException {

        JsonNode jsonNode;

        try {
            // 空报文处理
            if (TextUtils.isEmpty(response)) return "";
            jsonNode = readJson(response);
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetException(NetCode.E3001);
        }

        JsonNode state = jsonNode.get("state");
        JsonNode msg = jsonNode.get("msg");

        // 特殊报文格式 1：无 state、msg、resultInfo，直接返回报文
        if (state == null) return response;

        // 正常报文格式
        int code = state.asInt();
        String massage = msg == null ? "" : msg.asText();
        if (TextUtils.isEmpty(massage)) massage = "";

        if (code == NetCode.S0.getCode()) {
            JsonNode resultInfo = jsonNode.get("resultInfo");
            return resultInfo == null ? "" : resultInfo.toString();
        } else if (code == NetCode.S1.getCode()) {
            // other
            throw new NetException(code, massage);
        } else if (code == NetCode.S99.getCode()) {
            // 用户登录信息失效，需要清除本地登录数据
            throw new NetException(code, massage);
        } else {
            throw new NetException(code, massage);
        }
        /*
         * 报文格式正常约束，如遇特殊话又不能公用现有逻辑，请找领导或相关负责人修改接口
         * 1、state为公用状态码，不允许作为单个接口状态码；
         * 2、state仅在0状态下才解析resultInfo，其他接口不做解析；
         * 以有逻辑暂不做修改，以后遇到请参考以上两点
         */
    }

    /**
     * 异常转换
     */
    protected NetCode handleException(Throwable e) {
        NetCode apiCode;
        if (e instanceof EOFException) {
            apiCode = NetCode.E4001;
        } else if (e instanceof ConnectException) {
            apiCode = NetCode.E4002;
        } else if (e instanceof BindException) {
            apiCode = NetCode.E4003;
        } else if (e instanceof SocketException) {
            apiCode = NetCode.E4004;
        } else if (e instanceof SocketTimeoutException) {
            apiCode = NetCode.E4005;
        } else if (e instanceof UnknownHostException) {
            apiCode = NetCode.E4006;
        } else if (e instanceof JSONException || e instanceof JsonProcessingException) {
            apiCode = NetCode.E3000;
        } else {
            apiCode = NetCode.E1000;
        }
        return apiCode;
    }


    /**
     * 普通网络请求
     *
     * @param api      API接口url
     * @param request  请求参数
     * @param callback 回调
     * @param <Q>      请求类型
     * @param <P>      返回类型
     * @throws NetException 异常
     */
    @SuppressWarnings("unchecked")
    protected <Q, P> void execute(@NonNull final String api,
                                  @NonNull final Q request,
                                  final CommonCallback callback) throws NetException {

        String requestStr = handleRequest(request, DemoApplication.getContext());
        CommonInterface apiInterface = NetRetrofit.getInstance().getRetrofit().create(CommonInterface.class);
        apiInterface.postCall(api, requestStr)
                .subscribeOn(Schedulers.io())
                .delay(500, TimeUnit.MILLISECONDS)//延迟半秒，目的：不让加载狂销毁太快，
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<Response<String>>() {

                    @Override
                    public void onSuccess(Response<String> httpResponse) {
                        try {
                            int code = httpResponse.code();
                            if (code != 200 && BuildConfig.DEBUG) {// 仅开发提示，生产走未知错误
                                throw new NetException(code, String.valueOf(code));
                            }
                            String body = httpResponse.body();
                            String response = handleResponse(body);
                            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//在反序列化时忽略在 json 中存在但 Java 对象不存在的属性

                            if (callback != null) {
                                P p;

                                if (!TextUtils.isEmpty(response)) {
                                    Class entityClass = callback.getEntityClass();
                                    if (entityClass == null) {
                                        Class<P> responseClass = (Class<P>) getActualTypeArgument(callback.getClass());
                                        if (null == responseClass)
                                            throw new NetException(NetCode.E3004.getCode(), NetCode.E3004.getMessage());
                                        else if (responseClass == String.class) {
                                            p = (P) response;
                                        } else {
                                            p = objectMapper.readValue(response, responseClass);
                                        }
                                    } else { // 暂时仅添加ArrayList解析的支持
                                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, entityClass);
                                        List<P> list = objectMapper.readValue(response, javaType);
                                        ((CommonCallback<List<P>>) callback).onSuccess(list);
                                        return;
                                    }
                                } else {
                                    p = null;
                                }
                                callback.onSuccess(p);
                            }
                        } catch (NetException e) {
                            e.printStackTrace();
                            onFailure(callback, e);
                            if (e.getCode() == NetCode.S99.getCode())
                                againLogin(DemoApplication.getContext());
                        } catch (Throwable e) {
                            e.printStackTrace();
                            onFailure(callback, handleException(e));
                        }
                    }

                    @Override
                    public void onException(Throwable e) {
                        onFailure(callback, handleException(e));
                    }
                });
    }


    /**
     * 上传图片
     *
     * @param file     文件路径
     * @param callback 回调
     * @param <P>      返回类型
     */
    protected <P> void executeUpload(File file, final FileCallback callback) {
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + fileType), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);

        CommonInterface apiInterface = NetRetrofit.getInstance().getRetrofit().create(CommonInterface.class);
        apiInterface.uploadFile(HttpApi.uploadPic, part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<String>() {


                    @Override
                    public void onSuccess(String s) {
                        try {
                            String response = handleResponse(s);

                            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//在反序列化时忽略在 json 中存在但 Java 对象不存在的属性

                            if (callback != null) {
                                P p;
                                Class<P> responseClass = (Class<P>) getActualTypeArgument(callback.getClass());
                                if (null == responseClass)
                                    throw new NetException(NetCode.E3004.getCode(), NetCode.E3004.getMessage());
                                else if (responseClass == String.class) {
                                    p = (P) response;
                                } else {
                                    if (!TextUtils.isEmpty(response))
                                        p = objectMapper.readValue(response, responseClass);
                                    else
                                        p = null;
                                }
                                callback.onSuccess(p);
                            }
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                            onFailure(callback, NetCode.E3003);
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                            onFailure(callback, NetCode.E3002);
                        } catch (IOException e) {
                            e.printStackTrace();
                            onFailure(callback, NetCode.E3001);
                        } catch (NetException e) {
                            e.printStackTrace();
                            onFailure(callback, e);
                        }
                    }

                    @Override
                    public void onException(Throwable e) {
                        onFailure(callback, handleException(e));
                    }
                });
    }


    protected void onFailure(CommonCallback callback, NetCode apiCode) {
        netError(apiCode.getCode());
        if (callback != null)
            callback.onFailure(apiCode.getCode(), apiCode.getMessage());
    }

    protected void onFailure(CommonCallback callback, NetException e) {
        netError(e.getCode());
        if (callback != null)
            callback.onFailure(e.getCode(), e.getMessage());
    }

    /**
     * 解析json字符串
     */
    private JsonNode readJson(String json) throws IOException {
        return objectMapper.readTree(json);
    }

    /**
     * 对象 转 json
     */
    private String obj2String(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * json 转 map
     */
    private Map str2Map(String json) throws IOException {
        return objectMapper.readValue(json, Map.class);
    }

    /**
     * 获取泛型类型
     */
    protected Class<?> getActualTypeArgument(Class<?> clazz) {
        try {
            Class<?> entityClass = null;
            Type genericSuperclass = clazz.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                        .getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    entityClass = (Class<?>) actualTypeArguments[0];
                }
            }
            return entityClass;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String DeviceId(Context cxt) {
        TelephonyManager tm = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            assert tm != null;
            return tm.getDeviceId();
        } catch (Exception e) {
            return "DCBA12345";
        }
    }


    private String MD5(String input) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");// 获得 MD5 摘要算法的 MessageDigest 对象
            mdInst.update(input.getBytes());// 使用指定的字节更新摘要
            byte[] md = mdInst.digest();// 获得密文
            StringBuilder hexString = new StringBuilder();// 把密文转换成十六进制的字符串形式
            for (byte aMd : md) {// 字节数组转换为十六进制数
                String shaHex = Integer.toHexString(aMd & 0xFF);
                if (shaHex.length() < 2) hexString.append(0);
                hexString.append(shaHex);
            }
            return hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 网络错误要跳到网络错误页
    protected void netError(int errorCode) {
//        if (isNetError(errorCode)) {
//             AppManager.getAppManager().startActivity(NetErrorActivity.class);
//        }
        ToastUtils.showShort("网络异常，请稍后重试！");
    }

    // 登录异常 - 重新登录
    protected void againLogin(Context context) {
        UserSession.getInstance().logout();
        AppManager.getAppManager().startActivity(LoginActivity.class);
    }


    // 是否是网络错误的类型
    private boolean isNetError(int code) {
        return code == NetCode.E4001.getCode()
                || code == NetCode.E4002.getCode()
                || code == NetCode.E4003.getCode()
                || code == NetCode.E4004.getCode()
                || code == NetCode.E4005.getCode()
                || code == NetCode.E4006.getCode();

    }
}
