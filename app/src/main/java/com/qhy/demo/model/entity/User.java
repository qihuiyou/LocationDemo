package com.qhy.demo.model.entity;

import java.io.Serializable;

/**
 * Created by qhy on 2019/9/9.
 */
public class User implements Serializable {


    /**
     * birthday : 1991-12-16
     * cardFlag : 1002
     * userPict : https://fileserver.paat.com/0ff/0ff7bb5cfef837bd19adc30fbda1d200.jpg
     * recommend : 测试账号，内部员工，别打电话
     * cityId : 310100
     * accessToken : 722342839968262738662488
     * title : 产品研发中心
     * userId : 6305
     * provinceId : 310000
     * approveUserId : 204634
     * sexStr : 男
     * districtId : 310115
     * name : 安卓测试号
     * cityIdStr : 上海城区
     * tel : 19900000170
     * districtIdStr : 浦东新区
     * userRole : 1001
     * provinceIdStr : 上海市
     * activeFlag : 1001
     */

    private String birthday;
    private String cardFlag;
    private String userPict;
    private String recommend;
    private int cityId;
    private String accessToken;
    private String title;
    private int userId;
    private int provinceId;
    private int approveUserId;
    private String sexStr;
    private int districtId;
    private String name;
    private String cityIdStr;
    private String tel;
    private String districtIdStr;
    private int userRole;
    private String provinceIdStr;
    private String activeFlag;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCardFlag() {
        return cardFlag;
    }

    public void setCardFlag(String cardFlag) {
        this.cardFlag = cardFlag;
    }

    public String getUserPict() {
        return userPict;
    }

    public void setUserPict(String userPict) {
        this.userPict = userPict;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(int approveUserId) {
        this.approveUserId = approveUserId;
    }

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityIdStr() {
        return cityIdStr;
    }

    public void setCityIdStr(String cityIdStr) {
        this.cityIdStr = cityIdStr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDistrictIdStr() {
        return districtIdStr;
    }

    public void setDistrictIdStr(String districtIdStr) {
        this.districtIdStr = districtIdStr;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getProvinceIdStr() {
        return provinceIdStr;
    }

    public void setProvinceIdStr(String provinceIdStr) {
        this.provinceIdStr = provinceIdStr;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }
}
