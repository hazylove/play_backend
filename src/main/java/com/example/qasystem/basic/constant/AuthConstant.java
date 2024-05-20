package com.example.qasystem.basic.constant;

public class AuthConstant {
    // token请求头 前缀
    public static final String TOKEN_PREFIX = "Bearer ";
    // 图片验证码在redis中存储的key
    public static final String CAPTCHA_REDIS_KEY = "captcha";
    // token在redis中存储的前缀
    public static final String TOKEN_REDIS_PREFIX = "TOKEN_";
}
