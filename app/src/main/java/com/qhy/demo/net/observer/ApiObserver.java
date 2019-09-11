package com.qhy.demo.net.observer;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
/**
 * Created by qhy on 2019/9/9.
 */
public abstract class ApiObserver<T> implements Observer<T> {

    /**
     * 建立链接,代表正式与被观察者关联
     */
    @Override
    public final void onSubscribe(Disposable d) {
    }

    /**
     * 收到事件，代表通讯成功，一切正常
     */
    @Override
    public final void onNext(T value) {
        onSuccess(value);
    }

    /**
     * 收到错误事件，代表通讯出问题了,也不会再接收事件了
     */
    @Override
    public final void onError(Throwable e) {
        onException(e);
    }

    /**
     * 事件接收完成，不管再发送多少事件过来，都不会再接收了
     */
    @Override
    public final void onComplete() {
    }

    //==============================================================================================




    public abstract void onSuccess(T t);


    public abstract void onException(Throwable e);

}
