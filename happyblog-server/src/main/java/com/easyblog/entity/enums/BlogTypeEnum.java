package com.happyblog.entity.enums;

public enum BlogTypeEnum {
    BLOG(0, "博客"),
    SPECIAL(1, "专题");


    private Integer type;
    private String desc;

    BlogTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static BlogTypeEnum getByType(Integer type) {
        for (BlogTypeEnum item : BlogTypeEnum.values()) {
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
