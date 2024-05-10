package com.example.qasystem.basic.utils.result;

public class ResultCode {
    // 成功
    public static final int SUCCESS_CODE = 200;
    // 错误
    public static final int ERROR_CODE = 500;
    // 验证码错误
    public static final int VERIFICATION_CODE_ERROR_CODE = 1001;
    // 用户名密码错误
    public static final int INCORRECT_USERNAME_AND_PASSWORD_CODE = 1002;
    // 未经授权
    public static final int UNAUTHORIZED_CODE = 401;
    // 错误的请求
    public static final int BAD_REQUEST_CODE = 400;
    // 禁止的请求
    public static final int FORBIDDEN_CODE = 403;
    // 参数错误
    public static final int UNPROCESSABLE_ENTITY = 422;


    /**
     * 富文本图片上传返回码
     */
    // 成功
    public static final int IMAGE_SUCCESS_CODE = 0;
    // 失败
    public static final int IMAGE_ERROR_CODE = 0;
}
