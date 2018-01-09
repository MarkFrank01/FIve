package com.wjc.parttime.ui.guide_pages;

/**
 * Created by WJC on 2017/12/24 16:17
 * Describe : TODO
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.wjc.parttime.LitePalHelperDB.LoginHelperDB;
import com.wjc.parttime.LitePalHelperDB.UserHelperDB;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.R;
import com.wjc.parttime.app.HttpUrl;
import com.wjc.parttime.bean.RegisterUsersBean;
import com.wjc.parttime.util.AESCoder;
import com.wjc.parttime.util.CommonDialogUtil;
import com.wjc.parttime.util.LogUtil;
import com.wjc.parttime.util.NetworkUtil;
import com.wjc.parttime.widget.SplashView;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.Call;
import okhttp3.Response;

public class GuideActivity extends Activity {
    private static final String TAG = GuideActivity.class.getSimpleName();
    @BindView(R.id.banner_guide_background)
    BGABanner mBackgroundBanner;
    @BindView(R.id.banner_guide_foreground)
    BGABanner mForegroundBanner;

    private SharedPreferences preferences;

    private Boolean isAutoLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        setListener();
        init();

    }

    /*
    * 检测是否是第一次打开，如果是第一次false则到引导页，否则true到显示广告
    * */
    private void init() {
        preferences = getSharedPreferences("CreateApp", Context.MODE_PRIVATE);
        Boolean firstOpen = preferences.getBoolean("firstOpen", false);
        if (firstOpen) {
            showSplashView();
            //自动登录请求
            autoLogin();

        } else {
            processLogic();
        }
    }


    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经处理了防止重复点击事件
         * 在 BGABanner 里已经处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                preferences = getSharedPreferences("CreateApp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstOpen", true);
                editor.commit();
                showSplashView();
            }
        });
    }


    /**
     * show the SplashView
     */
    private void showSplashView() {
        // call after setContentView(R.layout.activity_login);
        SplashView.showSplashView(this, 3, R.mipmap.five, new SplashView.OnSplashViewActionListener() {
            @Override
            public void onSplashImageClick(String actionUrl) {
                LogUtil.e("SplashView", "img clicked. actionUrl: " + actionUrl);
                Toast.makeText(GuideActivity.this, "img clicked.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSplashViewDismiss(boolean initiativeDismiss) {
                LogUtil.e("SplashView", "dismissed, initiativeDismiss: " + initiativeDismiss);
                if (isAutoLogin) {
                    LogUtil.e("GuideActivity", "自动登录 ");
                } else {
                    finishGuide(LoginActivity.class);
                }

            }
        });

        if (NetworkUtil.networkConnected(this)) {
            // call this method anywhere to update splash view data
            SplashView.updateSplashData(this, "http://ww2.sinaimg.cn/large/72f96cbagw1f5mxjtl6htj20g00sg0vn.jpg", "http://bbc.com");
        }
    }

    private void processLogic() {
        // 设置数据源
        mBackgroundBanner.setData(R.mipmap.uoko_guide_background_1, R.mipmap.uoko_guide_background_2, R.mipmap.uoko_guide_background_3);

        mForegroundBanner.setData(R.mipmap.uoko_guide_foreground_1, R.mipmap.uoko_guide_foreground_2, R.mipmap.uoko_guide_foreground_3);
    }

    /*
    * 数据库登录表中没有数据禁止自动登录，有数据请求后台校验
    * */
    private void autoLogin() {
        LoginHelperDB userDB = DataSupport.findFirst(LoginHelperDB.class);
        if (userDB != null) {
            String token = userDB.getToken();
            AutoLogin(token);
        } else {
            isAutoLogin = false;
        }
    }

    /*
    * 请求后台校验token
    *
    * */
    private void AutoLogin(String token) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tokenID", token);
        map.put("clientType", HttpUrl.CLIENT_TYPE);
        String json = new Gson().toJson(map);
        OkHttpUtils.post(HttpUrl.AUTO_LOGIN_URL)
                .tag(this)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtil.e("GuideActivity", s);

                        Gson gson = new Gson();
                        RegisterUsersBean user = gson.fromJson(s, RegisterUsersBean.class);
                        Boolean success = user.isSuccess();
                        if (success) {
                            isAutoLogin = true;
                        } else {
                            isAutoLogin = false;
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        isAutoLogin = false;
                    }

                    @Override
                    public void onAfter(@Nullable String s, @Nullable Exception e) {
                    }
                });

    }

    private void finishGuide(Class newClass) {
        startActivity(new Intent(GuideActivity.this, newClass));
        GuideActivity.this.overridePendingTransition(R.anim.main_fade_in, R.anim.main_fade_out);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }

    @Override
    protected void onDestroy() {
        GuideActivity.this.overridePendingTransition(R.anim.main_fade_finish_in, R.anim.main_fade_finish_out);
        super.onDestroy();
    }
}