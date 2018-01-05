package com.wjc.parttime.LitePalHelperDB;

import org.litepal.crud.DataSupport;

/**
 * Created by y_hui on 2018/1/3.
 */

public class AdverstingHelperDB extends DataSupport {


    private String imgUrl;

    private String Type;

    private String startTime;

    private String endTime;

    private String AdverstingUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
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

    public String getAdverstingUrl() {
        return AdverstingUrl;
    }

    public void setAdverstingUrl(String adverstingUrl) {
        AdverstingUrl = adverstingUrl;
    }
}
