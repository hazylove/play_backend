package com.play.playsystem.basic.utils.result;

import lombok.Data;

@Data
public class ImgResult {
    private int errno = 0;
    private Object data;
    private String message;

    public ImgResult setErrno(int errno){
        this.errno=errno;
        return this;
    }

    public ImgResult setData(Object data){
        this.data=data;
        return this;
    }

    public ImgResult setMessage(String message){
        this.message=message;
        return this;
    }

}
