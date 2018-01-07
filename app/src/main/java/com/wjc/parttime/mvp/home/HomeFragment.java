package com.wjc.parttime.mvp.home;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.wjc.parttime.R;
import com.wjc.parttime.mvp.MVCBaseFragment;
import com.wjc.parttime.util.LogUtil;

import butterknife.BindView;
import butterknife.Unbinder;


/**
 * Created by WJC on 2018/1/12 15:19
 * Describe : TODO
 */

public class HomeFragment extends MVCBaseFragment{

    private static String TAG = "HomeFragment";

    @BindView(R.id.home_recyclerView)
    RecyclerView home_RecycleView;


    @Override
    public int getResource() {

        return R.layout.fragment_home;
    }

    @Override
    public void init(View view) {

    }

    @Override
    public void loadingDatas() {

    }

    @Override
    public void startdestroy() {

    }
}
