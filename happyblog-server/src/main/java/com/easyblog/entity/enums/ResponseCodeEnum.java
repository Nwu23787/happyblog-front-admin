package com.happyblog.entity.enums;

public enum ResponseCodeEnum {

    CODE_200(200, "请求成功"),
    CODE_400(400, "业务错误"),
    CODE_600(600, "请求参数不正确"),
    CODE_404(404, "请求地址不存在"),
    CODE_601(601, "数据转换失败"),
    CODE_500(500, "服务异常"),
    CODE_901(901, "登录超时"),
    CODE_902(902, "没有操作权限"),
    CODE_903(903, "Http请求超时"),
    CODE_904(904, "Http请求失败"),
    CODE_905(905, "未获取到信息");


    private Integer code;

    private String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
