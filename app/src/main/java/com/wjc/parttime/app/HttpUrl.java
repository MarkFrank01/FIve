package com.wjc.parttime.app;

/**
 * Http请求地址
 * Created by y_hui on 2018/1/8.
 */

public class HttpUrl {

    /*
     * 以下为静态数据
     **/

    //客户端使用参数，android为1，ios为2
    public final static int CLIENT_TYPE = 1;
    //广告页图片存储地址
    public final static String ADVERTISING_URL = android.os.Environment.getExternalStorageDirectory().getPath() + "/partTimeAd";




    /*
     * 以下为请求接口URL
     */

    public final static String BASE_URL = "http://120.79.40.105:8080/job-serviceweb/service/mapi/";
    //注册
    public final static String REGISTER_URL = BASE_URL + "common/register";
    //自动登录
    public final static String AUTO_LOGIN_URL = BASE_URL + "common/autoLoginByToken";
    //登录
    public final static String LOGIN_URL = BASE_URL + "common/getTokenLogin";
    //重置密码
    public final static String RESET_PASSWD_URL = BASE_URL + "common/resetPassword";
    //版本更新检测
    public final static String UPDATE_VERSION_URL = BASE_URL + "common/getSysVersion";
    //开屏广告
    public final static String ADVERSITING_URL = BASE_URL + "common/getAppAdvInfo";

}
