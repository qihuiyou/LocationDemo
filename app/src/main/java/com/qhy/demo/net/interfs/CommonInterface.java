package com.qhy.demo.net.interfs;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by qhy on 2019/9/9.
 */
public interface CommonInterface {

    @Headers({"Content-Type: application/json;charset=utf-8"})
    @POST
    Observable<Response<String>> postCall(@Url String api, @Body String request);

    @Multipart
    @POST
    Observable<String> uploadFile(@Url String api, @Part MultipartBody.Part file);
}
