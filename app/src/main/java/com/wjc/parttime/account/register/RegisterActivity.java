package com.wjc.parttime.account.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.wjc.parttime.LitePalHelperDB.UserHelperDB;
import com.wjc.parttime.R;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.account.modify.ModifyPassWordActivity;
import com.wjc.parttime.account.reset.MessageCodeCheckActivity;
import com.wjc.parttime.account.reset.ResetPassWordActivity;
import com.wjc.parttime.app.HttpUrl;
import com.wjc.parttime.bean.RegisterUsersBean;
import com.wjc.parttime.util.AESCoder;
import com.wjc.parttime.util.CheckPhoneNumberUtil;
import com.wjc.parttime.util.CommonDialogUtil;
import com.wjc.parttime.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 注册账号
 * Created by WJC on 2017/12/22 11:03
 * Describe : TODO
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.et_register_username)
    EditText mUserName;
    @BindView(R.id.et_register_password)
    EditText mUserPassWord;
    @BindView(R.id.et_register_auth_code)
    EditText mAuthCode;
    @BindView(R.id.register_student_iv)
    ImageView mStudentIV;
    @BindView(R.id.register_company_iv)
    ImageView mCompanyIV;
    @BindView(R.id.register_student_tv)
    TextView mStudentTV;
    @BindView(R.id.register_company_tv)
    TextView mCompanyTV;
    @BindView(R.id.tv_register_sms_call)
    TextView mRegisterCode;
    @BindView(R.id.bt_register_submit)
    Button submit;

    ImageButton back;

    TextView title;

    private String TAG = "RegisterActivity";

    private Boolean isCodeCheckTrue = false;//短信验证码是否失效，防止注册攻击

    private String username;//用户名

    private String userAuthCode;


    //用户类型，1 学生  2 企业  0初始值
    private int userType = 1;

    CommonDialogUtil dialog;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //短信发送成功，what值设置为1提交handler处理，将提交按钮置为不可用
                    changeBtnState(true);
                    break;
                case 2:
                    //短信发送失败，what值设置为2提交handler处理，将提交按钮置为不可用
                    changeBtnState(false);
                    dialog = new CommonDialogUtil(RegisterActivity.this, R.style.dialog, "短信获取失败，请重试", "确定", "取消", new CommonDialogUtil.OnListener() {
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
                    isCodeCheckTrue = true;
                    //验证码错误，清除验证码输入框内容
                    dialog = new CommonDialogUtil(RegisterActivity.this, R.style.dialog, "验证码错误，请重新输入", "确定", "取消", new CommonDialogUtil.OnListener() {
                        @Override
                        public void onCancelclick() {
                            //清除验证码输入框内容
                            mAuthCode.setText("");
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirmClick() {
                            //清除验证码输入框内容
                            mAuthCode.setText("");
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_one);
        View bar_ll = findViewById(R.id.ly_retrieve_bar);
        title = bar_ll.findViewById(R.id.tv_navigation_label);
        title.setText(R.string.register_submit);
        back = bar_ll.findViewById(R.id.ib_navigation_back);
        back.setOnClickListener(this);
        ButterKnife.bind(this);
        changeBtnState(false);
    }

    /**
     * show the Register activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    /**
     * show the Register activity
     *
     * @param context context
     */
    public static void show(Activity context, int requestCode) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * show the Register activity
     *
     * @param fragment fragment
     */
    public static void show(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), RegisterActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    @OnClick({R.id.et_register_username, R.id.et_register_password, R.id.register_student, R.id.register_company, R.id.tv_register_sms_call, R.id.bt_register_submit, R.id.ib_navigation_back
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.register_student:
                LogUtil.e("order", "点击学生");
                userType = 1;
                mStudentIV.setImageDrawable(getResources().getDrawable(R.drawable.student_select));
                mCompanyIV.setImageDrawable(getResources().getDrawable(R.drawable.company_unselect));
                mStudentTV.setTextColor(getResources().getColor(R.color.app_text_blue));
                mCompanyTV.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case R.id.register_company:
                LogUtil.e("order", "点击企业");
                mStudentIV.setImageDrawable(getResources().getDrawable(R.drawable.student_unselect));
                mCompanyIV.setImageDrawable(getResources().getDrawable(R.drawable.company_select));
                mStudentTV.setTextColor(getResources().getColor(R.color.colorBlack));
                mCompanyTV.setTextColor(getResources().getColor(R.color.app_text_blue));
                userType = 2;
                break;

            case R.id.tv_register_sms_call:
                //发送短信验证码
                username = mUserName.getText().toString().trim();
                sendSMS(username);
                break;

            case R.id.bt_register_submit:
                //校验短信验证码,通过后向后台发送注册请求
                username = mUserName.getText().toString().trim();
                userAuthCode = mAuthCode.getText().toString().trim();
                submitCode("86", username, userAuthCode);
                break;
        }
    }

    /*
    * 注册请求
    * */
    private void RegisterRequest(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("telephone", username);
        map.put("password", AESCoder.encryptAES_ECB(password));
        map.put("userType", userType + "");
        map.put("clientType", HttpUrl.CLIENT_TYPE);
        String json = new Gson().toJson(map);

        OkHttpUtils.post(HttpUrl.REGISTER_URL)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtil.e("RegisterActivity", s);

                        Gson gson = new Gson();
                        RegisterUsersBean user = gson.fromJson(s, RegisterUsersBean.class);
                        LogUtil.e("orderuser", user + "");
                        Boolean success = user.isSuccess();
                        //注册成功
                        if (success) {
                            //提交成功，短信验证码置为失效，防止攻击
                            isCodeCheckTrue = false;
                            //保存数据库
                            UserHelperDB person = new UserHelperDB();
                            person.setcreateDate(user.getResult().getUser().getcreateDate());
                            person.setToken(user.getResult().getToken());
                            person.setUserId(user.getResult().getUser().getUserid());
                            person.setTelePhone(user.getResult().getUser().getTelephone());
                            person.setPassWord(user.getResult().getUser().getPassword());
                            person.setUserType(user.getResult().getUser().getUsertype());
                            person.setStudentId(user.getResult().getUser().getStudentid());
                            person.save();
                            dialog = new CommonDialogUtil(RegisterActivity.this, R.style.dialog, "恭喜您注册成功！", "设置实名认证", "登录账号", new CommonDialogUtil.OnListener() {
                                @Override
                                public void onCancelclick() {
                                    LoginActivity.show(RegisterActivity.this);
                                    dialog.dismiss();
                                    finish();
                                }

                                @Override
                                public void onConfirmClick() {

                                }
                            });
                            dialog.show();
                        } else {
                            String errorMessage = user.getErrorMessage();
                            //注册失败
                            dialog = new CommonDialogUtil(RegisterActivity.this, R.style.dialog, errorMessage, "确定", "取消", new CommonDialogUtil.OnListener() {
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
    * 发送短信验证码
    * */
    private void sendSMS(String userAccount) {
        //将提交按钮置为不可用
        changeBtnState(false);
        if (TextUtils.isEmpty(userAccount)) {
            Toast.makeText(RegisterActivity.this, R.string.reset_account, Toast.LENGTH_SHORT).show();
        } else if (!CheckPhoneNumberUtil.FormatCheckForPhone(userAccount)) {
            LogUtil.e(TAG, "手机号码验证不通过");
            dialog = new CommonDialogUtil(RegisterActivity.this, R.style.dialog, "手机号码格式错误，请重新输入", "确定", new CommonDialogUtil.OnListener() {
                @Override
                public void onCancelclick() {
                    //清除账号输入框内容
                    mUserName.setText("");
                    dialog.dismiss();
                }

                @Override
                public void onConfirmClick() {
                    //清除验证码输入框内容
                    mUserName.setText("");
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
                    LogUtil.e(TAG, "短信发送失败");
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
                    isCodeCheckTrue = true;
                    if (isCodeCheckTrue) {
                        String password = mUserPassWord.getText().toString().trim();
                        RegisterRequest(username, password);
                    }
                } else {
                    // TODO 处理错误的结果
                    isCodeCheckTrue = false;
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
}
