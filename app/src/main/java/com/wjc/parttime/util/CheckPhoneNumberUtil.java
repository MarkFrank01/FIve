package com.wjc.parttime.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断字符串是否符合手机号码格式
 * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
 * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
 * 电信号段: 133,149,153,170,173,177,180,181,189
 * @return 是否匹配,true为不匹配，false为匹配
 */

public class CheckPhoneNumberUtil {

    public static boolean FormatCheckForPhone(String phone) {

        if (phone != null && phone.length() == 11) {
            Pattern pattern = Pattern.compile("^(13|14|15|17|18)\\d{9}$");
            Matcher matcher = pattern.matcher(phone);
            return matcher.matches();
        }
        return false;
    }
}
