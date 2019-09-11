package com.qhy.demo.app;


import android.text.TextUtils;

import com.qhy.demo.model.entity.User;
import com.qhy.demo.utils.DES;
import com.qhy.demo.utils.JacksonUtil;
import com.qhy.demo.utils.Logs;
import com.qhy.demo.utils.SharedPreUtils;


/**
 * Created by qhy on 2019/9/9.
 */
public class UserSession {


    private static final String ACCOUNT_DES_KEY = "JYBq40Actoa";

    private UserSession() {
    }

    private static final class UserSessionHolder {
        private static final UserSession mInstance = new UserSession();
    }

    public static UserSession getInstance() {
        return UserSessionHolder.mInstance;
    }


    /**
     * 获取登录用户身份ID
     *
     * @return
     */
    public int getUserId() {
        if (getUser() != null) {
            return getUser().getUserId();
        } else {
            return 0;
        }
    }


    /**
     * 获取登录用户Token
     *
     * @return
     */
    public String getToken() {
        if (getUser() != null) {
            return getUser().getAccessToken();
        } else {
            return "";
        }
    }


    /**
     * 获取用户本地信息
     *
     * @return
     */
    public User getUser() {
        User mLoginUser = getLocalLoginUser();
        if (mLoginUser == null) {
            mLoginUser = new User();
        }
        return mLoginUser;
    }

    /**
     * 更新用户本地信息
     *
     * @param user
     */
    public void setUser(User user) {
        //更新用户本地信息
        if (user != null) {
            saveLocalUserInfo(user);
        }
    }


    /**
     * 判断用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return SharedPreUtils.getBoolean(DemoApplication.getContext(), Constants.USER_LOGIN, false);
    }

    public void logout() {
        SharedPreUtils.cleanAppointKey(Constants.USER_LOGIN);
        SharedPreUtils.cleanAppointKey(Constants.USER_JSON_INFO);
    }


    /**
     * 设置登录状态
     *
     * @param loginStatus
     */
    public void setLoginStatus(boolean loginStatus) {
        SharedPreUtils.saveBoolean(DemoApplication.getContext(), Constants.USER_LOGIN, loginStatus);
    }

    /**
     * 保存登录用户信息到本地
     *
     * @param user 服务器返回的json格式用户数据
     */
    private void saveLocalUserInfo(User user) {
        String userInfo = null;
        try {
            userInfo = JacksonUtil.toJson(user);
            if (!TextUtils.isEmpty(userInfo)) {
                String str = DES.encryptDES(userInfo, ACCOUNT_DES_KEY);
                if (!TextUtils.isEmpty(str)) {
                    SharedPreUtils.saveString(DemoApplication.getContext(), Constants.USER_JSON_INFO, str);
                } else {
                    Logs.d("UserSession", "userinfo decrypt is empty");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存到本地的用户json数据
     *
     * @return
     */
    private User getLocalLoginUser() {
        String cache = SharedPreUtils.getString(DemoApplication.getContext(), Constants.USER_JSON_INFO, "");
        User user = null;
        try {
            if (!TextUtils.isEmpty(cache)) {
                String str = null;
                str = DES.decryptDES(cache, ACCOUNT_DES_KEY);
                if (!TextUtils.isEmpty(str)) {
                    user = JacksonUtil.readValue(str, User.class);
                } else {
                    Logs.d("UserSession", "userinfo decrypt is empty");
                }
            }
            if (user == null) {
                user = new User();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

}
