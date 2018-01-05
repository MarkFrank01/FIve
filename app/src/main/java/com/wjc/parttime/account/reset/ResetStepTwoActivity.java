package com.wjc.parttime.account.reset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wjc.parttime.R;

/**
 * Created by WJC on 2017/12/30 11:17
 * Describe : TODO
 */

public class ResetStepTwoActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, ResetStepTwoActivity.class);
        context.startActivity(intent);
    }
}
