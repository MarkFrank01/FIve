package com.wjc.parttime.util;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpHeaders;
import com.lzy.okhttputils.request.BaseRequest;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by y_hui on 2018/1/10.
 */

public class OKGOUtil {
    /**
     * 网络访问要求singleton
     */
    private static OKGOUtil instance;

    public static OKGOUtil getInstance() {
        if (instance == null) {
            synchronized (OKGOUtil.class) {
                if (instance == null) {
                    instance = new OKGOUtil();
                }
            }
        }
        return instance;
    }

    /**
     * @param url:请求路径
     * @param parms:要提交到后台的表单
     * @param resultCallback:回调
     * @方法名:PostFormData
     * @return:
     * @描述: 以表单方式发起POST请求
     * @作者： yhui
     * @创建日期 2018/1/10
     */
    public void PostFormData(String url, Map<String, Object> parms, ResultCallback resultCallback) {
        requestPostNetWork(url, parms, null, resultCallback);
    }

    /**
     * @param url:请求路径
     * @param parms:要提交到后台的表单
     * @param header:请求头部
     * @param resultCallback:回调
     * @方法名:PostFormDataWithHeader
     * @return:
     * @描述: 携带请求头部以表单方式发起POST请求
     * @作者： yhui
     * @创建日期 2018/1/10
     */
    public void PostFormDataWithHeader(String url, Map<String, Object> parms, HttpHeaders header, ResultCallback resultCallback) {
        requestPostNetWork(url, parms, header, resultCallback);
    }

    /**
     * @param url:请求路径
     * @param resultCallback:回调
     * @方法名:GetData
     * @return:
     * @描述: 无参数GET请求
     * @作者： yhui
     * @创建日期 2018/1/10
     */
    public void GetData(String url, ResultCallback resultCallback) {
        requestGetNetWork(url, null, resultCallback);
    }

    /**
     * @param url:请求路径
     * @param header:请求头部
     * @param resultCallback:回调
     * @方法名:GetDataWithHeader
     * @return:
     * @描述: 携带请求头部无参数GET请求
     * @作者： yhui
     * @创建日期 2018/1/10
     */
    public void GetDataWithHeader(String url, HttpHeaders header, ResultCallback resultCallback) {
        requestGetNetWork(url, header, resultCallback);
    }

    /**
     * @param url:请求路径
     * @param parms:要提交到后台的表单
     * @param header:请求头部（可为空）
     * @param resultCallback:回调
     * @方法名:requestPostNetWork
     * @return:
     * @描述: 以表单方式发起POST请求具体实现
     * @作者： yhui
     * @创建日期 2018/1/10
     */
    private void requestPostNetWork(String url, Map<String, Object> parms, HttpHeaders header, final ResultCallback resultCallback) {
        String json = new Gson().toJson(parms);
        if (parms != null && !TextUtils.isEmpty(url)) {
            LogUtil.e("OKGOUtil", "requestPostNetWork");
            OkHttpUtils.post(url)
                    .headers(header)
                    .upJson(json)
                    .execute(new StringCallback() {
                        @Override
                        public void onBefore(BaseRequest request) {
                            resultCallback.onBefore(request);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            resultCallback.onSuccess(s, call, response);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            resultCallback.onError(call, response, e);
                            super.onError(call, response, e);
                        }

                        @Override
                        public void onAfter(@Nullable String s, @Nullable Exception e) {
                            resultCallback.onAfter(s, e);

                        }
                    });
        }
    }

    /**
     * @param url:请求路径
     * @param header:请求头部（可为空）
     * @param resultCallback:回调
     * @方法名:requestGetNetWork
     * @return:
     * @描述: GET请求具体实现
     * @作者： yhui
     * @创建日期 2018/1/10
     */
    private void requestGetNetWork(String url, HttpHeaders header, final ResultCallback resultCallback) {
        if (TextUtils.isEmpty(url)) {
            OkHttpUtils.get(url)
                    .headers(header)
                    .execute(new StringCallback() {
                        @Override
                        public void onBefore(BaseRequest request) {
                            resultCallback.onBefore(request);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            resultCallback.onSuccess(s, call, response);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            resultCallback.onError(call, response, e);
                        }

                        @Override
                        public void onAfter(@Nullable String s, @Nullable Exception e) {
                            resultCallback.onAfter(s, e);
                        }
                    });
        }
    }

    /**
     * @方法名:ResultCallback
     * @return:
     * @描述: 回调接口
     * @作者： yhui
     * @创建日期 2018/1/10
     */
    public interface ResultCallback {
        void onBefore(BaseRequest request);

        void onSuccess(String s, Call call, Response response);

        void onError(Call call, Response response, Exception e);

        void onAfter(@Nullable String s, @Nullable Exception e);
    }
}
