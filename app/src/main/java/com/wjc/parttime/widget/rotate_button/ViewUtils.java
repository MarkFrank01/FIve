package com.wjc.parttime.widget.rotate_button;

import android.content.Context;

/**
 * Created by WJC on 2018/1/5 20:54
 * Describe : TODO
 */

public class ViewUtils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
