package com.yang.system.support.exception;

import com.yang.system.support.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class CatchException {

    //可预知异常
    @ExceptionHandler(InterException.class)
    @ResponseBody
    public ResponseResult interException(InterException exception) {
        int code = exception.getCode();
        String message = exception.getMessage();
        log.error(message,exception);
        return ResponseResult.error(code,message);
    }

    //不可预知异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception) {
        log.error("服务器内部错误", exception);
        return ResponseResult.error();
    }
}
