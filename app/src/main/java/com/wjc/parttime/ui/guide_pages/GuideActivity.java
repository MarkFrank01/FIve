package com.wjc.parttime.ui.guide_pages;

/**
 * Created by WJC on 2017/12/24 16:17
 * Describe : TODO
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.R;
import com.wjc.parttime.util.LogUtil;
import com.wjc.parttime.util.NetworkUtil;
import com.wjc.parttime.widget.SplashView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

public class GuideActivity extends Activity {
    private static final String TAG = GuideActivity.class.getSimpleName();
    @BindView(R.id.banner_guide_background)
    BGABanner mBackgroundBanner;
    @BindView(R.id.banner_guide_foreground)
    BGABanner mForegroundBanner;

    private SharedPreferences preferences;

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
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                GuideActivity.this.overridePendingTransition(R.anim.main_fade_in, R.anim.main_fade_out);
                finish();
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