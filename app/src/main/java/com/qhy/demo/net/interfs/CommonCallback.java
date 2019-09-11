package com.qhy.demo.net.interfs;

import android.support.annotation.NonNull;

/**
 * Created by qhy on 2019/9/9.
 */
public abstract class CommonCallback<P> {

    private Class<?> entityClass = null;

    public CommonCallback() {
    }

    public CommonCallback(Class<?> entityClass) {
        this.entityClass = entityClass;
    }


    public Class<?> getEntityClass() {
        return entityClass;
    }


    /**
     * 请求成功：相应状态正确、有数据
     *
     * @param p 返回数据
     */
    public abstract void onSuccess(P p);


    /**
     * 请求失败：后台异常、代码异常
     *
     * @param errorCode    异常码
     * @param errorMessage 异常信息
     */
    public abstract void onFailure(int errorCode, @NonNull String errorMessage);

}
