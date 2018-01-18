package com.wjc.parttime.mvp.discovery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wjc.parttime.R;
import com.wjc.parttime.mvp.MVCBaseFragment;

import butterknife.ButterKnife;

/**
 * Created by WJC on 2018/1/11 9:53
 * Describe : TODO
 */

public class DiscoveryFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery,container,false);
        return view;
    }
}
