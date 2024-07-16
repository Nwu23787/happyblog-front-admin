package com.happyblog.entity.dto;

public class BlogSaveDto {


    /**
     * 博客ID
     */
    private String blogId;

    /**
     * 父ID
     */
    private String pBlogId;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 封面
     */
    private String cover;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 内容
     */
    private String content;

    /**
     * markdown编辑内容
     */
    private String markdownContent;

    /**
     * 0:富文本 1:markdown编辑器
     */
    private Integer editorType;

    /**
     * 标签
     */
    private String tag;

    /**
     * 0:原创 1:转载
     */
    private Integer type;

    /**
     * 转载地址
     */
    private String reprintUrl;

    /**
     * 0:不允许评论 1:允许评论
     */
    private Integer allowComment;

    /**
     * 0:博客  1:专题
     */
    private Integer blogType;

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getpBlogId() {
        return pBlogId;
    }

    public void setpBlogId(String pBlogId) {
        this.pBlogId = pBlogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMarkdownContent() {
        return markdownContent;
    }

    public void setMarkdownContent(String markdownContent) {
        this.markdownContent = markdownContent;
    }

    public Integer getEditorType() {
        return editorType;
    }

    public void setEditorType(Integer editorType) {
        this.editorType = editorType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReprintUrl() {
        return reprintUrl;
    }

    public void setReprintUrl(String reprintUrl) {
        this.reprintUrl = reprintUrl;
    }

    public Integer getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Integer allowComment) {
        this.allowComment = allowComment;
    }

    public Integer getBlogType() {
        return blogType;
    }

    public void setBlogType(Integer blogType) {
        this.blogType = blogType;
    }
}
