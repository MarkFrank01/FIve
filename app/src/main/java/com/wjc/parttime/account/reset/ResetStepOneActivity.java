package com.wjc.parttime.account.reset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wjc.parttime.R;
import com.wjc.parttime.util.CheckPhoneNumberUtil;
import com.wjc.parttime.util.CommonDialogUtil;
import com.wjc.parttime.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 短信验证
 * Created by WJC on 2017/12/30 11:16
 * Describe : TODO
 */

public class ResetStepOneActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_reset_account)
    EditText account;
    @BindView(R.id.et_reset_auth_code)
    EditText authCode;
    @BindView(R.id.bt_reset_submit)
    Button submit;

    //跳转KEY
    public static String INTENT_PASSWD_KEY = "PASSWORD";
    //密码value
    public static int INTENT_PASSWD_VALUE = 0;
    //用户账号
    private String userAccount;
    //用户短信验证码
    private String userAuthCode;

    ImageButton back;

    TextView title;


    private CommonDialogUtil dialog;

    private String TAG="ResetStepOneActivity";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //短信发送成功，what值设置为1提交handler处理，将提交按钮置为可用
                    changeBtnState(true);
                    break;
                case 2:
                    //短信发送失败，what值设置为2提交handler处理，将提交按钮置为不可用
                    changeBtnState(false);
                    dialog = new CommonDialogUtil(ResetStepOneActivity.this, R.style.dialog, "短信获取失败，请重试", "确定", "取消", new CommonDialogUtil.OnListener() {
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
                    break;
                case 3:
                    //验证码错误，清除验证码输入框内容
                    dialog = new CommonDialogUtil(ResetStepOneActivity.this, R.style.dialog, "验证码错误，请重新输入", "确定", "取消", new CommonDialogUtil.OnListener() {
                        @Override
                        public void onCancelclick() {
                            //清除验证码输入框内容
                            authCode.setText("");
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirmClick() {
                            //清除验证码输入框内容
                            authCode.setText("");
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    break;
            }
            super.handleMessage(msg);
        }

    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_getmes);
        INTENT_PASSWD_VALUE = getIntent().getIntExtra(INTENT_PASSWD_KEY, 0);
        ButterKnife.bind(this);
        //将提交按钮置为不可用
        changeBtnState(false);
        View bar_ll = findViewById(R.id.ly_reset_bar);
        title = bar_ll.findViewById(R.id.tv_navigation_label);
        title.setText(R.string.reset_msg_hint);
        back = bar_ll.findViewById(R.id.ib_navigation_back);
        back.setOnClickListener(this);
    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, ResetStepOneActivity.class);
        context.startActivity(intent);
    }

    private void changeBtnState(Boolean isEnable) {
        if (isEnable) {
            //将提交按钮置为可用
            submit.setEnabled(true);
            submit.setClickable(true);
            submit.setBackground(getResources().getDrawable(R.drawable.bg_btn_pressed));
        } else {
            //将提交按钮置为不可用
            submit.setEnabled(false);
            submit.setClickable(false);
            submit.setBackground(getResources().getDrawable(R.drawable.bg_btn_normal));
        }
    }

    @OnClick({R.id.tv_reset_sms_call, R.id.bt_reset_submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reset_sms_call:
                //发送短信验证码
                userAccount = account.getText().toString().trim();
                if (TextUtils.isEmpty(userAccount)) {
                    Toast.makeText(ResetStepOneActivity.this, R.string.reset_account, Toast.LENGTH_SHORT).show();
                    //将提交按钮置为不可用
                    changeBtnState(false);
                } else if (!CheckPhoneNumberUtil.FormatCheckForPhone(userAccount)) {
                    LogUtil.e(TAG, "手机号码验证不通过");
                    dialog = new CommonDialogUtil(ResetStepOneActivity.this, R.style.dialog, "手机号码格式错误，请重新输入", "确定", new CommonDialogUtil.OnListener() {
                        @Override
                        public void onCancelclick() {
                            //清除账号输入框内容
                            account.setText("");
                            dialog.dismiss();
                        }
                        @Override
                        public void onConfirmClick() {
                            //清除验证码输入框内容
                            account.setText("");
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    //将提交按钮置为不可用
                    changeBtnState(false);
                } else {
                    LogUtil.e(TAG, "手机号码验证通过");
                    sendCode("86", userAccount);
                }
                break;

            case R.id.bt_reset_submit:
                //提交Mob凭条验证
                userAuthCode = authCode.getText().toString().trim();
                if (TextUtils.isEmpty(userAuthCode)) {
                    Toast.makeText(ResetStepOneActivity.this, R.string.reset_auth_code, Toast.LENGTH_SHORT).show();
                    //将提交按钮置为不可用
                    changeBtnState(false);
                } else {
                    submitCode("86", userAccount, userAuthCode);
                }
                break;

            case R.id.ib_navigation_back:
                finish();
                break;
        }

    }


    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    //短信发送成功，what值设置为1提交handler处理，将提交按钮置为可用
                    LogUtil.e(TAG, "短信发送成功");
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } else {
                    // TODO 处理错误的结果
                    //短信发送失败，将提交按钮置为不可用
                    LogUtil.e(TAG, "短信发送成功");
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
                // 用完回调要注销，否则会造成泄露
                SMSSDK.unregisterEventHandler(this);
            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    LogUtil.e(TAG, "验证成功");
                    Intent intent = new Intent(ResetStepOneActivity.this, ResetStepTwoActivity.class);
                    if (INTENT_PASSWD_VALUE == 1) {
                        //重置密码
                        intent.putExtra(ResetStepTwoActivity.INTENT_RESET_PASSWD, userAccount);
                    } else {
                        //修改密码
                        //   intent.putExtra(ResetStepTwoActivity.INTENT_RESET_PASSWD,userAccount);
                    }
                    startActivity(intent);

                } else {
                    // TODO 处理错误的结果
                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessage(message);
                }
                // 用完回调要注销，否则会造成泄露
                SMSSDK.unregisterEventHandler(this);
            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }


}
