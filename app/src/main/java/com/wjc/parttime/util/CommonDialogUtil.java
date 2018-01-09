package com.wjc.parttime.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wjc.parttime.R;

/**
 * 仿IOS风格dialog
 * Created by y_hui on 2018/1/4.
 */

public class CommonDialogUtil extends Dialog implements View.OnClickListener {

        private TextView contentTxt;
        private TextView titleTxt;
        private TextView submitTxt;
        private TextView cancelTxt;

        private Context mContext;
        private String content;
        private OnListener listener;
        private String positiveName;
        private String negativeName;
        private String title;

        public CommonDialogUtil(Context context) {
            super(context);
            this.mContext = context;
        }

        public CommonDialogUtil(Context context, int themeResId, String content) {
            super(context, themeResId);
            this.mContext = context;
            this.content = content;
        }

        public CommonDialogUtil(Context context, int themeResId, String content,String positiveName,String negativeName, OnListener listener) {
            super(context, themeResId);
            this.mContext = context;
            this.content = content;
            this.positiveName=positiveName;
            this.negativeName=negativeName;
            this.listener = listener;
        }

    public CommonDialogUtil(Context context, int themeResId, String content,String positiveName, OnListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.positiveName=positiveName;
        this.listener = listener;
    }

        protected CommonDialogUtil(Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
            this.mContext = context;
        }

        public CommonDialogUtil setTitle(String title){
            this.title = title;
            return this;
        }

        public CommonDialogUtil setPositiveButton(String name){
            this.positiveName = name;
            return this;
        }

        public CommonDialogUtil setNegativeButton(String name){
            this.negativeName = name;
            return this;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_common);
            setCanceledOnTouchOutside(false);
            initView();
        }

        private void initView(){
            contentTxt = (TextView)findViewById(R.id.content);
            titleTxt = (TextView)findViewById(R.id.title);
            submitTxt = (TextView)findViewById(R.id.submit);
            submitTxt.setOnClickListener(this);
            cancelTxt = (TextView)findViewById(R.id.cancel);
            cancelTxt.setOnClickListener(this);

            contentTxt.setText(content);
            if(!TextUtils.isEmpty(positiveName)){
                submitTxt.setText(positiveName);
            }

            if(!TextUtils.isEmpty(negativeName)){
                cancelTxt.setText(negativeName);
            }else{
                cancelTxt.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(title)){
                titleTxt.setText(title);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cancel:
                    if(listener != null){
                        listener.onCancelclick();
                    }
                   // this.dismiss();
                    break;
                case R.id.submit:
                    if(listener != null){
                        listener.onConfirmClick();
                    }
                    break;
            }
        }

        public interface OnListener{
           // void onClick(Dialog dialog, boolean confirm);
           void onCancelclick();
            void onConfirmClick();
        }
    }

