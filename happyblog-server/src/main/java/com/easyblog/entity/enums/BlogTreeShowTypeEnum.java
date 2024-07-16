package com.happyblog.entity.enums;

public enum BlogTreeShowTypeEnum {
    ALL(0, "包含全部"),
    WITH_CATEGORY(1, "包含分类"),
    ONLY_BLOG(2, "只有博客");


    private Integer type;
    private String desc;

    BlogTreeShowTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static BlogTreeShowTypeEnum getByType(Integer type) {
        for (BlogTreeShowTypeEnum item : BlogTreeShowTypeEnum.values()) {
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
