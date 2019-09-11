package com.qhy.demo.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.qhy.demo.R;
import com.qhy.demo.base.BaseFragment;
import com.qhy.demo.utils.SystemBarHelper;
import com.qhy.demo.view.activity.LoginActivity;

import butterknife.OnClick;


/**
 * Created by qhy on 2019/9/9.
 */
public class MineFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onResume() {
        super.onResume();
        SystemBarHelper.changeToLightStatusBar(getActivity());
    }


    @OnClick(R.id.tv_mine_login)
    public void login() {
        // 登录
        startActivity(new Intent(mContext, LoginActivity.class));
//        getActivity().overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }


}
