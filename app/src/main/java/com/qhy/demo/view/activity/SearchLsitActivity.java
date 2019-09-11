package com.qhy.demo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.qhy.demo.R;
import com.qhy.demo.adapter.SearchAdapter;
import com.qhy.demo.app.AppManager;
import com.qhy.demo.app.Constants;
import com.qhy.demo.base.BaseActivity;
import com.qhy.demo.inter.OnItemClickListener;
import com.qhy.demo.utils.CommonUtils;
import com.qhy.demo.utils.SystemBarHelper;
import com.qhy.demo.utils.ToastUtils;
import com.qhy.demo.view.fragment.LocationFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by qhy on 2019/9/10.
 * 搜索列表页
 */
public class SearchLsitActivity extends BaseActivity implements SearchView.OnQueryTextListener, Inputtips.InputtipsListener, OnItemClickListener {


    @BindView(R.id.keyWord)
    SearchView mSearchView;
    @BindView(R.id.recycle_search)
    RecyclerView mRecyclerView;


    private SearchAdapter mSearchAdapter;

    private ArrayList<Tip> mSearchContentList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        hideCommonTitleBar(mTitleParentLayout);
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.changeToLightStatusBar(this);
        initSearchList();
        initSearchView();
    }


    private void initSearchView() {
        mSearchView.setOnQueryTextListener(this);
        // 设置SearchView默认为展开显示
        mSearchView.setIconified(false);
        mSearchView.onActionViewExpanded();
        // 设置搜索图标是否显示在搜索框内
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(false);
        // 设置下划线透明
        CommonUtils.setUnderLinetransparent(mSearchView);
    }


    // 搜索内容列表
    private void initSearchList() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearchAdapter = new SearchAdapter(this, mSearchContentList);
        mRecyclerView.setAdapter(mSearchAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mSearchAdapter.setItemClickListener(this);
    }


    /**
     * 输入提示回调
     *
     * @param tipList
     * @param rCode
     */
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {// 正确返回
            mSearchContentList.clear();
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            mSearchContentList.addAll(tipList);
            mSearchAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showerror(this, rCode);
        }

    }


    @OnClick(R.id.iv_search_back)
    public void setSearchBack() {
        AppManager.getAppManager().killActivity(SearchLsitActivity.class);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_WORDS_NAME, query);
        setResult(LocationFragment.RESULT_CODE_KEYWORDS, intent);
        this.finish();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!CommonUtils.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, Constants.DEFAULT_CITY);
            Inputtips inputTips = new Inputtips(SearchLsitActivity.this.getApplicationContext(), inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } else {
            if (mSearchAdapter != null && mSearchContentList != null) {
                mSearchContentList.clear();
                mSearchAdapter.notifyDataSetChanged();
            }
        }
        return false;
    }


    @Override
    public void onItemClick(View view, int position) {
        if (mSearchContentList != null) {
            Tip tip = mSearchContentList.get(position);
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_TIP, tip);
            setResult(LocationFragment.RESULT_CODE_INPUTTIPS, intent);
            AppManager.getAppManager().killActivity(SearchLsitActivity.class);
        }
    }
}
