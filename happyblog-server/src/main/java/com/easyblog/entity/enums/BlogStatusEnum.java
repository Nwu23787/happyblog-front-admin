package com.happyblog.entity.enums;

public enum BlogStatusEnum {
    STATUS_0(0, "草稿"),
    STATUS_1(1, "已发布");


    private Integer status;
    private String desc;

    BlogStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static BlogStatusEnum getByStatus(Integer status) {
        for (BlogStatusEnum item : BlogStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
