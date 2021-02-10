package com.yang.system.support.resp;

import lombok.Data;

/**
 * @project data-parent
 * @Date 2021/2/7
 * @Auth yangrui
 **/
@Data
public class ResponseResult<T> {

    private int code;

    private String message;

    private T data;

    public ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseResult success(){

        return new ResponseResult(200,"访问成功");
    }

    public static <T> ResponseResult success(T data){
        return new ResponseResult(200,"访问成功",data);
    }

    public static ResponseResult error(int code,String message){
        return new ResponseResult(code,message);
    }

    public static ResponseResult error(int code){
        return new ResponseResult(code,"服务器内部错误");
    }

    public static ResponseResult error(){
        return new ResponseResult(500,"服务器内部错误");
    }
}
