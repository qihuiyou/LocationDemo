package com.qhy.demo.net.api;

import android.support.annotation.NonNull;
import android.util.Log;


import com.qhy.demo.net.HttpConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by qhy on 2019/9/9.
 */
public class NetRetrofit {

    private final String TAG = "---NetRetrofit---";
    private static NetRetrofit mInstance;

    private NetRetrofit() {
    }

    public static NetRetrofit getInstance() {
        if (null == mInstance) {
            synchronized (NetRetrofit.class) {
                if (mInstance == null) {
                    mInstance = new NetRetrofit();
                }
            }
        }
        return mInstance;
    }

    public Retrofit getRetrofit() {

        //开启Log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Log.i(TAG, "message:" + message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//        //缓存
//        File cacheFile = new File("", "cache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        Interceptor headInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        //.addHeader("Cookie", "")
                        .build();
                return chain.proceed(request);
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(HttpConfig.READ_TIME_OUT, TimeUnit.SECONDS)//读取超时
                .writeTimeout(HttpConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)//写入超时
                .connectTimeout(HttpConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)//连接超时
                .addInterceptor(logInterceptor)
                .addInterceptor(headInterceptor)
                //.cache(cache)
                .build();

        return new Retrofit.Builder()
                .baseUrl(HttpConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())//返回值String的支持
                //.addConverterFactory(JacksonConverterFactory.create())//返回值Json的支持，使用Jackson解析（但是后台返回的是加密的String，因此不需要使用）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
