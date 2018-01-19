package com.wjc.parttime.mvp.personal_center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjc.parttime.LitePalHelperDB.LoginHelperDB;
import com.wjc.parttime.R;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.account.reset.MessageCodeCheckActivity;
import com.wjc.parttime.setting.PersonalSettingAboutUsActivity;
import com.wjc.parttime.setting.PersonalSettingActivity;
import com.wjc.parttime.util.CommonDialogUtil;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WJC on 2018/1/11 9:54
 * Describe : TODO
 */

public class PersonalCenterFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.person_fragment_user_icon)
    ImageView mUserIcon;
    @BindView(R.id.person_fragment_user_name)
    TextView mUserName;
    @BindView(R.id.person_fragment_user_kyc_status)
    TextView mKycStatus;

    LinearLayout passwd, setting, loginOut;

    CommonDialogUtil dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        passwd = view.findViewById(R.id.person_fragment_passwd);
        passwd.setOnClickListener(this);
        setting = view.findViewById(R.id.person_fragment_setting);
        setting.setOnClickListener(this);
        loginOut = view.findViewById(R.id.person_fragment_login_out);
        loginOut.setOnClickListener(this);
        ButterKnife.bind(getActivity());
        return view;

    }

    @OnClick({R.id.person_fragment_money, R.id.person_fragment_info, R.id.person_fragment_love, R.id.person_fragment_passwd, R.id.person_fragment_setting, R.id.person_fragment_login_out})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.person_fragment_money:
                //我的钱包

                break;

            case R.id.person_fragment_info:
                //我的信息

                break;

            case R.id.person_fragment_love:
                //我的收藏

                break;

            case R.id.person_fragment_passwd:
                //修改密码
                Intent resetPassWdIntent = new Intent(getActivity(), MessageCodeCheckActivity.class);
                resetPassWdIntent.putExtra(MessageCodeCheckActivity.INTENT_PASSWD_KEY, 2);
                startActivity(resetPassWdIntent);
                break;

            case R.id.person_fragment_setting:
                //设置
                Intent settingIntent = new Intent(getActivity(), PersonalSettingActivity.class);
                startActivity(settingIntent);
                break;

            case R.id.person_fragment_login_out:
                //退出登录
                dialog = new CommonDialogUtil(getActivity(), R.style.dialog, "是否确定退出登录？", "确定","取消", new CommonDialogUtil.OnListener() {
                    @Override
                    public void onCancelclick() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onConfirmClick() {
                        //删除用户登录表
                        DataSupport.deleteAll(LoginHelperDB.class);
                        LoginActivity.show(getActivity());
                        getActivity().finish();
                    }
                });
                dialog.show();
                break;
        }

    }
}
