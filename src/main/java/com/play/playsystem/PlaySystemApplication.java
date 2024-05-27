package com.play.playsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.play.playsystem.*.mapper")
public class PlaySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaySystemApplication.class, args);
    }

}
