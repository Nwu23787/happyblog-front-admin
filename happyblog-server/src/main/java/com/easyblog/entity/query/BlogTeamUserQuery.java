package com.happyblog.entity.query;


/**
 * 博客成员参数
 */
public class BlogTeamUserQuery extends BaseParam {


    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 昵称
     */
    private String nickName;

    private String nickNameFuzzy;

    /**
     * 头像
     */
    private String avatar;

    private String avatarFuzzy;

    /**
     * 手机号
     */
    private String phone;

    private String phoneFuzzy;

    /**
     * 密码
     */
    private String password;

    private String passwordFuzzy;

    /**
     * 职业
     */
    private String profession;

    private String professionFuzzy;

    /**
     * 简介
     */
    private String introduction;

    private String introductionFuzzy;

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
    private String createTime;

    private String createTimeStart;
    private String createTimeEnd;

    /**
     * 最后登录时间
     */
    private String lastLoginTime;

    private String lastLoginTimeStart;
    private String lastLoginTimeEnd;

    private Boolean queryBlogCount;

    public Boolean getQueryBlogCount() {
        return queryBlogCount;
    }

    public void setQueryBlogCount(Boolean queryBlogCount) {
        this.queryBlogCount = queryBlogCount;
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

    public void setNickNameFuzzy(String nickNameFuzzy) {
        this.nickNameFuzzy = nickNameFuzzy;
    }

    public String getNickNameFuzzy() {
        return this.nickNameFuzzy;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatarFuzzy(String avatarFuzzy) {
        this.avatarFuzzy = avatarFuzzy;
    }

    public String getAvatarFuzzy() {
        return this.avatarFuzzy;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhoneFuzzy(String phoneFuzzy) {
        this.phoneFuzzy = phoneFuzzy;
    }

    public String getPhoneFuzzy() {
        return this.phoneFuzzy;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPasswordFuzzy(String passwordFuzzy) {
        this.passwordFuzzy = passwordFuzzy;
    }

    public String getPasswordFuzzy() {
        return this.passwordFuzzy;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfession() {
        return this.profession;
    }

    public void setProfessionFuzzy(String professionFuzzy) {
        this.professionFuzzy = professionFuzzy;
    }

    public String getProfessionFuzzy() {
        return this.professionFuzzy;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setIntroductionFuzzy(String introductionFuzzy) {
        this.introductionFuzzy = introductionFuzzy;
    }

    public String getIntroductionFuzzy() {
        return this.introductionFuzzy;
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

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeStart() {
        return this.createTimeStart;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getCreateTimeEnd() {
        return this.createTimeEnd;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTimeStart(String lastLoginTimeStart) {
        this.lastLoginTimeStart = lastLoginTimeStart;
    }

    public String getLastLoginTimeStart() {
        return this.lastLoginTimeStart;
    }

    public void setLastLoginTimeEnd(String lastLoginTimeEnd) {
        this.lastLoginTimeEnd = lastLoginTimeEnd;
    }

    public String getLastLoginTimeEnd() {
        return this.lastLoginTimeEnd;
    }

}
