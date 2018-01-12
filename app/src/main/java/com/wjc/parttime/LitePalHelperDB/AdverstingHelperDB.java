package com.wjc.parttime.LitePalHelperDB;

import org.litepal.crud.DataSupport;

/**
 * 广告表
 * Created by y_hui on 2018/1/3.
 */

public class AdverstingHelperDB extends DataSupport {


    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //广告类型：A或H;   A代表正常商家广告，H代表节假日广告
    private String adType;
    //广告展示类型：P或V;  P代表图片，H代表视频
    private String displayType;
    //展示的图片或者视频链接
    private String adUrl;
    //跳转商家地址链接
    private String actionUrl;
    //图片保存路径
    private String imgPath;
    //图片id
    private int advManageID;
    //校验值
    private String adCheckValue;

    public String getAdCheckValue() {
        return adCheckValue;
    }

    public void setAdCheckValue(String adCheckValue) {
        this.adCheckValue = adCheckValue;
    }

    public int getAdvManageID() {
        return advManageID;
    }

    public void setAdvManageID(int advManageID) {
        this.advManageID = advManageID;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
}
