<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>博客专栏 - ${blogCategory.categoryName} - 我是程序员</title>
    <link rel="stylesheet" href="../resource/static/css/category.blog.css">
</head>
<#import "./common/pagination.ftl" as p/>
<#import "./common/header.ftl" as t/>
<body>
<@t.top activePath="category.html" prePath=true/>
<div class="body-container">
    <div class="category-blog-body">
        <div class="container">
            <div class="category-panel">
                <div class="category-image">
                    <#if blogCategory.cover??&&blogCategory.cover!=''>
                        <img src="../resource/image/${blogCategory.cover}">
                    <#else>
                        <img src="../resource/static/default_img.png">
                    </#if>
                </div>
                <div class="category-info">
                    <div class="category-title">${blogCategory.categoryName}</div>
                    <div class="category-desc">${(blogCategory.categoryDesc)!''}</div>
                    <div class="blog-count">文章数：${blogCategory.blogCount!0}</div>
                </div>
            </div>
            <div class="blog-list">
                <#list blogList.list as item>
                    <div class="blog-item">
                        <#if item.cover??&&item.cover!=''>
                            <div class="blog-cover">
                                <img src="../resource/image/${item.cover}">
                            </div>
                        </#if>
                        <div class="blog-item-content">
                            <div class="title"><a href="../blog/${item.blogId}.html">${item.title}</a></div>
                            <div class="blog-summary">${item.summary!''}</div>
                            <div class="blog-info">
                                <div class="create-time">${item.createTime?string('yyyy-MM-dd hh:mm:ss')}</div>
                                <div class="nick-name">作者：<a href="../user.html#${item.userId}">${item.nickName}</a></div>
                                <div class="nick-name">分类专栏：
                                    <#if item.categoryId??&&item.categoryId!=0>
                                        <a href="../category/${item.categoryId?c}.html">${item.categoryName}</a>
                                    <#else>
                                        全部
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
                <@p.pagination pageNo=blogList.pageNo  pageTotal=blogList.pageTotal toURL="${blogCategory.categoryId?c}"/>
                <#if (blogList.list?size==0)>
                    <div class="no-data">空空如也</div>
                </#if>
            </div>
        </div>
        <div class="right">
            <div class="category-panel">
                <div class="right-title ">分类专栏</div>
                <#list categoryList as item>
                    <a class="category-item ${(item.categoryId==blogCategory.categoryId)?string('category-active','')}" href="../category/${item.categoryId?c}.html">
                        <span class="category-icon">
                         <#if item.cover??&&item.cover!=''>
                             <img src="../resource/image/${item.cover}">
                        <#else>
                             <img src="../resource/static/default_img.png">
                         </#if>
                        </span>
                        <span class="category-name" title="${item.categoryName}">${item.categoryName}</span>
                        <span class="blog-count">${item.blogCount}篇</span>
                    </a>
                </#list>
            </div>
        </div>
    </div>
</div>
<#import "./common/footer.ftl" as f/>
<@f.footer icon="../resource/static/beian.png" />
</body>
</html>