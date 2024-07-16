<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页 - 我是程序员</title>
    <meta name="keywords" content="一个可以生成静态页面，支持git pages的博客">
    <link rel="stylesheet" href="resource/static/css/index.css">
</head>
<#import "./common/pagination.ftl" as p/>
<#import "./common/header.ftl" as t/>
<body>
<@t.top activePath="index.html" prePath=false/>
<div class="body-container">
    <div class="home-body">
        <div class="container">
            <#list blogList.list as item>
                <div class="blog-item">
                    <#if item.cover??&&item.cover!=''>
                        <div class="blog-cover">
                            <img src="resource/image/${item.cover}">
                        </div>
                    </#if>
                    <div class="blog-item-content">
                        <div class="title"><a href="blog/${item.blogId}.html">${item.title}</a></div>
                        <div class="blog-summary">${item.summary!''}</div>
                        <div class="blog-info">
                            <div class="create-time">${item.createTime?string('yyyy-MM-dd hh:mm:ss')}</div>
                            <div class="nick-name">作者：<a href="user.html#${item.userId?c}">${item.nickName}</a></div>
                            <div class="nick-name">分类专栏：
                                <#if item.categoryId??&&item.categoryId!=0>
                                    <a href="category/${item.categoryId?c}.html">${item.categoryName}</a>
                                <#else>
                                    全部
                                </#if>
                            </div>
                        </div>
                    </div>

                </div>
            </#list>
            <@p.pagination pageNo=blogList.pageNo  pageTotal=blogList.pageTotal toURL="index" />
            <#if (blogList.list?size==0)>
                <div class="no-data">空空如也</div>
            </#if>
        </div>
        <div class="right">
            <div class="right-title">
                <span>分类专栏</span>
                <a href="category.html" class="more">更多>></a>
            </div>
            <#list categoryList as item>
                <div>
                    <a href="category/${item.categoryId?c}.html" class="category-item">
                        <span class="category-icon">
                            <#if item.cover??&&item.cover!=''>
                                <img src="resource/image/${item.cover}">
                                <#else>
                                <img src="resource/static/default_img.png">
                            </#if>
                            </span>
                        <span class="category-name" title="${item.categoryName}">${item.categoryName}</span>
                        <span class="blog-count">${item.blogCount}篇</span>
                    </a>
                </div>
            </#list>
            <div class="right-title team-user-title">
                <span>博客成员</span>
                <a href="user.html" class="more">更多>></a>
            </div>
            <#list teamUserList as item>
                <div>
                    <a href="user.html#${item.userId?c}" class="user-item">
                        <span class="user-icon">
                            <#if item.avatar??&&item.avatar!=''>
                                <img src="resource/image/${item.avatar}">
                                <#else>
                                <img src="resource/static/default_avatar.png">
                            </#if>
                        </span>
                        <span class="user-info">
                            <span class="nick-name" title="${item.nickName}">${item.nickName}</span>
                            <span class="profession" title="${item.profession!''}">${item.profession!''}</span>
                        </span>
                        <span class="blog-count">
                            ${item.blogCount!0}篇
                        </span>
                    </a>
                </div>
            </#list>
            <div class="right-title special-title">
                <span>专题</span>
                <a href="special.html" class="more">更多>></a>
            </div>
            <#list specialList as item>
                <div>
                    <#if item.firstBlogId??>
                        <a href="special/${item.firstBlogId}.html" class="category-item">
                        <span class="category-icon">
                            <#if item.cover??&&item.cover!=''>
                                <img src="resource/image/${item.cover}">
                                <#else>
                                <img src="resource/static/default_img.png">
                            </#if>
                            </span>
                            <span class="category-name" title="${item.categoryName}">${item.categoryName}</span>
                            <span class="blog-count">${item.blogCount}篇</span>
                        </a>
                    </#if>
                </div>
            </#list>
        </div>
    </div>
</div>
<#import "./common/footer.ftl" as f/>
<@f.footer icon="./resource/static/beian.png" />
</body>
</html>