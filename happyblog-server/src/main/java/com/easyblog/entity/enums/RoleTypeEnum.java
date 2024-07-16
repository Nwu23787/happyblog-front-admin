package com.happyblog.entity.enums;

public enum RoleTypeEnum {
    ROLE_TEAM_USER(0, "成员"),
    ROLE_ADMIN(1, "管理员");


    private Integer type;
    private String desc;

    RoleTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static RoleTypeEnum getByType(Integer type) {
        for (RoleTypeEnum item : RoleTypeEnum.values()) {
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
