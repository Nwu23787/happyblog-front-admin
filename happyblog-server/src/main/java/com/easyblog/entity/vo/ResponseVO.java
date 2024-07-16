package com.happyblog.entity.vo;


import com.happyblog.entity.enums.ResponseCodeEnum;
import com.happyblog.entity.enums.ResponseStatusEnum;

public class ResponseVO<T> {
    private T data;
    private String status = ResponseStatusEnum.SUCCESS.getStatus();
    private Integer code = ResponseCodeEnum.CODE_200.getCode();
    private String info;

    public ResponseVO() {

    }

    public ResponseVO(String status, Integer code, String info) {
        this.status = status;
        this.code = code;
        this.info = info;
    }


    public ResponseVO(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public ResponseVO(Integer code, String info, T data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }

    public ResponseVO(String status, Integer code, String info, T data) {
        this.status = status;
        this.code = code;
        this.info = info;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
