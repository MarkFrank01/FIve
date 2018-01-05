package com.wjc.parttime.mvp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wjc.parttime.R;
import com.wjc.parttime.account.login.LoginActivity;
import com.wjc.parttime.widget.rotate_button.OnRotateItemClickListener;
import com.wjc.parttime.widget.rotate_button.RotateMenuItem;
import com.wjc.parttime.widget.rotate_button.RotateMenuView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseHomeActivity extends AppCompatActivity {


    @BindView(R.id.rotate_button)
    RotateMenuView mRotateMenuView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_home);
        ButterKnife.bind(this);

        ShowRotateView();

    }

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
}
