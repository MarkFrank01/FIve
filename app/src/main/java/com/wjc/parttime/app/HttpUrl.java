package com.wjc.parttime.app;

/**
 * Http请求地址
 * Created by y_hui on 2018/1/8.
 */

public class HttpUrl {

    public final static int CLIENT_TYPE = 1;

    public final static String BASE_URL = "http://120.79.40.105:8080/job-serviceweb/service/mapi/";
    //注册
    public final static String REGISTER_URL = BASE_URL + "common/register";
    //自动登录
    public final static String AUTO_LOGIN_URL = BASE_URL + "common/autoLoginByToken";
    //登录
    public final static String LOGIN_URL = BASE_URL + "common/getTokenLogin";
    //重置密码
    public final static String RESET_PASSWD_URL = BASE_URL + "common/resetPassword";

}
