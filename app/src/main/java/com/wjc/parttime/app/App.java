package com.wjc.parttime.app;

import android.database.sqlite.SQLiteDatabase;

import com.lzy.okhttputils.OkHttpUtils;
import com.mob.MobSDK;


import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.io.IOException;


/**
 * Created by WJC on 2017/12/24 11:28
 * Describe :
 */

public class App extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        OKGOinit();



        //ShareSDK初始化
        MobSDK.init(this);

        SQLiteDatabase db = Connector.getDatabase();

    }

    /**
     * @方法名:OKGOinit
     * @参数：
     * @返回值：
     * @描述: OKGO网络请求封装初始化
     * @作者： yhui
     * @创建日期 2018/1/2
     */
    public void OKGOinit() {
        //OKGO必须调用初始化
        OkHttpUtils.init(this);
        //cer证书
        try {
            OkHttpUtils.getInstance().setCertificates(getAssets().open("server.cer"), getAssets().open("server.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //所有的 header 都 不支持 中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //所有的 params 都 支持 中文
        params.put("commonParamsKey2", "这里支持中文参数");*/

        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()
                .debug("OkHttpUtils")                   //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
        //.setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
        //.setCookieStore(new PersistentCookieStore())                       //cookie持久化存储，如果cookie不过期，则一直有效
        //.addCommonHeaders(headers)                                         //设置全局公共头
        // .addCommonParams(params)                                          //设置全局公共参数
        ;
    }





}
