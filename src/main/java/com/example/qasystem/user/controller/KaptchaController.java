package com.example.qasystem.user.controller;


import cn.hutool.core.map.MapUtil;
import com.example.qasystem.basic.constant.AuthConstant;
import com.example.qasystem.basic.utils.RedisUtil;
import com.example.qasystem.basic.utils.result.JsonResult;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/captcha")
@Slf4j
public class KaptchaController {

    @Autowired
    private Producer producer;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/create")
    public JsonResult createCaptcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        Base64.Encoder encoder = Base64.getEncoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encodeToString(outputStream.toByteArray());

        redisUtil.hset(AuthConstant.CAPTCHA_REDIS_KEY, key, code, 120);

        return JsonResult.success(MapUtil.builder()
                .put("userKey", key)
                .put("captcherImg", base64Img)
                .build()
        );
    }
}
