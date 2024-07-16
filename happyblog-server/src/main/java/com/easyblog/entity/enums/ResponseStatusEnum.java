package com.happyblog.entity.enums;

/**
 * @description:
 * @author:HailiLuo
 * @date:Create in 2018/3/22
 */
public enum ResponseStatusEnum {

    SUCCESS("success","请求成功"),
    ERROR("error","请求失败");

    ResponseStatusEnum(String status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ResponseStatusEnum getByStatus(Integer code) {
        for (ResponseStatusEnum item : ResponseStatusEnum.values()) {
           if(item.getStatus().equals(code)){
               return item;
           }
        }
        return null;
    }

    private String status;
    private String desc;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
