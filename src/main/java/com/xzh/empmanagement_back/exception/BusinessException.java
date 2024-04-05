package com.xzh.empmanagement_back.exception;

import com.xzh.empmanagement_back.common.ErrorCode;

/**
 * 自定义异常类
 */
public class BusinessException extends RuntimeException{

    private final int code;

    private final String description;

    public BusinessException(int code,String message,String description){
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode,String message){
        super(message);
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode,String message,String description){
        super(message);
        this.code = errorCode.getCode();
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
