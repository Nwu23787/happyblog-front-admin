package com.happyblog.entity.enums;

public enum AllowCommentTypeEnum {
    NOT_ALLOW(0, "不允许"),
    ALLOW(1, "允许");


    private Integer type;
    private String desc;

    AllowCommentTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static AllowCommentTypeEnum getByType(Integer type) {
        for (AllowCommentTypeEnum item : AllowCommentTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
