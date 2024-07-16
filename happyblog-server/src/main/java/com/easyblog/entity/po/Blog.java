package com.happyblog.entity.po;

import com.happyblog.entity.enums.AllowCommentTypeEnum;
import com.happyblog.entity.enums.BlogStatusEnum;
import com.happyblog.entity.enums.EditorTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import com.happyblog.entity.enums.DateTimePatternEnum;
import com.happyblog.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;


/**
 * 博客
 */
public class Blog implements Serializable {


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
     * 用户ID
     */
    private Integer userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 0:不允许评论 1:允许评论
     */
    private Integer allowComment;

    /**
     * 0:草稿 1:已发布
     */
    private Integer status;

    /**
     * 0:删除 1:正常
     */
    private Integer delType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**
     * 0:博客  1:专题
     */
    private Integer blogType;

    /**
     * 排序号
     */
    private Integer sort;


    private String statusName;

    private String allowCommentTypeName;

    private String typeName;

    private String editorTypeName;

    private List<Blog> children;

    public List<Blog> getChildren() {
        return children;
    }

    public void setChildren(List<Blog> children) {
        this.children = children;
    }

    public String getEditorTypeName() {
        EditorTypeEnum typeEnum = EditorTypeEnum.getByType(editorType);
        return typeEnum == null ? "" : typeEnum.getDesc();
    }

    public String getAllowCommentTypeName() {
        AllowCommentTypeEnum typeEnum = AllowCommentTypeEnum.getByType(allowComment);
        return typeEnum == null ? "" : typeEnum.getDesc();
    }

    public String getStatusName() {
        BlogStatusEnum blogStatusEnum = BlogStatusEnum.getByStatus(status);
        return blogStatusEnum == null ? "" : blogStatusEnum.getDesc();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getBlogId() {
        return this.blogId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return this.cover;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setMarkdownContent(String markdownContent) {
        this.markdownContent = markdownContent;
    }

    public String getMarkdownContent() {
        return this.markdownContent;
    }

    public void setEditorType(Integer editorType) {
        this.editorType = editorType;
    }

    public Integer getEditorType() {
        return this.editorType;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }

    public void setReprintUrl(String reprintUrl) {
        this.reprintUrl = reprintUrl;
    }

    public String getReprintUrl() {
        return this.reprintUrl;
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

    public void setAllowComment(Integer allowComment) {
        this.allowComment = allowComment;
    }

    public Integer getAllowComment() {
        return this.allowComment;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setDelType(Integer delType) {
        this.delType = delType;
    }

    public Integer getDelType() {
        return this.delType;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setBlogType(Integer blogType) {
        this.blogType = blogType;
    }

    public Integer getBlogType() {
        return this.blogType;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return this.sort;
    }

    public String getpBlogId() {
        return pBlogId;
    }

    public void setpBlogId(String pBlogId) {
        this.pBlogId = pBlogId;
    }

    @Override
    public String toString() {
        return "博客ID:" + (blogId == null ? "空" : blogId) + "，父ID:" + (pBlogId == null ? "空" : pBlogId) + "，标题:" + (title == null ? "空" : title) + "，分类ID:" + (categoryId == null ? "空" : categoryId) + "，分类名称:" + (categoryName == null ? "空" : categoryName) + "，封面:" + (cover == null ? "空" : cover) + "，摘要:" + (summary == null ? "空" : summary) + "，内容:" + (content == null ? "空" : content) + "，markdown编辑内容:" + (markdownContent == null ? "空" : markdownContent) + "，0:富文本 1:markdown编辑器:" + (editorType == null ? "空" : editorType) + "，标签:" + (tag == null ? "空" : tag) + "，0:原创 1:转载:" + (type == null ? "空" : type) + "，转载地址:" + (reprintUrl == null ? "空" : reprintUrl) + "，用户ID:" + (userId == null ? "空" : userId) + "，昵称:" + (nickName == null ? "空" : nickName) + "，0:不允许评论 1:允许评论:" + (allowComment == null ? "空" : allowComment) + "，0:草稿 1:已发布:" + (status == null ? "空" : status) + "，0:删除 1:正常:" + (delType == null ? "空" : delType) + "，创建时间:" + (createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，最后更新时间:" + (lastUpdateTime == null ? "空" : DateUtil.format(lastUpdateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，0:博客  1:专题:" + (blogType == null ? "空" : blogType) + "，排序号:" + (sort == null ? "空" : sort);
    }
}
