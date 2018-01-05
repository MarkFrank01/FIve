package com.wjc.parttime.util;

import android.content.Context;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * ShareSDK一键分享案例
 * Created by y_hui on 2018/1/3.
 */

public class ShareUtil {

    /**
     * @param title:分享的标题
     * @param titleUrl:分享的URL
     * @param text:分享的文本
     * @param context:分享的上下文
     * @方法名:showShareQQ
     * @return:
     * @描述: 分享到QQ好友和QQ空间
     * @作者： yhui
     * @创建日期 2018/1/3
     */
    public static void showShareQQ(String title, String titleUrl, String text,String imgUrl, Context context) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(titleUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(imgUrl);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //  oks.setImagePath(imgUrl);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //  oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //  oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("partTime");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //  oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * @param title:分享的标题
     * @param titleUrl:分享的URL
     * @param text:分享的文本
     * @param context:分享的上下文
     * @方法名:showShareWX
     * @return:
     * @描述: 分享到微信好友和微信朋友圈
     * @作者： yhui
     * @创建日期 2018/1/3
     */
    public static void showShareWX(String title, String titleUrl, String text,String imgUrl, Context context) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        //   oks.setTitleUrl(titleUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(imgUrl);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //  oks.setImagePath(imgUrl);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(titleUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //  oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("partTime");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //  oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(context);
    }


}
