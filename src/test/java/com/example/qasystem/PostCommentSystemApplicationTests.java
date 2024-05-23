package com.example.qasystem;

import com.example.qasystem.basic.utils.tool.FormatCheckUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostCommentSystemApplicationTests {

    @Test
    void contextLoads() {
        String email = "test@gmail.com";
        System.out.println(FormatCheckUtil.validateEmail(email));
    }

}
