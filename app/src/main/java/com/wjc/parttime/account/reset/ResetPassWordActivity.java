package com.wjc.parttime.account.reset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.wjc.parttime.R;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.account.register.RegisterActivity;
import com.wjc.parttime.app.HttpUrl;
import com.wjc.parttime.util.AESCoder;
import com.wjc.parttime.util.CommonDialogUtil;
import com.wjc.parttime.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 重置密码
 * Created by WJC on 2017/12/30 11:17
 * Describe : TODO
 */

public class ResetPassWordActivity extends AppCompatActivity implements View.OnClickListener{
     @BindView(R.id.et_reset_pwd)
    EditText newPwd;
    @BindView(R.id.et_reset_pwd_confirm)
    EditText newPwdConfirm;

    ImageButton back;

    TextView title;

    CommonDialogUtil dialog;

    private Boolean success;

    public static String INTENT_RESET_PASSWD="RESETPASSWD";

    private String userName="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        View bar_ll = findViewById(R.id.ly_reset_bar);
        title = bar_ll.findViewById(R.id.tv_navigation_label);
        title.setText(R.string.reset_title);
        back = bar_ll.findViewById(R.id.ib_navigation_back);
        back.setOnClickListener(this);
        ButterKnife.bind(this);

        userName=getIntent().getStringExtra(INTENT_RESET_PASSWD);

    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, ResetPassWordActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.bt_reset_submit})
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.ib_navigation_back:
               LoginActivity.show(ResetPassWordActivity.this);
               finish();
               break;
           case R.id.bt_reset_submit:
               //提交
               String newPasswd=newPwd.getText().toString().trim();
               String passwdConfirm=newPwdConfirm.getText().toString().trim();
               if (TextUtils.isEmpty(newPasswd)) {
                   Toast.makeText(ResetPassWordActivity.this, R.string.reset_password_hint, Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(passwdConfirm)) {
                   Toast.makeText(ResetPassWordActivity.this, R.string.reset_password_again_hint, Toast.LENGTH_SHORT).show();
               }else if (!newPasswd.equals(passwdConfirm)){
                   Toast.makeText(ResetPassWordActivity.this, R.string.reset_password_check, Toast.LENGTH_SHORT).show();
               }else{
                       ResetPassWdRequest(passwdConfirm,userName);
               }
               break;
       }
    }
    /*
   * 重置密码请求
   * */
    private void ResetPassWdRequest(String password,String userName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("telephone", userName);
        map.put("newPassword", AESCoder.encryptAES_ECB(password));
        String json = new Gson().toJson(map);
        OkHttpUtils.post(HttpUrl.RESET_PASSWD_URL)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtil.e("RegisterActivity", s);
                        JSONObject mObj = null;
                        try {
                            mObj = new JSONObject(s);
                            success = mObj.optBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //重置密码成功
                        if (success) {
                            dialog = new CommonDialogUtil(ResetPassWordActivity.this, R.style.dialog, "密码重置成功！", "重新登录", "确定", new CommonDialogUtil.OnListener() {
                                @Override
                                public void onCancelclick() {
                                    LoginActivity.show(ResetPassWordActivity.this);
                                    dialog.dismiss();
                                    finish();
                                }

                                @Override
                                public void onConfirmClick() {
                                    LoginActivity.show(ResetPassWordActivity.this);
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            dialog.show();
                        } else {
                            //重置密码失败
                            dialog = new CommonDialogUtil(ResetPassWordActivity.this, R.style.dialog, "重置密码失败，请重试", "确定", "取消", new CommonDialogUtil.OnListener() {
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

    @Override
    protected void onDestroy() {
        LoginActivity.show(ResetPassWordActivity.this);
        finish();
        super.onDestroy();
    }
}
