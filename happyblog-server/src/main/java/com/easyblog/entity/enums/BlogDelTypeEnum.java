package com.happyblog.entity.enums;

public enum BlogDelTypeEnum {
    DEL(0, "已删除"),
    NORMAL(1, "正常");


    private Integer type;
    private String desc;

    BlogDelTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static BlogDelTypeEnum getByType(Integer type) {
        for (BlogDelTypeEnum item : BlogDelTypeEnum.values()) {
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
