package com.example.qasystem.basic.utils.result;

import lombok.Data;

@Data
public class JsonResult {
    private Boolean success=true;
    private String message="操作成功";
    private Integer code =200;
    private Object data;
    private Object resultObj;

//    public static JsonResult me(){
//        return new JsonResult();
//    }

    public static JsonResult success(Object data){
        return new JsonResult().setData(data);
    }

    public JsonResult setSuccess(Boolean success){
        this.success=success;
        return this;
    }

    public  JsonResult setMassage(String message){
        this.message=message;
        return this;
    }

    public JsonResult setData(Object data){
        this.data=data;
        return this;
    }

    public JsonResult setCode(Integer code){
        this.code=code;
        return this;
    }

}
