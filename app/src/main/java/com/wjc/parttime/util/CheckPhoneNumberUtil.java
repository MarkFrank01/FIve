package com.wjc.parttime.util;

/**
 * 校验手机号码是否合法
 * Created by yhui on 2017/4/1 0001.
 */

public class CheckPhoneNumberUtil {

    public static boolean FormatCheckForPhone(String phone) {

        return phone.matches("^(13|14|15|17|18)\\d{9}$");
    }
}
