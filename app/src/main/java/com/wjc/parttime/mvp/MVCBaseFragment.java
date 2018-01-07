package com.wjc.parttime.mvp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WJC on 2018/1/6 15:01
 * Describe : TODO
 */

public abstract class MVCBaseFragment extends Fragment{
    public abstract int getResource();//初始化资源文件

    public abstract void init(View view);//初始化组件

    public abstract void loadingDatas();//加载数据，初始化数据，初始化对象

    public abstract void startdestroy();//销毁数据，释放内存

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getActivity()).inflate(getResource(), container, false);
        init(view);
        loadingDatas();
        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        startdestroy();
        System.gc();
    }
}
