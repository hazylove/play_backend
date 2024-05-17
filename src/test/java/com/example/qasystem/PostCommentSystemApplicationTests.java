package com.example.qasystem;

import com.example.qasystem.basic.utils.FormatCheckUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostCommentSystemApplicationTests {
    @Autowired
    private FormatCheckUtil formatCheckUtil;

    @Test
    void contextLoads() {
        String email = "test@gmail.com";
        System.out.println(formatCheckUtil.validateEmail(email));
    }

}
