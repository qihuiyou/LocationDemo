package com.qhy.demo.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.qhy.demo.R;
import com.qhy.demo.app.AppManager;
import com.qhy.demo.base.BaseActivity;
import com.qhy.demo.utils.CommonUtils;
import com.qhy.demo.utils.SystemBarHelper;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by qhy on 2019/9/9.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_login_phone)
    EditText mEtLoginPhone;
    @BindView(R.id.et_login_code)
    EditText mEtLoginCode;
    @BindView(R.id.tv_login_next)
    TextView mTvLoginNext;
    @BindView(R.id.tv_not_paw)
    TextView mTvTitle;
    @BindView(R.id.tv_not_agreement)
    TextView mTvHint;
    @BindView(R.id.rl_login_phone)
    RelativeLayout mRlLoginPhone;
    @BindView(R.id.rl_login_code)
    RelativeLayout mRlLoginCode;
    @BindView(R.id.tv_login_edit)
    TextView mTvLoginEdit;
    @BindView(R.id.rl_login_bottom)
    RelativeLayout mRlLoginBottom;


    private boolean isNext = false;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.changeToLightStatusBar(this);
        hideCommonTitleBar(mTitleParentLayout);
        initEditText();
    }

    private void initEditText() {
        CommonUtils.phoneNumAddSpace(mEtLoginPhone);
        mEtLoginPhone.addTextChangedListener(mTextWatcher);
        mEtLoginCode.addTextChangedListener(mTextWatcher);
    }


    @OnClick(R.id.iv_login_close)
    public void loginClose() {
        AppManager.getAppManager().killActivity(LoginActivity.class);
//        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }


    @SuppressLint("SetTextI18n")
    @OnClick(R.id.tv_login_next)
    public void loginNext() {
        if (!isNext) return;
        mTvTitle.setText("输入验证码");
        mTvHint.setText("验证码已发送至 +86 " + mEtLoginPhone.getText().toString().trim());
        mRlLoginPhone.setVisibility(View.GONE);
        isNext = false;
        mTvLoginNext.setBackgroundResource(R.drawable.bg_gray_circle);
        mTvLoginEdit.setVisibility(View.VISIBLE);
        mRlLoginBottom.setVisibility(View.GONE);
        mRlLoginCode.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.tv_login_edit)
    public void loginEdit() {
        mRlLoginPhone.setVisibility(View.VISIBLE);
        mRlLoginCode.setVisibility(View.GONE);
        mTvTitle.setText("免密登录");
        mTvHint.setText("登录表示同意'用户协议'和'隐私政策'");
        mRlLoginBottom.setVisibility(View.VISIBLE);
    }


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (mRlLoginPhone.getVisibility() == View.VISIBLE) {
                if (s.toString().length() >= 11) {
                    mTvLoginNext.setBackgroundResource(R.drawable.bg_red_circle);
                    isNext = true;
                } else {
                    mTvLoginNext.setBackgroundResource(R.drawable.bg_gray_circle);
                    isNext = false;
                }
            } else {
                if (s.toString().length() >= 4) {
                    mTvLoginNext.setBackgroundResource(R.drawable.bg_red_circle);
                    isNext = true;
                } else {
                    mTvLoginNext.setBackgroundResource(R.drawable.bg_gray_circle);
                    isNext = false;
                }
            }

        }
    };


    private void loadData() {

//        showLoadingDialog();
//        HashMap<String, String> params = new HashMap<>();
//        params.put("epName", "");
//        params.put("provinceId", "");
//        params.put("cityId", "");
//
//        params.put("pageNumb", 1 + "");
//        params.put("pageSize", 10 + "");
//        HttpUtils.getInstance().request(HttpApi.demo, params, new CommonCallback<List<ParkItem>>(ParkItem.class) {
//
//
//            @Override
//            public void onSuccess(List<ParkItem> parkItems) {
//
//                ParkItem parkItem = parkItems.get(2);
//
//                Logger.d(parkItem.getUrl().get(0));
//
//                ImageUtils.load(LoginActivity.this, parkItem.getUrl().get(0), mImageView);
//
//                loadWebView(parkItem.getDetailPageUrl(), parkItem.getName());
//
//                dimissLoadingDialog();
//
//            }
//
//            @Override
//            public void onFailure(int errorCode, @NonNull String errorMessage) {
//                Logger.d("onFailure:   " + errorMessage);
//                dimissLoadingDialog();
//            }
//        });

    }


}
