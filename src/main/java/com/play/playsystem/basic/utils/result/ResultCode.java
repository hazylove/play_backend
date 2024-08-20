package com.play.playsystem.basic.utils.result;

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
     * 用户
     */
    // 用户异常操作
    public static final int USER_OPERATION_ERROR = 990;
    // 数据不存在
    public static final int DATA_NOT_EXIST = 991;

    // 验证码错误
    public static final int VERIFICATION_CODE_ERROR_CODE = 1001;
    // 登录信息验证失败
    public static final int LOGIN_FAILED = 1002;

    // 注册用户名已存在
    public static final int USERNAME_EXISTING = 1010;
    // 两次输入密码不一致
    public static final int TWICE_PASSWORD_INCONSISTENT = 1011;
    // 用户名格式不正确
    public static final int USERNAME_FORMAT_ERROR = 1012;

    // 邮箱验证码错误
    public static final int EMAIL_CODE_ERROR = 1020;
    // 邮箱格式错误
    public static final int EMAIL_FORMAT_ERROR = 1021;
    // 注册邮箱已存在
    public static final int EMAIL_EXISTING = 1022;
    // 修改密码邮箱验证失败
    public static final int EMAIL_CHECK_FAILED = 1023;
    // 邮箱未注册
    public static final int EMAIL_NOT_REGISTER = 1024;

    // 手机号格式错误
    public static final int PHONE_FORMAT_ERROR = 1030;

    // 头像大小超出限制
    public static final int AVATAR_SIZE_TOO_LARGE = 1040;
    // 头像文件类型错误
    public static final int AVATAR_TYPE_ERROR = 1041;

    // 用户不存在
    public static final int USER_NOT_EXIST = 1051;

    // 已成为好友，不可重复添加
    public static final int BECAME_FRIEND = 1055;

    // 被拉黑不能操作
    public static final int BLOCKED_NOT_OPERATE = 1060;

    /**
     * 帖子相关返回码
     */
    // 帖子不存在
    public static final int POST_NOT_EXIST = 1101;

    // 评论不存在
    public static final int COMMENT_NOT_EXIST = 1151;

    /**
     * 富文本图片上传返回码
     */
    // 成功
    public static final int IMAGE_SUCCESS_CODE = 0;
    // 失败
    public static final int IMAGE_ERROR_CODE = 0;
}
