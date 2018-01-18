package com.wjc.parttime.mvp.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wjc.parttime.R;
import com.wjc.parttime.mvp.discovery.DiscoveryFragment;
import com.wjc.parttime.mvp.part_time.PartTimeFragment;
import com.wjc.parttime.mvp.personal_center.PersonalCenterFragment;
import com.wjc.parttime.mvp.testhome.ShoppingFragment;
import com.wjc.parttime.util.ActivityUtils;
import com.wjc.parttime.widget.rotate_button.OnRotateItemClickListener;
import com.wjc.parttime.widget.rotate_button.RotateMenuItem;
import com.wjc.parttime.widget.rotate_button.RotateMenuView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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


    //四个Fragment,Home为首页,Part_time为我的兼职,Discovery为发现,Personal_Center为个人中心
//    private HomeFragment mHomeFragment;
    private ShoppingFragment mHomeFragment;
    private PartTimeFragment mPartTimeFragment;
    private DiscoveryFragment mDiscoveryFragment;
    private PersonalCenterFragment mPersonalCenterFragment;

    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_home);
        ButterKnife.bind(this);
        fm = getSupportFragmentManager();

        init();
        ShowRotateView();

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
                break;
            case R.id.parent_part_time:
                showFragment(1);
                mImgPartTime.setBackgroundResource(R.drawable.comui_tab_pond_selected);
                mTvPartTime.setTextColor(getResources().getColor(R.color.color2473));
                break;
            case R.id.parent_discovery:
                showFragment(2);
                mImgDiscovery.setBackgroundResource(R.drawable.comui_tab_message_selected);
                mTvDiscovery.setTextColor(getResources().getColor(R.color.color2473));
                break;
            case R.id.parent_mine:
                showFragment(3);
                mImgMine.setBackgroundResource(R.drawable.comui_tab_person_selected);
                mTvMine.setTextColor(getResources().getColor(R.color.color2473));
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

        mImgPartTime.setBackgroundResource(R.drawable.comui_tab_pond);
        mTvPartTime.setTextColor(getResources().getColor(R.color.color9));

        mImgDiscovery.setBackgroundResource(R.drawable.comui_tab_message);
        mTvDiscovery.setTextColor(getResources().getColor(R.color.color9));

        mImgMine.setBackgroundResource(R.drawable.comui_tab_person);
        mTvMine.setTextColor(getResources().getColor(R.color.color9));
    }

}
