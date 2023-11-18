package com.example.qasystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.qasystem.org.mapper")
public class QaSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(QaSystemApplication.class, args);
    }

}
