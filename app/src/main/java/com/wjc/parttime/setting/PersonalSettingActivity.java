package com.wjc.parttime.setting;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mukesh.permissions.AppPermissions;
import com.wjc.parttime.R;
import com.wjc.parttime.util.CommonDialogUtil;
import com.wjc.parttime.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by y_hui on 2018/1/19.
 */

public class PersonalSettingActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.person_setting_rl_clear_cache)
    RelativeLayout clearCache;
    @BindView(R.id.person_setting_rl_suggest)
    RelativeLayout suggest;
    @BindView(R.id.person_setting_rl_about_us)
    RelativeLayout aboutUs;
    @BindView(R.id.person_setting_rl_connect_us)
    RelativeLayout connectUs;
    @BindView(R.id.person_setting_rl_shared)
    RelativeLayout shared;

    ImageButton back;

    TextView title;

    //客服热线
    private String phoneNumber = "18250154818";

    private String TAG = "PersonalSettingActivity";

    CommonDialogUtil dialog;

    String[] allPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE
    };
    public static final int READ_EXTERNAL_STORAGES = 1;
    public static final int WRITE_EXTERNAL_STORAGES = 2;
    public static final int CALL_PHONE = 3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        View bar_ll = findViewById(R.id.ly_setting_bar);
        title = bar_ll.findViewById(R.id.tv_navigation_label);
        title.setText("设置");
        back = bar_ll.findViewById(R.id.ib_navigation_back);
        back.setOnClickListener(this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.person_setting_rl_clear_cache, R.id.person_setting_rl_suggest, R.id.person_setting_rl_about_us, R.id.person_setting_rl_connect_us, R.id.person_setting_rl_shared})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;

            case R.id.person_setting_rl_clear_cache:
                //清理缓存
                dialog = new CommonDialogUtil(PersonalSettingActivity.this, R.style.dialog, "缓存清理成功", "确定", new CommonDialogUtil.OnListener() {
                    @Override
                    public void onCancelclick() {
                    }

                    @Override
                    public void onConfirmClick() {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

            case R.id.person_setting_rl_suggest:
                //建议反馈
                PersonalSettingSuggestActivity.show(PersonalSettingActivity.this);
                break;

            case R.id.person_setting_rl_about_us:
                //关于我们
                PersonalSettingAboutUsActivity.show(PersonalSettingActivity.this);
                break;
            case R.id.person_setting_rl_connect_us:
                //联系我们
                AppPermissions runtimePermission = new AppPermissions(this);
                if (!runtimePermission.hasPermission(Manifest.permission.CALL_PHONE)) {
                    runtimePermission.requestPermission(allPermissions, CALL_PHONE);
                    return;
                } else {
                    callPhone(phoneNumber);
                }
                break;
            case R.id.person_setting_rl_shared:
                //分享
                LogUtil.e(TAG, "点击");
                break;
        }
    }

    /*
    * 拨打电话
    * */
    private void callPhone(final String phoneNumber) {
        dialog = new CommonDialogUtil(PersonalSettingActivity.this, R.style.dialog, "呼叫 " + phoneNumber, "确定","取消", new CommonDialogUtil.OnListener() {
            @Override
            public void onCancelclick() {
                dialog.dismiss();
            }

            @Override
            public void onConfirmClick() {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:" + phoneNumber));
                if (ActivityCompat.checkSelfPermission(PersonalSettingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(call);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGES:
            case WRITE_EXTERNAL_STORAGES:
                break;
            case CALL_PHONE:
                callPhone(phoneNumber);
                break;
            default:
                break;
        }

    }


}
