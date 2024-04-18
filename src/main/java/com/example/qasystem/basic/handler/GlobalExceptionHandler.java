package com.example.qasystem.basic.handler;

import com.example.qasystem.basic.utils.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.example.qasystem.org.controller")
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局异常处理：处理所有未被捕获的异常
     *
     * @param e       异常对象
     * @param request 请求对象
     * @return 返回封装后的响应结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public JsonResult handleException(Exception e, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        log.error("接口：{}：发生异常", requestUrl, e);
        return new JsonResult().setCode(500).setSuccess(false).setMassage("服务器错误");
    }

    /**
     * 参数校验异常处理
     *
     * @param e       异常对象
     * @param request 请求对象
     * @return 返回封装后的响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonResult handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        log.error("接口：{}：参数校验失败", requestUrl, e);

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new JsonResult().setCode(400).setSuccess(false).setMassage("参数校验失败").setData(errorMap);
    }

    /**
     * 参数绑定异常处理
     *
     * @param e       异常对象
     * @param request 请求对象
     * @return 返回封装后的响应结果
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonResult handleBindException(BindException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        log.error("接口：{}：参数绑定失败", requestUrl, e);

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new JsonResult().setCode(400).setSuccess(false).setMassage("参数绑定失败").setData(errorMap);
    }

    /**
     * 请求体解析异常处理
     *
     * @param e       异常对象
     * @param request 请求对象
     * @return 返回封装后的响应结果
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        log.error("接口：{}：请求体解析失败", requestUrl, e);

        return new JsonResult().setCode(400).setSuccess(false).setMassage("请求体解析失败");
    }

}
