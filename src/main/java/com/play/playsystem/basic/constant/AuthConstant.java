package com.play.playsystem.basic.constant;

public class AuthConstant {
    // 头像文件存储相对目录
    public static final String AVATAR_PATH = "avatar/";


    // token请求头 前缀
    public static final String TOKEN_PREFIX = "Bearer ";
    // 图片验证码在redis中存储的key
    public static final String CAPTCHA_REDIS_KEY = "CAPTCHA_";
    // token在redis中存储的前缀
    public static final String TOKEN_REDIS_PREFIX = "TOKEN_";
}
