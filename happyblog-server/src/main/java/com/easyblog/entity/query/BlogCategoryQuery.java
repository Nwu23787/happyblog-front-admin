package com.happyblog.entity.query;


/**
 * 博客分类参数
 */
public class BlogCategoryQuery extends BaseParam {


    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 封面
     */
    private String cover;

    private String coverFuzzy;

    /**
     * 分类名称
     */
    private String categoryName;

    private String categoryNameFuzzy;

    /**
     * 分类描述
     */
    private String categoryDesc;

    private String categoryDescFuzzy;

    /**
     * 0:博客分类  1:专题
     */
    private Integer categoryType;

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
     * 排序
     */
    private Integer sort;

    private Boolean queryBlogCount;

    private Boolean queryHaveBlog;

    public Boolean getQueryHaveBlog() {
        return queryHaveBlog;
    }

    public void setQueryHaveBlog(Boolean queryHaveBlog) {
        this.queryHaveBlog = queryHaveBlog;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCoverFuzzy(String coverFuzzy) {
        this.coverFuzzy = coverFuzzy;
    }

    public String getCoverFuzzy() {
        return this.coverFuzzy;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryNameFuzzy(String categoryNameFuzzy) {
        this.categoryNameFuzzy = categoryNameFuzzy;
    }

    public String getCategoryNameFuzzy() {
        return this.categoryNameFuzzy;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getCategoryDesc() {
        return this.categoryDesc;
    }

    public void setCategoryDescFuzzy(String categoryDescFuzzy) {
        this.categoryDescFuzzy = categoryDescFuzzy;
    }

    public String getCategoryDescFuzzy() {
        return this.categoryDescFuzzy;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getCategoryType() {
        return this.categoryType;
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

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Boolean getQueryBlogCount() {
        return queryBlogCount;
    }

    public void setQueryBlogCount(Boolean queryBlogCount) {
        this.queryBlogCount = queryBlogCount;
    }
}
