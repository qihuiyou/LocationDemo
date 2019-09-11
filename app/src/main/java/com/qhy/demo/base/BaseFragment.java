package com.qhy.demo.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qhy.demo.R;
import com.qhy.demo.widget.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by qhy on 2019/9/9.
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG = getClass().getName();

    protected Context mContext;
    private Unbinder mUnbinder;
    private LoadingDialog loadingDlg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }


    protected abstract int getLayoutId();


    protected abstract void initData(Bundle savedInstanceState);


    /**
     * 显示加载弹框
     */
    protected void showLoadingDialog() {
        loadingDlg = new LoadingDialog(getActivity(), R.style.LoadingDialog, "加载中...", false, false);
        loadingDlg.show();
    }


    /**
     * 销毁加载弹框
     */
    protected void dimissLoadingDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadingDlg != null && loadingDlg.isShowing()) {
                    loadingDlg.dismiss();
                    loadingDlg = null;
                }
            }
        }, 300);
    }


    @Override
    public void onDestroyView() {
        if (this.mUnbinder != null) {
            this.mUnbinder.unbind();// 解除ButterKnife的绑定
            this.mUnbinder = null;
        }
        super.onDestroyView();
    }
}
