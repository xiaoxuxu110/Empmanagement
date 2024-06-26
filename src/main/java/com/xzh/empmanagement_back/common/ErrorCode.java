package com.xzh.empmanagement_back.common;

/**
 * 错误码
 */
public enum ErrorCode {

    SUCCESS(0,"OK",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求数据为空",""),
    NOT_LOGIN(40100,"用户未登录",""),
    NO_AUTH(40101,"无权限访问",""),
    SYSTEM_ERROR(50000,"系统内部异常",""),
    NOT_FOUND_ERROR(40400, "请求数据不存在",""),
    OPERATION_ERROR(50001, "操作失败",""),
    INVALID_PASSWORD_ERROR(40102, "无效密码", "");


    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
