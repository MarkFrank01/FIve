package com.wjc.parttime.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wjc.parttime.R;
import com.wjc.parttime.util.CommonDialogUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PersonalSettingSuggestActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.person_suggest_tv)
    TextView limit;

    @BindView(R.id.person_suggest_et)
    EditText suggest;
    @BindView(R.id.bt_suggest_submit)
    Button submit;

    ImageButton back;

    TextView title;

    CommonDialogUtil dialog;

    private final static int LIMIT = 500; //字符限制

    private final static int TV_LIMIT = 1003;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TV_LIMIT:
                    int mLimit = (int) msg.obj;
                    if (mLimit > 0 && mLimit <= 10) {
                        limit.setText(msg.obj + "");
                        limit.setTextColor(Color.RED);
                    } else {
                        limit.setText(msg.obj + "");
                        limit.setTextColor(PersonalSettingSuggestActivity.this.getResources().getColor(R.color.color9));
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_person_suggest);
        View bar_ll = findViewById(R.id.ly_setting_bar);
        title = bar_ll.findViewById(R.id.tv_navigation_label);
        title.setText("意见反馈");
        back = bar_ll.findViewById(R.id.ib_navigation_back);
        back.setOnClickListener(this);
        ButterKnife.bind(this);
        initData();
    }

    public void initData() {
        limit.setText(LIMIT + "");
        suggest.addTextChangedListener(new MyTextWatcher(suggest, limit, LIMIT, PersonalSettingSuggestActivity.this)); //字数变化监听
    }

    /**
     * show the PersonalSettingSuggestActivity activity
     *
     * @param context context
     */
    public static void show(Activity context) {
        Intent intent = new Intent(context, PersonalSettingSuggestActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.bt_suggest_submit)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.bt_suggest_submit:
                String suggestcomplete = suggest.getText().toString().trim();
                if (TextUtils.isEmpty(suggestcomplete)) {
                    showToast("请填写反馈建议内容");
                } else {
                    //向后台提交内容
                }
                break;
        }
    }

    /**
     * 居中显示的Toast
     *
     * @param content
     */
    public void showToast(String content) {

        Toast toast = Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /*
    *
    *自定义MyTextWatcher类实现TextWatcher接口
    *
    * */
    private class MyTextWatcher implements TextWatcher {
        private int limit;// 字符个数限制
        private EditText text;// 编辑框控件
        private TextView tvlimit;//显示剩余数字
        private Context context;// 上下文对象

        int cursor = 0;// 用来记录输入字符的时候光标的位置
        int before_length;// 用来标注输入某一内容之前的编辑框中的内容的长度

        public MyTextWatcher(EditText text, TextView tvlimit, int limit,
                             Context context) {
            this.limit = limit;
            this.text = text;
            this.context = context;
            this.tvlimit = tvlimit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            before_length = s.length();
        }

        /**
         * s 编辑框中全部的内容 、start 编辑框中光标所在的位置（从0开始计算）、count 从手机的输入法中输入的字符个数
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            cursor = start;
//      Log.e("此时光标的位置为", cursor + "");
        }

        @Override
        public void afterTextChanged(Editable s) {
            // 这里可以知道已经输入的字数，可以自己根据需求来自定义文本控件实时的显示已经输入的字符个数
            //  Logger.i("此时你已经输入了", "" + s.length());
            int surpluslimit = limit - s.length(); //剩余字符长度
            //  Logger.i("123", "剩余" + surpluslimit);
            Message message = new Message();
            message.obj = surpluslimit;
            message.what = TV_LIMIT;
            handler.sendMessage(message);
            int after_length = s.length();// 输入内容后编辑框所有内容的总长度
            // 如果字符添加后超过了限制的长度，那么就移除后面添加的那一部分，这个很关键
            if (after_length > limit) {
                // 比限制的最大数超出了多少字
                int d_value = after_length - limit;
                // 这时候从手机输入的字的个数
                int d_num = after_length - before_length;

                int st = cursor + (d_num - d_value);// 需要删除的超出部分的开始位置
                int en = cursor + d_num;// 需要删除的超出部分的末尾位置
                // 调用delete()方法将编辑框中超出部分的内容去掉
                Editable s_new = s.delete(st, en);
                // 给编辑框重新设置文本
                text.setText(s_new.toString());
                // 设置光标最后显示的位置为超出部分的开始位置，优化体验
                text.setSelection(st);
                // 弹出信息提示已超出字数限制
                showToast("已超出最大字数限制");
            }
        }

    }
}
