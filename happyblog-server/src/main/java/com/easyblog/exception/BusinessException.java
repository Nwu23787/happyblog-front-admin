package com.happyblog.exception;


import com.happyblog.entity.enums.ResponseCodeEnum;

public class BusinessException extends Exception {
    private static final long serialVersionUID = 2874310081615076500L;

    private Integer code;

    public BusinessException(String message, Throwable e) {
        super(message, e);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResponseCodeEnum responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
    }


    public BusinessException(Throwable e) {
        super(e);
    }

    /**
     * 重写fillInStackTrace 业务异常不需要堆栈信息，提高效率.
     *
     * @see Throwable#fillInStackTrace()
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
