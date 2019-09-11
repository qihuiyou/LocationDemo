package com.qhy.demo.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jaeger.library.StatusBarUtil;
import com.qhy.demo.R;
import com.qhy.demo.base.BaseFragment;
import com.qhy.demo.utils.CommonUtils;
import com.qhy.demo.utils.SystemBarHelper;
import com.qhy.demo.utils.ToastUtils;
import com.qhy.demo.view.MainActivity;
import com.qhy.demo.widget.GradationScrollView;
import com.qhy.demo.widget.NoScrollListview;
import com.qhy.demo.widget.convenientbanner.CBLoopViewPager;
import com.qhy.demo.widget.convenientbanner.ConvenientBanner;
import com.qhy.demo.widget.convenientbanner.holder.CBViewHolderCreator;
import com.qhy.demo.widget.convenientbanner.holder.LocalImageHolderView;
import com.qhy.demo.widget.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by qhy on 2019/9/9.
 */
public class HomeFragment extends BaseFragment implements OnItemClickListener {


    @BindView(R.id.home_scrollview)
    GradationScrollView mGradationScrollView;
    @BindView(R.id.home_convenientBanner)
    ConvenientBanner<Integer> mBanner;
    @BindView(R.id.home_ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.home_listview)
    NoScrollListview mListview;
    @BindView(R.id.home_tv_city)
    TextView mTvCity;


    private CBLoopViewPager mViewPager;


    private ArrayList<Integer> localImages = new ArrayList<Integer>();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mViewPager = mBanner.getViewPager();
        StatusBarUtil.setTranslucentForImageView(getActivity(), 0, null);

        initBanner();
        initListeners();
        initListData();
    }

    private void initListData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.data));
        mListview.setAdapter(adapter);
        mListview.setNestedScrollingEnabled(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        //开始翻页
        mBanner.startTurning(3000);
        SystemBarHelper.cancelLightStatusBar(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        mBanner.stopTurning();
    }

    // 设置当前城市名
    public void setAddressTitle(String address) {
        mTvCity.setText(address);
    }


    private void initBanner() {

        //本地图片集合
        for (int position = 0; position < 7; position++) {
            localImages.add(CommonUtils.getResId("ic_test_" + position, R.drawable.class));
        }

        mBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setOnItemClickListener(this);
    }


    /**
     * 获取顶部图片高度
     */
    private void initListeners() {

        ViewTreeObserver vto = mViewPager.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setGradationListener(mViewPager.getHeight());
                mViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }


    /**
     * 设置动态监听
     *
     * @param imageHeight
     */
    private void setGradationListener(final int imageHeight) {

        mGradationScrollView.setScrollViewListener(new GradationScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y <= 0) {   //设置标题的背景颜色
                    mLlTitle.setBackgroundColor(Color.argb((int) 0, 62, 144, 227));
                } else if (y > 0 && y <= imageHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) y / imageHeight;
                    float alpha = (255 * scale);
                    mLlTitle.setBackgroundColor(Color.argb((int) alpha, 62, 144, 227));
                } else {    //滑动到banner下面设置普通颜色
                    mLlTitle.setBackgroundColor(Color.argb((int) 255, 62, 144, 227));
                }
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        // 轮播图
        ToastUtils.showShort("点击了第" + position + "个");
    }


}
