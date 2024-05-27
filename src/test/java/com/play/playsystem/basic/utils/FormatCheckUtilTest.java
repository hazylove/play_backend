package com.play.playsystem.basic.utils;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


class FormatCheckUtilTest {
    @Test
    void validatePhone() {
    }

    @Test
    void validateEmail() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utcTime = sdf.format(date);
        System.out.println("当前UTC时间：" + utcTime);

        System.out.println(Calendar.getInstance().getTime());
    }

}