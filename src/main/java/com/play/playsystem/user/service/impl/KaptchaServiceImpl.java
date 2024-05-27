package com.play.playsystem.user.service.impl;

import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.play.playsystem.basic.constant.AuthConstant;
import com.play.playsystem.basic.utils.result.JsonResult;
import com.play.playsystem.basic.utils.tool.RedisUtil;
import com.play.playsystem.user.service.IKaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
public class KaptchaServiceImpl implements IKaptchaService {


    @Autowired
    private Producer producer;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public JsonResult createCaptcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        Base64.Encoder encoder = Base64.getEncoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encodeToString(outputStream.toByteArray());

        redisUtil.set(AuthConstant.CAPTCHA_REDIS_KEY + key, code, 120);

        return JsonResult.success(MapUtil.builder()
                .put("userKey", key)
                .put("captcherImg", base64Img)
                .build()
        );
    }
}
