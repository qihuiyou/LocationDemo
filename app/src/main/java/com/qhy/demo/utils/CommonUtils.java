package com.qhy.demo.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.qhy.demo.app.Constants;
import com.qhy.demo.app.DemoApplication;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by qhy on 2019/9/9.
 */
public class CommonUtils {


    /**
     * dp转换成px
     *
     * @param cxt
     * @param dpVal
     * @return
     */
    public static int dp2px(Context cxt, int dpVal) {
        float density = cxt.getResources().getDisplayMetrics().scaledDensity;
        return (int) (dpVal * density + 0.5f);
    }


    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }


    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 隐藏软键盘
     */
    public static void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 打开系统软键盘
     */
    public static void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }


    /**
     * 设置SearchView下划线透明
     **/
    public static void setUnderLinetransparent(SearchView searchView) {
        if (searchView == null) {
            return;
        }
        try {
            Class<?> argClass = searchView.getClass();
            // mSearchPlate是SearchView父布局的名字
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }


    /**
     * 四色五入到两位小数
     *
     * @param v
     * @param scale
     * @return
     */
    public static float round(float v, int scale) {
        if (scale < 0)
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        BigDecimal bgNum1 = new BigDecimal(Float.toString(v));
        BigDecimal bgNum2 = new BigDecimal("1");
        return bgNum1.divide(bgNum2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
        // return b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }


    /**
     * 获取高德地图两点之间的距离
     *
     * @param v
     * @param v1
     * @return
     */
    public static String getAmapDistance(double v, double v1) {
        String AMAP_LATLNG_LONGITUDE = SharedPreUtils.getString(DemoApplication.getContext(), Constants.AMAP_LATLNG_LONGITUDE, "");
        String AMAP_LATLNG_LATITUDE = SharedPreUtils.getString(DemoApplication.getContext(), Constants.AMAP_LATLNG_LATITUDE, "");
        LatLng latLng1 = new LatLng(Double.parseDouble(AMAP_LATLNG_LATITUDE), Double.parseDouble(AMAP_LATLNG_LONGITUDE));
        LatLng latLng = new LatLng(v, v1);
        float distance = AMapUtils.calculateLineDistance(latLng, latLng1);
        float qianmifload = (float) distance / 1000;
        return CommonUtils.round(qianmifload, 2) + "km";
    }


    /**
     * 更改输入手机号的格式
     *
     * @param mEditText
     */
    public static void phoneNumAddSpace(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 3 || index == 8)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

}
