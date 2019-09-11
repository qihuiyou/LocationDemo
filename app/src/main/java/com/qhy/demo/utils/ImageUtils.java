package com.qhy.demo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qhy.demo.R;

/**
 * Created by qhy on 2019/9/9.
 * 图片加载工具
 */
public class ImageUtils {


    /**
     * 默认加载图片
     */
    public static void load(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.bg_park_default)
                .error(R.mipmap.bg_park_default)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 从res文件内部加载图片
     */
    public static void loadFromRes(Context context, int resourceId, ImageView imageView) {
        Glide.with(context).load(resourceId).into(imageView);
    }

    /**
     * 网上加载指定图片大小的图片
     */
    public static void loadSize(Context context, String url, int width, int height, ImageView imageView) {
        Glide.with(context).load(url).override(width, height).centerCrop().into(imageView);
    }

    /**
     * res文件内部加载指定大小图片
     */
    public static void loadSizeForRes(Context context, int id, int width, int height, ImageView imageView) {
        Glide.with(context).load(id).override(width, height).centerCrop().into(imageView);
    }

    /**
     * 1、加载中图片，加载错误图片，加载正确图片并且设置
     * 2、当没有图片时，加载错误图片
     */
    public static void loadHolder(Context context, String url, int loadingImage, int loadingErrorImage, ImageView imageView) {
        if (url != null && !TextUtils.isEmpty(url)) {
            Glide.with(context).load(url).placeholder(loadingImage).error(loadingErrorImage).into(imageView);
        } else {
            imageView.setImageResource(loadingErrorImage);
        }

    }

    public static void loadHolderSize(Context context, String url, int width, int height, int loadingImage, int loadingErrorImage, ImageView imageView) {
        if (url != null && !TextUtils.isEmpty(url)) {
            Glide.with(context).load(url).override(width, height).centerCrop().placeholder(loadingImage).error(loadingErrorImage).into(imageView);
        } else {
            imageView.setImageResource(loadingErrorImage);
        }
    }


}
