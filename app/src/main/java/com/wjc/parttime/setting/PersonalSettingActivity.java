package com.wjc.parttime.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private String TAG = "PersonalSettingActivity";

    CommonDialogUtil dialog;


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
                LogUtil.e(TAG, "点击");
                break;
            case R.id.person_setting_rl_shared:
                //分享
                LogUtil.e(TAG, "点击");
                break;
        }
    }
}
