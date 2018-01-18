package com.wjc.parttime.mvp.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.mukesh.permissions.AppPermissions;
import com.wjc.parttime.LitePalHelperDB.AdverstingHelperDB;
import com.wjc.parttime.LitePalHelperDB.LoginHelperDB;
import com.wjc.parttime.R;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.app.HttpUrl;
import com.wjc.parttime.bean.AdversitingBean;
import com.wjc.parttime.mvp.discovery.DiscoveryFragment;
import com.wjc.parttime.mvp.part_time.PartTimeFragment;
import com.wjc.parttime.mvp.personal_center.PersonalCenterFragment;
import com.wjc.parttime.mvp.testhome.ShoppingFragment;
import com.wjc.parttime.util.ActivityUtils;
import com.wjc.parttime.util.LogUtil;
import com.wjc.parttime.widget.SplashView;
import com.wjc.parttime.widget.rotate_button.OnRotateItemClickListener;
import com.wjc.parttime.widget.rotate_button.RotateMenuItem;
import com.wjc.parttime.widget.rotate_button.RotateMenuView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class BaseHomeActivity extends AppCompatActivity {

    private long exitTime = 0;

    @BindView(R.id.rotate_button)
    RotateMenuView mRotateMenuView;

    //TextView
    @BindView(R.id.tv_home)
    TextView mTvHome;
    @BindView(R.id.tv_part_time)
    TextView mTvPartTime;
    @BindView(R.id.tv_discovery)
    TextView mTvDiscovery;
    @BindView(R.id.tv_mine)
    TextView mTvMine;
    //ImageView
    @BindView(R.id.img_home)
    ImageView mImgHome;
    @BindView(R.id.img_part_time)
    ImageView mImgPartTime;
    @BindView(R.id.img_discovery)
    ImageView mImgDiscovery;
    @BindView(R.id.img_mine)
    ImageView mImgMine;
    //线条
    @BindView(R.id.line_home)
    View mLineHome;
    @BindView(R.id.line_part_time)
    View mLinePartTime;
    @BindView(R.id.line_discovery)
    View mLineDiscovery;
    @BindView(R.id.line_mine)
    View mLineMine;

    //四个Fragment,Home为首页,Part_time为我的兼职,Discovery为发现,Personal_Center为个人中心
//    private HomeFragment mHomeFragment;
    private ShoppingFragment mHomeFragment;
    private PartTimeFragment mPartTimeFragment;
    private DiscoveryFragment mDiscoveryFragment;
    private PersonalCenterFragment mPersonalCenterFragment;

    private FragmentManager fm;

    AppPermissions runtimePermission = new AppPermissions(BaseHomeActivity.this);
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
        setContentView(R.layout.activity_base_home);
        ButterKnife.bind(this);
        fm = getSupportFragmentManager();

        init();
        ShowRotateView();
        //获取广告
        getAdversiting();

    }

    private void init() {
        showFragment(0);
        mImgHome.setBackgroundResource(R.drawable.comui_tab_home_selected);
        mTvHome.setTextColor(getResources().getColor(R.color.color2473));
    }

    //发布按钮
    private void ShowRotateView() {
        RotateMenuItem rotateMenuItem1 = new RotateMenuItem(this);
        rotateMenuItem1.setTopImageRes(R.drawable.home_publish_erwei_ic);
        rotateMenuItem1.setBottomTextstr("兼职信息1");

        RotateMenuItem rotateMenuItem2 = new RotateMenuItem(this);
        rotateMenuItem2.setTopImageRes(R.drawable.home_publish_erwei_ic);
        rotateMenuItem2.setBottomTextstr("兼职信息2");

        List<RotateMenuItem> list = new ArrayList<>();
        list.add(rotateMenuItem1);
        list.add(rotateMenuItem2);


        mRotateMenuView.setCreateItems(list);

        mRotateMenuView.setOnRotateItemClickListner(new OnRotateItemClickListener() {
            @Override
            public void onclickMenu(int i) {
                Toast.makeText(BaseHomeActivity.this, "选项" + (i + 1), Toast.LENGTH_SHORT).show();
                switch (i) {
                    case 0:

                        break;
                    case 1:

                        break;
                }
            }
        });
    }

    /**
     * show the BaseHome activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, BaseHomeActivity.class);
        context.startActivity(intent);
    }

    /**
     * 退出应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                //判断2次点击事件的时间
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(BaseHomeActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    ActivityUtils.finishAll();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.parent_home, R.id.parent_part_time, R.id.parent_discovery, R.id.parent_mine})
    public void onViewClicked(View view) {
        reSetBackground();
        switch (view.getId()) {
            case R.id.parent_home:
                showFragment(0);
                mImgHome.setBackgroundResource(R.drawable.comui_tab_home_selected);
                mTvHome.setTextColor(getResources().getColor(R.color.color2473));
                mLineHome.setVisibility(View.VISIBLE);
                break;
            case R.id.parent_part_time:
                showFragment(1);
                mImgPartTime.setBackgroundResource(R.drawable.comui_tab_pond_selected);
                mTvPartTime.setTextColor(getResources().getColor(R.color.color2473));
                mLinePartTime.setVisibility(View.VISIBLE);
                break;
            case R.id.parent_discovery:
                showFragment(2);
                mImgDiscovery.setBackgroundResource(R.drawable.comui_tab_message_selected);
                mTvDiscovery.setTextColor(getResources().getColor(R.color.color2473));
                mLineDiscovery.setVisibility(View.VISIBLE);
                break;
            case R.id.parent_mine:
                showFragment(3);
                mImgMine.setBackgroundResource(R.drawable.comui_tab_person_selected);
                mTvMine.setTextColor(getResources().getColor(R.color.color2473));
                mLineMine.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    private void showFragment(int position) {
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (position) {
            case 0:
                if (mHomeFragment != null) {
                    ft.show(mHomeFragment);
                } else {
                    mHomeFragment = new ShoppingFragment();
                    ft.add(R.id.container, mHomeFragment);
                }
                break;
            case 1:
                if (mPartTimeFragment != null) {
                    ft.show(mPartTimeFragment);
                } else {
                    mPartTimeFragment = new PartTimeFragment();
                    ft.add(R.id.container, mPartTimeFragment);
                }
                break;
            case 2:
                if (mDiscoveryFragment != null) {
                    ft.show(mDiscoveryFragment);
                } else {
                    mDiscoveryFragment = new DiscoveryFragment();
                    ft.add(R.id.container, mDiscoveryFragment);
                }
                break;
            case 3:
                if (mPersonalCenterFragment != null) {
                    ft.show(mPersonalCenterFragment);
                } else {
                    mPersonalCenterFragment = new PersonalCenterFragment();
                    ft.add(R.id.container, mPersonalCenterFragment);
                }
                break;

            default:
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (mHomeFragment != null) {
            ft.hide(mHomeFragment);
        }
        if (mPartTimeFragment != null) {
            ft.hide(mPartTimeFragment);
        }
        if (mDiscoveryFragment != null) {
            ft.hide(mDiscoveryFragment);
        }
        if (mPersonalCenterFragment != null) {
            ft.hide(mPersonalCenterFragment);
        }
    }


    //    重置底部Button的背景和字体颜色
    private void reSetBackground() {
        mImgHome.setBackgroundResource(R.drawable.comui_tab_home);
        mTvHome.setTextColor(getResources().getColor(R.color.color9));
        mLineHome.setVisibility(View.INVISIBLE);

        mImgPartTime.setBackgroundResource(R.drawable.comui_tab_pond);
        mTvPartTime.setTextColor(getResources().getColor(R.color.color9));
        mLinePartTime.setVisibility(View.INVISIBLE);

        mImgDiscovery.setBackgroundResource(R.drawable.comui_tab_message);
        mTvDiscovery.setTextColor(getResources().getColor(R.color.color9));
        mLineDiscovery.setVisibility(View.INVISIBLE);

        mImgMine.setBackgroundResource(R.drawable.comui_tab_person);
        mTvMine.setTextColor(getResources().getColor(R.color.color9));
        mLineMine.setVisibility(View.INVISIBLE);
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
                        LogUtil.e("adversitingFile", s);
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
