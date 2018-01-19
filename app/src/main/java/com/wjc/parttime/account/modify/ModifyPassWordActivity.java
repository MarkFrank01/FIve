package com.wjc.parttime.account.modify;

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
import com.wjc.parttime.LitePalHelperDB.LoginHelperDB;
import com.wjc.parttime.R;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.account.reset.MessageCodeCheckActivity;
import com.wjc.parttime.account.reset.ResetPassWordActivity;
import com.wjc.parttime.app.HttpUrl;
import com.wjc.parttime.util.AESCoder;
import com.wjc.parttime.util.CommonDialogUtil;
import com.wjc.parttime.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 修改密码
 * Created by WJC on 2017/12/30 11:17
 * Describe : TODO
 */

public class ModifyPassWordActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.et_modify_old_pwd)
    EditText oldPwd;
    @BindView(R.id.et_modify_pwd)
    EditText newPwd;
    @BindView(R.id.et_modify_pwd_confirm)
    EditText newPwdConfirm;
    @BindView(R.id.tv_modify_forget_pwd)
    TextView forgetPassWd;

    ImageButton back;

    TextView title;

    CommonDialogUtil dialog;

    private Boolean success;

    private String errorMessage; //错误消息

    public static String INTENT_MODIFY_PASSWD="MODIFYPASSWD";

    private String userName="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        ButterKnife.bind(this);
        View bar_ll = findViewById(R.id.ly_modify_bar);
        title = bar_ll.findViewById(R.id.tv_navigation_label);
        title.setText(R.string.modify_title);
        back = bar_ll.findViewById(R.id.ib_navigation_back);
        back.setOnClickListener(this);


        userName=getIntent().getStringExtra(INTENT_MODIFY_PASSWD);

    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, ModifyPassWordActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.bt_modify_submit,R.id.tv_modify_forget_pwd})
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.ib_navigation_back:
               LoginActivity.show(ModifyPassWordActivity.this);
               finish();
               break;
           case R.id.bt_modify_submit:
               //提交
               LoginHelperDB userDB = DataSupport.findFirst(LoginHelperDB.class);
               String token="";
               if (userDB != null) {
                  token = userDB.getToken();
               }
               String oldPasswd=oldPwd.getText().toString().trim();
               String newPasswd=newPwd.getText().toString().trim();
               String passwdConfirm=newPwdConfirm.getText().toString().trim();
               if (TextUtils.isEmpty(oldPasswd)) {
                   Toast.makeText(ModifyPassWordActivity.this, R.string.modify_password_hint, Toast.LENGTH_SHORT).show();
               }else if (TextUtils.isEmpty(newPasswd)) {
                   Toast.makeText(ModifyPassWordActivity.this, R.string.reset_password_hint, Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(passwdConfirm)) {
                   Toast.makeText(ModifyPassWordActivity.this, R.string.reset_password_again_hint, Toast.LENGTH_SHORT).show();
               }else if (!newPasswd.equals(passwdConfirm)){
                   Toast.makeText(ModifyPassWordActivity.this, R.string.reset_password_check, Toast.LENGTH_SHORT).show();
               }else{
                       modifyPassWdRequest(token,userName,oldPasswd,passwdConfirm);
               }
               break;

           case R.id.tv_modify_forget_pwd:
               //重置密码
               Intent resetPassWdIntent=new Intent(ModifyPassWordActivity.this,MessageCodeCheckActivity.class);
               resetPassWdIntent.putExtra(MessageCodeCheckActivity.INTENT_PASSWD_KEY,1);
               startActivity(resetPassWdIntent);
               break;
       }
    }
    /*
   * 重置密码请求
   * */
    private void modifyPassWdRequest(String token,String userName,String oldPassWord,String passWord) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("telephone", userName);
        map.put("oldPassword",AESCoder.encryptAES_ECB(oldPassWord));
        map.put("newPassword", AESCoder.encryptAES_ECB(passWord));
        String json = new Gson().toJson(map);
        OkHttpUtils.post(HttpUrl.MODIFY_PASSWD_URL)
                .headers("t",token)
                .headers("clientType",HttpUrl.CLIENT_TYPE+"")
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtil.e("ModifyActivity", s);
                        JSONObject mObj = null;
                        try {
                            mObj = new JSONObject(s);
                            success = mObj.optBoolean("success");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //修改密码成功
                        if (success) {
                            dialog = new CommonDialogUtil(ModifyPassWordActivity.this, R.style.dialog, "密码修改成功！", "重新登录", "确定", new CommonDialogUtil.OnListener() {
                                @Override
                                public void onCancelclick() {
                                    LoginActivity.show(ModifyPassWordActivity.this);
                                    dialog.dismiss();
                                    finish();
                                }

                                @Override
                                public void onConfirmClick() {
                                    LoginActivity.show(ModifyPassWordActivity.this);
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            dialog.show();
                        } else {
                            //重置密码失败
                            errorMessage=mObj.optString("errorMessage");
                            dialog = new CommonDialogUtil(ModifyPassWordActivity.this, R.style.dialog, errorMessage, "确定", "取消", new CommonDialogUtil.OnListener() {
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
        LoginActivity.show(ModifyPassWordActivity.this);
        finish();
        super.onDestroy();
    }
}
