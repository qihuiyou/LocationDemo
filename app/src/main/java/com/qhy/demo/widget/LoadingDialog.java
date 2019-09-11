package com.qhy.demo.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qhy.demo.R;

/**
 * Created by qhy on 2019/9/9.
 */
public class LoadingDialog extends Dialog {
    @SuppressWarnings("unused")
    private Context ctx;
    private String content;

    public LoadingDialog(Activity context, int theme, String content, boolean cancelable, boolean outside) {
        super(context, theme);
        this.ctx = context;
        this.content = content;
        setCancelable(cancelable);
        setCanceledOnTouchOutside(outside);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading_dialog);
        ((TextView) (findViewById(R.id.dialog_title))).setText(content);

        if (TextUtils.isEmpty(content)) {
            findViewById(R.id.dialog_title).setVisibility(View.GONE);
        } else {
            findViewById(R.id.dialog_title).setVisibility(View.VISIBLE);
            ((TextView) (findViewById(R.id.dialog_title))).setText(content);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
    }
}
