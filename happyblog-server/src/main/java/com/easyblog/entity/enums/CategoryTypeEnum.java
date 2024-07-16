package com.happyblog.entity.enums;

public enum CategoryTypeEnum {
    BLOG(0, "博客"),
    SPECIAL(1, "专题");


    private Integer type;
    private String desc;

    CategoryTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static CategoryTypeEnum getByType(Integer type) {
        for (CategoryTypeEnum item : CategoryTypeEnum.values()) {
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
