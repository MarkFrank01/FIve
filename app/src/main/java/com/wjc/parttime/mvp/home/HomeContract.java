package com.wjc.parttime.mvp.home;

import com.wjc.parttime.mvp.IBasePresenter;
import com.wjc.parttime.mvp.IBaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by WJC on 2018/1/5 20:11
 * Describe : TODO
 */

public interface HomeContract {

    interface View extends IBaseView {
        void setAdapter(List<Map<String, Object>> mainList1, List<Map<String, Object>> mainList2);
    }

    interface Presenter extends IBasePresenter<View>{

        void loadResult(int position);

        void loadNews1(int position);
    }
}
