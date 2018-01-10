package com.wjc.parttime.LitePalHelperDB;

import org.litepal.crud.DataSupport;

/**
 * 当前登录用户表
 * Created by y_hui on 2018/1/8.
 */

public class LoginHelperDB extends DataSupport {

    private String userName;

    private String token;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
