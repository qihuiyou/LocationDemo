package com.qhy.demo.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.qhy.demo.R;

/**
 * Created by qhy on 2019/9/9.
 */
public class BaseWebView extends WebView {


    private OnScrollChangedCallback mOnScrollChangedCallback;

    private ProgressBar mProgressBar;


    public BaseWebView(final Context context) {
        super(context);
    }

    public BaseWebView(final Context context, final AttributeSet attrs){
        super(context, attrs);
    }

    public BaseWebView(final Context context, final AttributeSet attrs,final int defStyle) {
        super(context, attrs, defStyle);
    }


    public void addProgress(Context context) {
        mProgressBar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 3));
        mProgressBar.setLayoutParams(layoutParams);

        Drawable drawable = context.getResources().getDrawable(
                R.drawable.progressbar_back);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);
        setWebChromeClient(new MyWebChromeClient());
    }


    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        float webcontent = getContentHeight() * getScale();// webview的高度
        float webnow = getHeight() + getScrollY();// 当前webview的高度
        if (Math.abs(webcontent - webnow) < 1) {
            // 已经处于底端
            mOnScrollChangedCallback.onPageEnd(l, t, oldl, oldt);
        } else if (getScrollY() == 0) {
            mOnScrollChangedCallback.onPageTop(l, t, oldl, oldt);
        } else {
            mOnScrollChangedCallback.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }



    public class MyWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.e("-------------",newProgress+"----");
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                    mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }


    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }



    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    public interface OnScrollChangedCallback {
        void onPageEnd(int l, int t, int oldl, int oldt);

        void onPageTop(int l, int t, int oldl, int oldt);

        void onScrollChanged(int l, int t, int oldl, int oldt);
    }


}
