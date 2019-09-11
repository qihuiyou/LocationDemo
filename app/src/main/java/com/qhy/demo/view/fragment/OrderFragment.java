package com.qhy.demo.view.fragment;

import android.os.Bundle;

import com.qhy.demo.R;
import com.qhy.demo.base.BaseFragment;
import com.qhy.demo.utils.SystemBarHelper;


/**
 * Created by qhy on 2019/9/9.
 */
public class OrderFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onResume() {
        super.onResume();
        SystemBarHelper.changeToLightStatusBar(getActivity());
    }

}
