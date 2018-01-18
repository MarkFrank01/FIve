package com.wjc.parttime.account.login;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.mob.tools.utils.UIHandler;
import com.mukesh.permissions.AppPermissions;
import com.wjc.parttime.LitePalHelperDB.AdverstingHelperDB;
import com.wjc.parttime.LitePalHelperDB.LoginHelperDB;
import com.wjc.parttime.LitePalHelperDB.UserHelperDB;
import com.wjc.parttime.R;
import com.wjc.parttime.account.register.RegisterActivity;
import com.wjc.parttime.account.reset.ResetStepOneActivity;
import com.wjc.parttime.app.HttpUrl;
import com.wjc.parttime.bean.AdversitingBean;
import com.wjc.parttime.bean.RegisterUsersBean;
import com.wjc.parttime.util.AESCoder;
import com.wjc.parttime.util.CommonDialogUtil;
import com.wjc.parttime.mvp.home.BaseHomeActivity;
import com.wjc.parttime.util.LogUtil;
import com.wjc.parttime.widget.SplashView;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;
import okhttp3.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.R.attr.action;

/**
 * 账号登录
 * Created by WJC on 2017/12/22 11:03
 * Describe : TODO
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener, Handler.Callback {

    @BindView(R.id.et_login_username)
    EditText mLoginUsername;

    @BindView(R.id.et_login_pwd)
    EditText mLoginPassword;

    private PlatformDb platDB;
    private ProgressDialog progressDialog;
    private static final int MSG_ACTION_CCALLBACK = 0;

    CommonDialogUtil dialog;

    AppPermissions runtimePermission = new AppPermissions(LoginActivity.this);
    String[] allPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    public static final int READ_EXTERNAL_STORAGES = 1;
    public static final int WRITE_EXTERNAL_STORAGES = 2;

    //广告图片查询数据库校验值
    private String chackValue = "";
    //广告图片是否存在手机图库里
    private Boolean isImgExist;
    //图片保存名称
    private String IMG_NAME;
    //图片保存名称
    private String IMG_URL;

    private AdversitingBean temporaryAdversiting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // showSplashView();
        //广告获取(临时)
        getAdversiting();

    }

    /*
    * 返回到桌面
    * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

      //  return false;//不执行父类点击事件
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.et_login_username, R.id.et_login_pwd, R.id.bt_login_submit, R.id.tv_login_other_way,
            R.id.bt_login_wx, R.id.bt_login_xl, R.id.tv_login_no_account, R.id.tv_login_forget_pwd})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_submit:
                //登录
                final String userName = mLoginUsername.getText().toString().trim();
                final String userPassWord = mLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, R.string.input_username, Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userPassWord)) {
                    Toast.makeText(LoginActivity.this, R.string.input_password, Toast.LENGTH_SHORT).show();
                } else {
                    LoginRequest(userName,userPassWord);
                }
                //暂时未做处理
                BaseHomeActivity.show(LoginActivity.this);
                break;

            case R.id.et_login_username:
                mLoginPassword.clearFocus();
                mLoginUsername.setFocusableInTouchMode(true);
                mLoginUsername.requestFocus();
                break;

            case R.id.et_login_pwd:
                mLoginUsername.clearFocus();
                mLoginPassword.setFocusableInTouchMode(true);
                mLoginPassword.requestFocus();
                break;

            case R.id.tv_login_no_account:
                //注册
                Toast.makeText(this, "注册", Toast.LENGTH_SHORT).show();
                RegisterActivity.show(LoginActivity.this);
                break;

            case R.id.tv_login_forget_pwd:
                //忘记密码和重置密码共用
                Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
                Intent resetPassWdIntent=new Intent(LoginActivity.this,ResetStepOneActivity.class);
                resetPassWdIntent.putExtra(ResetStepOneActivity.INTENT_PASSWD_KEY,1);
                startActivity(resetPassWdIntent);
                break;

            case R.id.tv_login_other_way:
                //第三方登录
                // QQ登录
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(this);
                qq.SSOSetting(false);
                authorize(qq, 1);
                break;

            case R.id.bt_login_wx:
                // 微信登录
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(this);
                wechat.SSOSetting(false);
                authorize(wechat, 2);
                break;

            case R.id.bt_login_xl:
                //新浪
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                sina.setPlatformActionListener(this);
                sina.SSOSetting(false);
                authorize(sina, 3);
                break;

        }
    }

    /*
    * 登录请求
    * */
    private void LoginRequest(final String userName,final String userPassWord){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("telephone", userName);
        map.put("password", AESCoder.encryptAES_ECB(userPassWord));
        map.put("clientType", HttpUrl.CLIENT_TYPE);
        String json = new Gson().toJson(map);
        OkHttpUtils.post(HttpUrl.LOGIN_URL)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtil.e("LoginActivity", s);

                        Gson gson = new Gson();
                        RegisterUsersBean user = gson.fromJson(s, RegisterUsersBean.class);
                        Boolean success = user.isSuccess();
                        //注册成功
                        if (success) {
                            List<UserHelperDB> userList = DataSupport.where("telephone = ?", userName).find(UserHelperDB.class);
                            if (userList == null) {
                                //保存用户表数据库
                                LogUtil.e("LoginActivity", "保存数据库");
                                UserHelperDB person = new UserHelperDB();
                                person.setcreateDate(user.getResult().getUser().getcreateDate());
                                person.setToken(user.getResult().getToken());
                                person.setUserId(user.getResult().getUser().getUserid());
                                person.setTelePhone(user.getResult().getUser().getTelephone());
                                person.setPassWord(user.getResult().getUser().getPassword());
                                person.setUserType(user.getResult().getUser().getUsertype());
                                person.setStudentId(user.getResult().getUser().getStudentid());
                                person.save();
                            } else {
                                //更新用户表数据库
                                LogUtil.e("LoginActivity", "更新数据库");
                                ContentValues values = new ContentValues();
                                values.put("token", user.getResult().getToken());
                                DataSupport.updateAll(UserHelperDB.class, values, "telePhone = ?", userName);
                            }
                            //删除用户登录表
                            DataSupport.deleteAll(LoginHelperDB.class);
                            //重新保存用户登录表
                            LoginHelperDB loginHelperDB = new LoginHelperDB();
                            loginHelperDB.setUserName(user.getResult().getUser().getTelephone());
                            loginHelperDB.setToken(user.getResult().getToken());
                            loginHelperDB.save();
                            LogUtil.e("LoginActivity", "登录成功");

                        } else {
                            String errorMessage = user.getErrorMessage();
                            //登录失败
                            dialog = new CommonDialogUtil(LoginActivity.this, R.style.dialog, errorMessage, "确定", new CommonDialogUtil.OnListener() {
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


    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Activity context, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * show the login activity
     *
     * @param fragment fragment
     */
    public static void show(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), LoginActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    //一键登录授权
    private void authorize(Platform plat, int type) {
        switch (type) {
            case 1:
                showProgressDialog("正在打开QQ，请稍后...");
                break;
            case 2:
                showProgressDialog("正在打开微信，请稍后...");
                break;
            case 3:
                showProgressDialog("正在打开微博，请稍后...");
                break;
        }
        if (plat.isAuthValid()) { //如果授权就删除授权资料
            plat.removeAccount(true);
        }
        plat.showUser(null);//授权并获取用户信息
    }

    //登录授权成功的回调
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);   //发送消息

    }

    //登陆授权错误的回调
    @Override
    public void onError(Platform platform, int i, Throwable t) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    //登陆授权取消的回调
    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    //登陆发送的handle消息在这里处理
    @Override
    public boolean handleMessage(Message message) {
        hideProgressDialog();
        switch (message.arg1) {
            case 1: { // 成功
                Toast.makeText(LoginActivity.this, "授权登陆成功", Toast.LENGTH_SHORT).show();

                //获取用户资料
                Platform platform = (Platform) message.obj;
                String userId = platform.getDb().getUserId();//获取用户账号
                String userName = platform.getDb().getUserName();//获取用户名字
                String userIcon = platform.getDb().getUserIcon();//获取用户头像
                String userGender = platform.getDb().getUserGender(); //获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
                Toast.makeText(LoginActivity.this, "用户信息为--用户名：" + userName + "  性别：" + userGender, Toast.LENGTH_SHORT).show();

                //下面就可以利用获取的用户信息登录自己的服务器或者做自己想做的事啦!
                //。。。

            }
            break;
            case 2: { // 失败
                Toast.makeText(LoginActivity.this, "授权登陆失败", Toast.LENGTH_SHORT).show();
            }
            break;
            case 3: { // 取消
                Toast.makeText(LoginActivity.this, "授权登陆取消", Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return false;
    }

    //显示dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    //隐藏dialog
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    //临时获取开屏广告
    private void getAdversiting(){
        OkHttpUtils.post(HttpUrl.ADVERSITING_URL)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtil.e("advertising", s);
                        Gson gson =new Gson();
                        AdversitingBean adversitingBean=gson.fromJson(s,AdversitingBean.class);
                        temporaryAdversiting=adversitingBean;
                        chackValue="";
                        if (adversitingBean.isSuccess()){
                            for (int i=0;i<adversitingBean.getResult().size();i++){
                                chackValue=chackValue+adversitingBean.getResult().get(i).getAdvManageID();
                            }
                            //根据校验值查询数据库中图片id进行比对
                            LogUtil.e("adversitingFile:","getAdversiting"+chackValue);
                            findAdversitngDB(chackValue,adversitingBean);
                        }else{
                            LogUtil.e("adversitingFile:", "getAdversiting删除数据库");
                            deleteAdversitingDB(adversitingBean);
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
    * 根据校验值查询数据库中图片id进行比对
    * */
    private void findAdversitngDB(String chackValue,AdversitingBean adversitingBean){
        LogUtil.e("adversitingFile:","findAdversitngDB查询到数据"+chackValue);
        List<AdverstingHelperDB> adList = DataSupport.where("adCheckValue = ?",chackValue).find(AdverstingHelperDB.class);
        if (adList.size()>0){
            LogUtil.e("adversitingFile:","findAdversitngDB查询到数据");
            for (int i = 0; i < adList.size(); i++) {
                if (SplashView.isFileExist(adList.get(i).getImgPath())) {
                    isImgExist = true;
                } else {
                    isImgExist = false;
                }
            }
            if (!isImgExist) {//没有匹配数据
                LogUtil.e("adversitingFile:","findAdversitngDB删除数据库false");
                deleteAdversitingDB(adversitingBean); //删除本地数据库和图片重新下载
            } else {
                LogUtil.e("adversitingFile", "本地匹配到数据");
            }
        }else{
            //没有查询到数据，删除广告表和广告图片
            LogUtil.e("adversitingFile:","findAdversitngDB删除数据库");
            deleteAdversitingDB(adversitingBean);
        }
    }

    /*
    * 删除广告表及广告图片
    * */
    private void deleteAdversitingDB(AdversitingBean adversitingBean){
        //广告数据为空,删除广告表
        LogUtil.e("adversitingFile:","deleteAdversitingDB删除数据库");
        DataSupport.deleteAll(LoginHelperDB.class);
        if (!runtimePermission.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            runtimePermission.requestPermission(allPermissions, WRITE_EXTERNAL_STORAGES);
            return;
        } else {
            //删除广告文件
            LogUtil.e("adversitingFile:", "deleteAdversitingDB删除数据库有权限");
            SplashView.deleteDir(HttpUrl.ADVERTISING_URL + "/");
            LogUtil.e("adversitingFile:", "deleteAdversitingDB保存图片");
            saveAdvertisingFile(adversitingBean);
        }

    }

    //删除广告后重新保存数据库和下载图片
    public void saveAdvertisingFile(AdversitingBean adversitingBean) {
        LogUtil.e("adversitingFile:", "saveAdvertisingFile保存图片");
        if (adversitingBean != null && adversitingBean.getResult().size() > 0) {
            //查询到后，把查询到的数据存储到MyApp内存中
            chackValue = "";
            for (int i=0;i<adversitingBean.getResult().size();i++) {
                chackValue = chackValue + adversitingBean.getResult().get(i).getAdvManageID();
            }
            LogUtil.e("adversitingFile:", "saveAdvertisingFile校验值"+chackValue);
            for (int i=0;i<adversitingBean.getResult().size();i++) {
                if ("V".equalsIgnoreCase(adversitingBean.getResult().get(i).getDisplayType())){
                    IMG_NAME = HttpUrl.ADVERTISING_URL + "/" + adversitingBean.getResult().get(i).getAdvManageID() + ".mp4";
                }else if ("G".equalsIgnoreCase(adversitingBean.getResult().get(i).getDisplayType())){
                    IMG_NAME = HttpUrl.ADVERTISING_URL + "/" + adversitingBean.getResult().get(i).getAdvManageID() + ".gif";
                }else if("P".equalsIgnoreCase(adversitingBean.getResult().get(i).getDisplayType())){
                    IMG_NAME = HttpUrl.ADVERTISING_URL + "/" + adversitingBean.getResult().get(i).getAdvManageID() + ".jpg";
                }

                IMG_URL = adversitingBean.getResult().get(i).getAdUrl();
                LogUtil.e("adversitingFile:", "saveAdvertisingFile图片名称"+IMG_NAME);
                //重新保存保存表
                AdverstingHelperDB adversting=new AdverstingHelperDB();
                adversting.setStartTime(adversitingBean.getResult().get(i).getStartTime());
                adversting.setEndTime(adversitingBean.getResult().get(i).getEndTime());
                adversting.setAdvManageID(adversitingBean.getResult().get(i).getAdvManageID());
                adversting.setAdType(adversitingBean.getResult().get(i).getAdType());
                adversting.setDisplayType(adversitingBean.getResult().get(i).getDisplayType());
                adversting.setAdUrl(IMG_URL);
                adversting.setActionUrl(adversitingBean.getResult().get(i).getActionUrl());
                adversting.setImgPath(IMG_NAME);
                adversting.setAdCheckValue(chackValue);
                adversting.save();
                //获取图片并保存到SD卡
                if (!runtimePermission.hasPermission(WRITE_EXTERNAL_STORAGE)) {
                    runtimePermission.requestPermission(allPermissions, WRITE_EXTERNAL_STORAGES);
                    return;
                } else {
                    LogUtil.e("adversitingFile:", "saveAdvertisingFile有权限，下载图片");

                    if ("V".equalsIgnoreCase(adversitingBean.getResult().get(i).getDisplayType())){
                        SplashView.saveVedioFile(IMG_URL,HttpUrl.ADVERTISING_URL,IMG_NAME);
                    }else{
                        //下载并保存图片到相册。要下载图片的url，图片下载后保存名称，图片下载后文件夹（路径），上下文
                        SplashView.getAndSaveNetWorkBitmap(IMG_URL, IMG_NAME, HttpUrl.ADVERTISING_URL, this);
                    }



                }
            }
        }else{
            LogUtil.e("adversitingFile:", "没有图片下载");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGES:
            case READ_EXTERNAL_STORAGES:
                //删除广告文件
                LogUtil.e("adversitingFile:", "deleteAdversitingDB删除数据库有权限");
                SplashView.deleteDir(HttpUrl.ADVERTISING_URL + "/");
                LogUtil.e("adversitingFile:", "deleteAdversitingDB保存图片");
                saveAdvertisingFile(temporaryAdversiting);
                break;
            default:
                break;
        }
    }



}
