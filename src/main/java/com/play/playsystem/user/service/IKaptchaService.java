package com.play.playsystem.user.service;

import com.play.playsystem.basic.utils.result.JsonResult;

import java.io.IOException;

public interface IKaptchaService {

    JsonResult createCaptcha() throws IOException;
}
