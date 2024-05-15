package com.example.qasystem.basic.utils.result;

public class ResultCode {
    /**
     * 系统
     */
    // 成功
    public static final int SUCCESS_CODE = 200;
    // 错误
    public static final int ERROR_CODE = 500;
    // 未经授权
    public static final int UNAUTHORIZED_CODE = 401;
    // 错误的请求
    public static final int BAD_REQUEST_CODE = 400;
    // 禁止的请求
    public static final int FORBIDDEN_CODE = 403;
    // 参数错误
    public static final int UNPROCESSABLE_ENTITY = 422;

    /**
     * 登录
     */
    // 验证码错误
    public static final int VERIFICATION_CODE_ERROR_CODE = 1001;
    // 用户名密码错误
    public static final int INCORRECT_USERNAME_AND_PASSWORD_CODE = 1002;

    /**
     * 注册
     */
    // 注册用户名已存在
    public static final int USERNAME_EXISTING = 1101;
    // 注册邮箱已存在
    public static final int EMAIL_EXISTING = 1102;
    // 两次输入密码不一致
    public static final int TWICE_PASSWORD_INCONSISTENT = 1103;
    // 用户名、密码格式不正确
    public static final int USERNAME_PASSWORD_FORMAT_ERROR = 1104;
    // 邮箱验证码错误
    public static final int EMAIL_CODE_ERROR = 1105;

    /**
     * 富文本图片上传返回码
     */
    // 成功
    public static final int IMAGE_SUCCESS_CODE = 0;
    // 失败
    public static final int IMAGE_ERROR_CODE = 0;
}
