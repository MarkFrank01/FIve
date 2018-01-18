package com.wjc.parttime.webView;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wjc.parttime.R;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.mvp.home.BaseHomeActivity;


/**
 * @类名: FAQActivity
 * @描述: 信包箱帮助详情
 * @作者 CHENHUI
 * @创建日期 2017/6/8 16:15
 */
public class WebViewActivity extends Activity implements View.OnClickListener {

    private WebView mWebView;

    private TextView webTitle;

    private ImageButton back;

    public static String TITLE_KEY = "web_title"; //标题

    private String titleName;

    public static String CONTENT_KEY = "web_content"; //web Url

    public static String AUTO_KEY="web_auto_login";//是否自动登录

    private String contentUrl = "";

    private Boolean autoLogin=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titleName = getIntent().getStringExtra(TITLE_KEY);
        contentUrl = getIntent().getStringExtra(CONTENT_KEY);
        autoLogin=getIntent().getBooleanExtra(AUTO_KEY,false);
        setContentView(R.layout.activity_webview);

        webTitle = findViewById(R.id.tv_navigation_label);
        if (!TextUtils.isEmpty(contentUrl)) {
            webTitle.setText(titleName);
        }
        back = findViewById(R.id.ib_navigation_back);
        back.setOnClickListener(this);
        init();

    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Context context, String title, String contentUrl) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(CONTENT_KEY, contentUrl);
        context.startActivity(intent);
    }

    protected void init() {

        mWebView = (WebView) findViewById(R.id.wv_view);


        WebSettings settings = mWebView.getSettings();
        //启用支持javascript
        settings.setJavaScriptEnabled(true);
        // 设置编码
        settings.setDefaultTextEncodingName("utf-8");

        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        settings.setDomStorageEnabled(true);

        settings.setGeolocationEnabled(true);

        settings.setDatabaseEnabled(true);

        mWebView.loadUrl(contentUrl);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //两者都可以
            mWebView.getSettings().setMixedContentMode(mWebView.getSettings().getMixedContentMode());
            //mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //   mWebView.getSettings().setGeolocationDatabasePath(dir);
//mWebView.loadUrl("file:///android_asset/user-problem/how-to-register.html");

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {
            //打开网页时不调用系统浏览器， 而是在本WebView中显示；
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (!TextUtils.isEmpty(contentUrl)) {
                    view.loadUrl(url);
                }
                return true;
            }

            //设定加载开始的操作
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            //设定加载结束的操作
            @Override
            public void onPageFinished(WebView view, String url) {

            }

            //加载页面的服务器出现错误时（如404）调用
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                switch (errorCode) {
                    case 404:
                        view.loadUrl("file:///android_assets/error_handle.html");
                        break;
                }
            }
        });

        //加载过程中操作
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                } else {
                    // 加载中
                }
            }

            //获取Web页中的标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                webTitle.setText(title);
            }
        });
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
                return true;
            } else {
                // System.exit(0);//退出程序
                if (autoLogin){
                    //允许自动登录，跳转主页面
                    BaseHomeActivity.show(WebViewActivity.this);
                    finish();
                }else{
                    //自动登录失败，跳转登录页
                    LoginActivity.show(WebViewActivity.this);
                    finish();
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_navigation_back:
                if (autoLogin){
                    //允许自动登录，跳转主页面
                    BaseHomeActivity.show(WebViewActivity.this);
                    finish();
                }else{
                    //自动登录失败，跳转登录页
                    LoginActivity.show(WebViewActivity.this);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        mWebView.onPause();
        mWebView.pauseTimers();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mWebView.onResume();
        mWebView.resumeTimers();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
