package com.wjc.parttime.ui.guide_pages;

/**
 * 引导页及广告页
 * Created by WJC on 2017/12/24 16:17
 * Describe : TODO
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.mukesh.permissions.AppPermissions;
import com.wjc.parttime.LitePalHelperDB.AdverstingHelperDB;
import com.wjc.parttime.LitePalHelperDB.LoginHelperDB;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.R;
import com.wjc.parttime.app.HttpUrl;
import com.wjc.parttime.bean.RegisterUsersBean;
import com.wjc.parttime.util.CommonDialogUtil;
import com.wjc.parttime.util.GoToMarketUtil;
import com.wjc.parttime.util.LogUtil;
import com.wjc.parttime.util.NetworkUtil;
import com.wjc.parttime.util.VersionMessageUtils;
import com.wjc.parttime.widget.SplashView;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private Boolean success = false;//返回结果是否成功

    private Boolean isAutoLogin = false; //是否自动登录临时数据

    private String imgPath="";

    ArrayList<AdverstingHelperDB> adList = new ArrayList(); //广告列表临时数据

    private CommonDialogUtil dialog;

    String[] allPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    public static final int READ_EXTERNAL_STORAGES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        //自动登录请求
        autoLogin();
        //版本检测
        checkVersion();
        //引导页
        setListener();
        //  init();

    }

    /*
    * 版本升级检测
    * */
    private void checkVersion() {

        OkHttpUtils.post(HttpUrl.UPDATE_VERSION_URL)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtil.e("update", s);
                        JSONObject mObj = null;
                        try {
                            mObj = new JSONObject(s);
                            success = mObj.optBoolean("success");

                            //版本请求成功
                            if (success) {
                                long version = Long.parseLong(mObj.getJSONObject("result").optString("mostNewVersion"));
                                Boolean must = mObj.getJSONObject("result").optBoolean("update");
                                LogUtil.e(TAG, version + "");
                                update(version, must);

                            } else {
                                //检测版本失败，直接进入广告
                                init();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }

                    @Override
                    public void onAfter(@Nullable String s, @Nullable Exception e) {
                    }
                });
    }

    /*
    * 版本升级操作
    * */
    private void update(long version, Boolean isMust) {

        long currentVersion = VersionMessageUtils.getVersionCode(GuideActivity.this);
        LogUtil.e(TAG, currentVersion + "");
        LogUtil.e(TAG, version + "");
        String confirm = "";
        String cancel = "";
        String message = "";

        if (currentVersion == version) {
            //没有版本更新，直接进入广告
            init();
        } else {
            if (currentVersion < version) {
                message = "检测到新的软件版本";
                if (isMust) {
                    cancel = "";
                    confirm = "去升级";
                } else {
                    confirm = "去升级";
                    cancel = "取消";
                }
            } else {
                confirm = "确定";
                message = "版本出错";
            }


            final String finalConfirm = confirm;
            dialog = new CommonDialogUtil(GuideActivity.this, R.style.dialog, message, confirm, cancel, new CommonDialogUtil.OnListener() {
                @Override
                public void onCancelclick() {
                    //不更新，直接进入广告
                    init();
                    dialog.dismiss();
                }

                @Override
                public void onConfirmClick() {
                    if ("确定".equals(finalConfirm)) {
                        init();
                    } else if ("去升级".equals(finalConfirm)) {
                        //跳转到应用市场，跳转应用宝
                        GoToMarketUtil.goToMarket(GuideActivity.this, getPackageName());
                    }

                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }

    /*
    * 检测是否是第一次打开，如果是第一次false则到引导页，否则true到显示广告
    * */
    private void init() {
        preferences = getSharedPreferences("CreateApp", Context.MODE_PRIVATE);
        Boolean firstOpen = preferences.getBoolean("firstOpen", false);
        if (firstOpen) {
            //自动登录请求
            // autoLogin();
            //广告数据请求
            adverstingSelect();
        } else {
            //跳转引导页
            processLogic();
        }
    }

    /*
    * 广告数据查询
    * */
    private void adverstingSelect() {
        int numDisplay = 0;

        List<AdverstingHelperDB> ads = DataSupport.findAll(AdverstingHelperDB.class);
        if (ads.size() > 0) {
            for (int i = 0; i < ads.size(); i++) {
                AdverstingHelperDB adverstingHelper = new AdverstingHelperDB();
                adverstingHelper.setStartTime(ads.get(i).getStartTime());
                adverstingHelper.setEndTime(ads.get(i).getEndTime());
                adverstingHelper.setAdType(ads.get(i).getAdType());
                adverstingHelper.setDisplayType(ads.get(i).getDisplayType());
                adverstingHelper.setAdUrl(ads.get(i).getAdUrl());
                adverstingHelper.setActionUrl(ads.get(i).getActionUrl());
                adverstingHelper.setImgPath(ads.get(i).getImgPath());
                adList.add(adverstingHelper);
            }
            AppPermissions runtimePermission = new AppPermissions(this);
            if (!runtimePermission.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                runtimePermission.requestPermission(allPermissions, READ_EXTERNAL_STORAGES);
                return;
            } else {
                //获取当前系统时间
                Date currentDate = new Date();
                //定义时间的格式
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                String currentTime = fmt.format(currentDate);

                SharedPreferences preferences = getSharedPreferences("adHoliday", Context.MODE_PRIVATE);
                Boolean holidayDisplay = preferences.getBoolean("holidayDisplay", false);
                String holidaytime = preferences.getString("holidaytime", currentTime);

                if (adList.size() == 1) {
                    numDisplay = 0;
                } else if (adList.size() == 2 && !inTime(holidaytime, holidaytime)) {
                    //类型为H且时间不是今天
                    LogUtil.e("adversiting", "方法1");
                    numDisplay = selectAdNumber("H", adList);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("holidayDisplay", true);
                    editor.putString("holidaytime", currentTime);
                    editor.commit();
                } else if (adList.size() == 2 && inTime(holidaytime, holidaytime) && !holidayDisplay) {
                    LogUtil.e("adversiting", "方法2");
                    numDisplay = selectAdNumber("H", adList);
                    SharedPreferences.Editor editor1 = preferences.edit();
                    editor1.putBoolean("holidayDisplay", true);
                    editor1.putString("holidaytime", currentTime);
                    editor1.commit();
                } else {
                    LogUtil.e("adversiting", "方法3");
                    numDisplay = selectAdNumber("A", adList);
                }
                final String adUrl = adList.get(numDisplay).getAdUrl();
                imgPath = adList.get(numDisplay).getImgPath();
                Boolean isiImgExist = SplashView.isFileExist(imgPath);
                String beginTime = adList.get(numDisplay).getStartTime();
                String endTime = adList.get(numDisplay).getEndTime();
                String type = adList.get(numDisplay).getAdType();
                String displayType = adList.get(numDisplay).getDisplayType();
                String actionUrl = adList.get(numDisplay).getActionUrl();
                //文件是否存在
                if (isiImgExist && inTime(beginTime, endTime)) {
                    if ("P".equalsIgnoreCase(displayType)) {
                        //广告类型为图片
                        showSplashView(imgPath, actionUrl,imgPath);
                    } else if ("V".equalsIgnoreCase(displayType)) {
                        //广告类型为视频


                    }
                } else {
                    LogUtil.e("adversitingFile:", "文件不存在");
                    gotoLogin();
                }
            }
        } else {
            gotoLogin();
            //没有读取权限
            // SplashView.updateSplashData(this, "http://ww2.sinaimg.cn/large/72f96cbagw1f5mxjtl6htj20g00sg0vn.jpg", "http://bbc.com");
            //      SplashView.updateSplashData(this, adList.get(numDisplay).getAdUrl(), adList.get(numDisplay).getActionUrl());
        }
    }

    /*
    * 引导页跳过和开启体验按钮监听
    * */
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
                //进入广告
                adverstingSelect();
            }
        });
    }


    /**
     * 广告页展示，第一个参数上下文，第二个参数为倒计时时间，第三个为加载不出来时默认图片
     * show the SplashView
     */
    private void showSplashView(String url, String actionUrl,String imgPath) {
        // call after setContentView(R.layout.activity_login);
        SplashView.showSplashView(this, 3, R.mipmap.five,imgPath, new SplashView.OnSplashViewActionListener() {
            @Override
            public void onSplashImageClick(String actionUrl) {
                LogUtil.e("SplashView", "img clicked. actionUrl: " + actionUrl);
                //跳转商家广告
            }

            @Override
            public void onSplashViewDismiss(boolean initiativeDismiss) {
                LogUtil.e("SplashView", "dismissed, initiativeDismiss: " + initiativeDismiss);
                gotoLogin();
            }
        });

        if (NetworkUtil.networkConnected(this)) {
            // call this method anywhere to update splash view data
            SplashView.updateSplashData(this, url, actionUrl);
        }
    }

    /*
    * 自动登录是否通过，如果通过跳转主页面，如果不通过跳转登录页
    * */
    private void gotoLogin() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isAutoLogin) {
            LogUtil.e("GuideActivity", "自动登录 ");
            //跳转主页面
            //    finishGuide(MainActivity.class);
        } else {
            finishGuide(LoginActivity.class);
        }
    }

    /*
    * 设置引导页数据来源
    * */
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

    /*
    * 结束当前页面跳转添加动画
    * */
    private void finishGuide(Class newClass) {
        startActivity(new Intent(GuideActivity.this, newClass));
        GuideActivity.this.overridePendingTransition(R.anim.main_fade_in, R.anim.main_fade_out);
        finish();

    }

    /**
     * @param begintime:开始时间
     * @param endtime:结束时间
     * @function:判断当前时间是否处于特定的时间内
     * @return:当前时间是否处于特定的时间内，true为处于
     */
    public static Boolean inTime(String begintime, String endtime) {
        if (TextUtils.isEmpty(begintime) || TextUtils.isEmpty(endtime)) {
            return false;
        } else {
            //结束时间加一天
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, 1);
            //获取当前系统时间
            Date currentTime = new Date();
            //定义时间的格式
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            //起始时间
            Date strBeginDate = null;
            //结束时间
            Date strEndDate = null;
            //结束时间加一天
            Date strEndDates = null;
            try {
                //将时间转化成相同格式的Date类型
                strBeginDate = fmt.parse(begintime);
                strEndDate = fmt.parse(endtime);
                c.setTime(strEndDate);
                c.add(Calendar.DAY_OF_MONTH, 1);
                String endtimes = fmt.format(c.getTime());
                strEndDates = fmt.parse(endtimes);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            LogUtil.e("adversitingFile:", "当前时间戳:" + currentTime.getTime());
            LogUtil.e("adversitingFile:", "开始时间戳:" + strBeginDate.getTime());
            LogUtil.e("adversitingFile:", "结束时间戳:" + strEndDates.getTime());
            //使用getTime方法把时间转化成毫秒数,然后进行比较
            if ((currentTime.getTime() >= strBeginDate.getTime()) && (strEndDates.getTime() >= currentTime.getTime())) {
                return true;
            } else {
                return false;
            }
        }
    }

    /*
    * 广告返回类型为A或者为H时的当前序列位置
    * */
    private int selectAdNumber(String ad, ArrayList<AdverstingHelperDB> list) {
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            if (ad.equals(list.get(i).getAdType())) {
                LogUtil.e("adversitingFile", i + list.get(i).getAdType());
                num = i;
            }
        }
        return num;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGES:
                gotoLogin();
                break;
            default:
                break;
        }

    }
}