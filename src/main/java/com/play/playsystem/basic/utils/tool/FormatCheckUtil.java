package com.play.playsystem.basic.utils.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatCheckUtil {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX = "1[3-9]\\d{9}";

    /**
     * 校验邮箱格式
     * @param email 邮箱
     * @return 正确返回true，错误返回false
     */
    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 校验手机号格式
     * @param phone 手机号
     * @return 正确返回true，错误返回false
     */
    public static boolean validatePhone(String phone) {
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
