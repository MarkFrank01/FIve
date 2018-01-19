package com.wjc.parttime.setting;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.wjc.parttime.R;
import com.wjc.parttime.app.HttpUrl;
import com.wjc.parttime.ui.guide_pages.GuideActivity;
import com.wjc.parttime.util.CommonDialogUtil;
import com.wjc.parttime.util.GoToMarketUtil;
import com.wjc.parttime.util.LogUtil;
import com.wjc.parttime.util.VersionMessageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class PersonalSettingAboutUsActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.personal_setting_about_us_version)
    TextView versionCode;
    @BindView(R.id.bt_check_version)
    Button checkVersion;


    ImageButton back;

    TextView title;

    private String TAG = "PersonalSettingAboutUsActivity";

    private Boolean success;

    CommonDialogUtil dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting_about_us);
        ButterKnife.bind(this);
        long currentVersion = VersionMessageUtils.getVersionCode(PersonalSettingAboutUsActivity.this);
        versionCode.setText(String.valueOf(currentVersion));
        View bar_ll = findViewById(R.id.ly_about_us_bar);
        title = bar_ll.findViewById(R.id.tv_navigation_label);
        title.setText("关于我们");
        back = bar_ll.findViewById(R.id.ib_navigation_back);
        back.setOnClickListener(this);

    }

    /**
     * show the PersonalSettingAboutUsActivity activity
     *
     * @param context context
     */
    public static void show(Activity context) {
        Intent intent = new Intent(context, PersonalSettingAboutUsActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.bt_check_version)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;

            case R.id.bt_check_version:
                checkVersion();
                break;
        }
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
                                //检测版本失败
                                dialog = new CommonDialogUtil(PersonalSettingAboutUsActivity.this, R.style.dialog, "版本检测失败，请重试", "确定", new CommonDialogUtil.OnListener() {
                                    @Override
                                    public void onCancelclick() {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onConfirmClick() {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
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

        long currentVersion = VersionMessageUtils.getVersionCode(PersonalSettingAboutUsActivity.this);
        LogUtil.e(TAG, currentVersion + "");
        LogUtil.e(TAG, version + "");
        String confirm = "";
        String cancel = "";
        String message = "";

        if (currentVersion == version) {
            //没有版本更新
            dialog = new CommonDialogUtil(PersonalSettingAboutUsActivity.this, R.style.dialog, "您的是最新版本", "确定", new CommonDialogUtil.OnListener() {
                @Override
                public void onCancelclick() {
                    dialog.dismiss();
                }

                @Override
                public void onConfirmClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();

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
            dialog = new CommonDialogUtil(PersonalSettingAboutUsActivity.this, R.style.dialog, message, confirm, cancel, new CommonDialogUtil.OnListener() {
                @Override
                public void onCancelclick() {
                    //不更新
                    dialog.dismiss();
                }

                @Override
                public void onConfirmClick() {
                    if ("确定".equals(finalConfirm)) {

                    } else if ("去升级".equals(finalConfirm)) {
                        //跳转到应用市场，跳转应用宝
                        GoToMarketUtil.goToMarket(PersonalSettingAboutUsActivity.this, getPackageName());
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }
}
