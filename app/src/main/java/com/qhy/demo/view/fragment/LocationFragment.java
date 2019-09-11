package com.qhy.demo.view.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;


import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.qhy.demo.R;
import com.qhy.demo.app.Constants;
import com.qhy.demo.base.BaseFragment;
import com.qhy.demo.utils.SystemBarHelper;
import com.qhy.demo.utils.ToastUtils;
import com.qhy.demo.view.activity.SearchLsitActivity;
import com.qhy.demo.widget.overlay.PoiOverlay;


import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by qhy on 2019/9/9.
 */
public class LocationFragment extends BaseFragment implements PoiSearch.OnPoiSearchListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter {


    @BindView(R.id.ll_location)
    LinearLayout mLl;
    @BindView(R.id.tv_search_keyword)
    TextView mTvKeywords;

    private AMap mAMap;
    private MyLocationStyle myLocationStyle;
    private UiSettings mUiSettings;
    private MapView mMapView;


    private int currentPage = 1;
    private String mKeyWords = "";// 要输入的poi搜索关键字

    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private PoiResult poiResult; // poi返回的结果
    private ProgressDialog progDialog = null;// 搜索时进度条
    private Marker mPoiMarker;

    public static final int REQUEST_CODE = 1000;
    public static final int RESULT_CODE_INPUTTIPS = 1001;
    public static final int RESULT_CODE_KEYWORDS = 1002;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_location;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setDefaultCity(savedInstanceState);
        initLoacationMap();
    }


    /**
     * 配置map
     */
    private void initLoacationMap() {
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(1000 * 60 * 60 * 24); // 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);// 设置定位蓝点的Style
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));// 设置缩放距离

        mUiSettings = mAMap.getUiSettings();//实例化UiSettings类对象

    }

    /**
     * 设置默认显示城市 - 当前默认上海
     *
     * @param savedInstanceState
     */
    private void setDefaultCity(Bundle savedInstanceState) {
        AMapOptions mapOptions = new AMapOptions();
        // 设置了一个可视范围的初始化位置
        // CameraPosition 第一个参数： 目标位置的屏幕中心点经纬度坐标。
        // CameraPosition 第二个参数： 目标可视区域的缩放级别
        // CameraPosition 第三个参数： 目标可视区域的倾斜度，以角度为单位。
        // CameraPosition 第四个参数： 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度
        mapOptions.camera(new CameraPosition(Constants.SHANGHAI, 10f, 0, 0));
        mMapView = new MapView(getActivity(), mapOptions);
        mMapView.onCreate(savedInstanceState);// 创建地图

        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
        }
        mLl.addView(mMapView);
    }


    /**
     * 设置页面监听
     */
    private void setUpMap() {
        mAMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        mAMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
        mAMap.getUiSettings().setRotateGesturesEnabled(false);
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(mContext);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + mKeyWords);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }


    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keywords) {
        showProgressDialog();// 显示进度框
        currentPage = 1;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keywords, "", Constants.DEFAULT_CITY);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(10);
        // 设置查第一页
        query.setPageNum(currentPage);

        poiSearch = new PoiSearch(mContext, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
                null);
        TextView title = view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtils.showShort(infomation);

    }


    @OnClick(R.id.rl_search)
    public void doSearch() {
        // 搜索
        Intent intent = new Intent(mContext, SearchLsitActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
        getActivity().overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();// 重新绘制加载地图
        }
        SystemBarHelper.changeToLightStatusBar(getActivity());
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause(); // 暂停地图的绘制
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState); // 保存地图当前的状态
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();// 销毁地图
        }
    }


    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == 1000) {
            if (result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        mAMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(mAMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtils.showShort(R.string.no_result);
                    }
                }
            } else {
                ToastUtils.showShort(R.string.no_result);
            }
        } else {
            ToastUtils.showerror(mContext, rCode);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        // TODO Auto-generated method stub
    }


    /**
     * 输入提示activity选择结果后的处理逻辑
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_INPUTTIPS && data
                != null) {
            mAMap.clear();
            Tip tip = data.getParcelableExtra(Constants.EXTRA_TIP);
            if (tip.getPoiID() == null || tip.getPoiID().equals("")) {
                doSearchQuery(tip.getName());
            } else {
                addTipMarker(tip);
            }
            mTvKeywords.setText(tip.getName());

        } else if (resultCode == RESULT_CODE_KEYWORDS && data != null) {
            mAMap.clear();
            String keywords = data.getStringExtra(Constants.KEY_WORDS_NAME);
            if (keywords != null && !keywords.equals("")) {
                doSearchQuery(keywords);
            }
            mTvKeywords.setText(keywords);
        }
    }

    /**
     * 用marker展示输入提示list选中数据
     *
     * @param tip
     */
    private void addTipMarker(Tip tip) {
        if (tip == null) {
            return;
        }
        mPoiMarker = mAMap.addMarker(new MarkerOptions());
        LatLonPoint point = tip.getPoint();
        if (point != null) {
            LatLng markerPosition = new LatLng(point.getLatitude(), point.getLongitude());
            mPoiMarker.setPosition(markerPosition);
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 17));
        }
        mPoiMarker.setTitle(tip.getName());
        mPoiMarker.setSnippet(tip.getAddress());
    }


}
