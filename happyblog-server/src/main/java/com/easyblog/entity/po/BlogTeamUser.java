package com.happyblog.entity.po;

import com.happyblog.entity.enums.DateTimePatternEnum;
import com.happyblog.entity.enums.EditorTypeEnum;
import com.happyblog.entity.enums.RoleTypeEnum;
import com.happyblog.entity.enums.UserStatusEnum;
import com.happyblog.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;


/**
 * 博客成员
 */
@SuppressWarnings("serial")
public class BlogTeamUser implements Serializable {


    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 职业
     */
    private String profession;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 0:富文本 1:markdown编辑器
     */
    private Integer editorType;

    /**
     * 0:普通用户 1:超级管理员
     */
    private Integer roleType;

    /**
     * 0:禁用 1:启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**
     * 最后登录时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date lastLoginTime;

    private Integer blogCount;

    public Integer getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(Integer blogCount) {
        this.blogCount = blogCount;
    }

    private String roleTypeName;

    private String editorTypeName;

    private String statusName;

    public String getStatusName() {
        UserStatusEnum userStatusEnum = UserStatusEnum.getByStatus(status);
        return userStatusEnum == null ? "" : userStatusEnum.getDesc();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getRoleTypeName() {
        RoleTypeEnum roleTypeEnum = RoleTypeEnum.getByType(roleType);
        return roleTypeEnum == null ? "" : roleTypeEnum.getDesc();
    }

    public void setRoleTypeName(String roleTypeName) {
        this.roleTypeName = roleTypeName;
    }

    public String getEditorTypeName() {
        EditorTypeEnum editorTypeEnum = EditorTypeEnum.getByType(editorType);
        return editorTypeEnum == null ? "" : editorTypeEnum.getDesc();
    }

    public void setEditorTypeName(String editorTypeName) {
        this.editorTypeName = editorTypeName;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfession() {
        return this.profession;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setEditorType(Integer editorType) {
        this.editorType = editorType;
    }

    public Integer getEditorType() {
        return this.editorType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Integer getRoleType() {
        return this.roleType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }

    public void setLastLoginTime(java.util.Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public java.util.Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    @Override
    public String toString() {
        return "用户ID:" + (userId == null ? "空" : userId) + "，昵称:" + (nickName == null ? "空" : nickName) + "，头像:" + (avatar == null ? "空" : avatar) + "，手机号:" + (phone == null ? "空" : phone) + "，密码:" + (password == null ? "空" : password) + "，职业:" + (profession == null ? "空" : profession) + "，简介:" + (introduction == null ? "空" : introduction) + "，0:富文本 1:markdown编辑器:" + (editorType == null ? "空" : editorType) + "，0:普通用户 1:超级管理员:" + (roleType == null ? "空" : roleType) + "，0:禁用 1:启用:" + (status == null ? "空" : status) + "，创建时间:" + (createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，最后登录时间:" + (lastLoginTime == null ? "空" : DateUtil.format(lastLoginTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
    }
}
