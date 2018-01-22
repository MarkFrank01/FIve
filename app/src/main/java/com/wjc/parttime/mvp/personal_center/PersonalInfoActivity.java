package com.wjc.parttime.mvp.personal_center;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjc.parttime.R;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.util.CommonDialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInfoActivity extends Activity implements View.OnClickListener {

    //企业名称
    @BindView(R.id.activity_person_info_enter_name)
    EditText enterName;

    //企业行业和选择企业行业，需要调整
    @BindView(R.id.activity_person_info_enter_industry)
    TextView enterIndustry;
    @BindView(R.id.activity_person_info_enter_industry_choose)
    TextView chooseEnterIndustry;

    //企业地址
    @BindView(R.id.activity_person_info_enter_addr)
    EditText enterAddr;

    //企业简介
    @BindView(R.id.activity_person_info_enter_introduce)
    EditText enterIndtroduce;

    //营业执照
    @BindView(R.id.activity_person_info_enter_business_license)
    ImageView enterBussinessLicense;

    //企业LOGO
    @BindView(R.id.activity_person_info_enter_LOGO)
    ImageView enterLOGO;

    //企业背景墙图片
    @BindView(R.id.activity_person_info_enter_info_bg)
    ImageView enterInfoBG;

    //企业联系方式
    @BindView(R.id.activity_person_info_enter_phone_number)
    EditText enterPhoneNumber;

    //企业认证状态
    @BindView(R.id.activity_person_info_enter_certification_status)
    TextView enterCertificationStatus;

    //企业认证未通过原因，当未通过时才显示
    @BindView(R.id.activity_person_info_enter_certification_case_ll)
    LinearLayout enterCertificationCaseLl;
    @BindView(R.id.activity_person_info_enter_certification_case)
    TextView enterCertificationCase;

    //提交
    @BindView(R.id.activity_person_info_enter_submit)
    Button submit;

    //返回按钮
    ImageButton back;
    //标题
    TextView title;

    //提交成功dialog
    CommonDialogUtil dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        View bar_ll = findViewById(R.id.ly_personal_info_bar);
        title = bar_ll.findViewById(R.id.tv_navigation_label);
        title.setText("企业信息");
        back = bar_ll.findViewById(R.id.ib_navigation_back);
        back.setOnClickListener(this);
        ButterKnife.bind(PersonalInfoActivity.this);
    }

    @OnClick({R.id.activity_person_info_enter_industry_choose,R.id.activity_person_info_enter_business_license,
            R.id.activity_person_info_enter_LOGO,R.id.activity_person_info_enter_info_bg, R.id.activity_person_info_enter_submit}
    )
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_navigation_back:
                //返回按钮
                finish();
                break;

            case R.id.activity_person_info_enter_industry_choose:
                //企业行业选择，暂时有问题,等待行业json

                break;

            case R.id.activity_person_info_enter_business_license:
                //企业营业执照

                break;

            case R.id.activity_person_info_enter_LOGO:
                //企业LOGO

                break;

            case R.id.activity_person_info_enter_info_bg:
                //企业背景墙图片

                break;

            case R.id.activity_person_info_enter_submit:
                //提交按钮（异步请求base64图片上传返回图片id后再请求企业信息上传）


                //保存成功显示dialog（取消按钮可以不传，不传时只显示一个确定按钮，onCancelclick不生效）
                displaySaveSuccess();
                break;
        }
    }

    /*
    * 提交成功弹出dialog
    * */
    private void displaySaveSuccess(){
        //信息上传后台保存成功
        dialog = new CommonDialogUtil(PersonalInfoActivity.this, R.style.dialog, "保存成功", "确定", new CommonDialogUtil.OnListener() {
            @Override
            public void onCancelclick() {
            }

            @Override
            public void onConfirmClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * show the PersonalInfoActivity activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, PersonalInfoActivity.class);
        context.startActivity(intent);
    }

}
