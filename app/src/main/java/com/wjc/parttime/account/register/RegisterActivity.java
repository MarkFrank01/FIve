package com.wjc.parttime.account.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.wjc.parttime.LitePalHelperDB.UserHelperDB;
import com.wjc.parttime.R;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.bean.UsersBean;
import com.wjc.parttime.util.CommonDialogUtil;
import com.wjc.parttime.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by WJC on 2017/12/22 11:03
 * Describe : TODO
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.et_register_username)
    EditText mUserName;
    @BindView(R.id.et_register_password)
    EditText mUserPassWord;
    @BindView(R.id.register_student_iv)
    ImageView mStudentIV;
    @BindView(R.id.register_company_iv)
    ImageView mCompanyIV;
    @BindView(R.id.register_student_tv)
    TextView mStudentTV;
    @BindView(R.id.register_company_tv)
    TextView mCompanyTV;
    @BindView(R.id.bt_register_submit)
    Button submit;

    ImageButton back;

    TextView title;



    //用户类型，1 学生  2 企业  0初始值
    private int userType = 0;

    CommonDialogUtil dialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_one);
        View bar_ll=findViewById(R.id.ly_retrieve_bar);
        title=bar_ll.findViewById(R.id.tv_navigation_label);
        title.setText(R.string.register_submit);
        back=bar_ll.findViewById(R.id.ib_navigation_back);
        back.setOnClickListener(this);
        ButterKnife.bind(this);
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

    @OnClick({R.id.et_register_username, R.id.et_register_password, R.id.register_student, R.id.register_company, R.id.bt_register_submit,R.id.ib_navigation_back
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

            case R.id.bt_register_submit:
                LogUtil.e("order", "点击确定");
                /*if (userType==0){
                    Toast.makeText(this,"请选择用户类型",Toast.LENGTH_SHORT).show();
                }else{*/
                String url = "http://120.79.40.105:8080/job-serviceweb/service/mapi/common/register";
                //向后台发送请求
                String password = mUserPassWord.getText().toString().trim();
                String username = mUserName.getText().toString().trim();
                Map<String, Object> map = new HashMap<String, Object>();

                map.put("telephone", username);
                map.put("password", password);
                map.put("clientType", userType + "");

                String json = new Gson().toJson(map);
                OkHttpUtils.post(url)
                        .tag(this)
                        .upJson(json)
                        .execute(new StringCallback() {

                            @Override
                            public void onBefore(BaseRequest request) {

                            }

                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                                LogUtil.e("order", s);

                                Gson gson = new Gson();

                                UsersBean user = gson.fromJson(s, UsersBean.class);
                                LogUtil.e("orderuser", user+"");
                                Boolean success = user.isSuccess();
                                //注册成功
                                if (success) {
                                    //保存数据库
                                    UserHelperDB person = new UserHelperDB();
                                    //String sdate=();
                                    String ss=user.getResult().getUser().getcreateDate();
                                    LogUtil.e("ordertime", ss+"");
                                    person.setcreateDate(user.getResult().getUser().getcreateDate());
                                  //  person.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(user.getResult().getUser().getCreateTime()));
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
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
                //  }
                break;
        }

    }
}
