<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>专题 - 我是程序员</title>
    <link rel="stylesheet" href="resource/static/css/category.css">
</head>
<#import "./common/pagination.ftl" as p/>
<#import "./common/header.ftl" as t/>
<body>
<@t.top activePath="special.html" prePath=false/>
<div class="body-container">
    <div style="background: #fff">
        <div class="category-list">
            <#list categoryResult.list as item>
                <div class="category-panel">
                    <div class="category-panel-inner">
                        <div class="category-image">
                            <#if item.cover??&&item.cover!=''>
                                <#assign imgSrc="resource/image/${item.cover}"/>
                            <#else>
                                <#assign imgSrc="resource/static/default_img.png"/>
                            </#if>
                            <#if item.firstBlogId??>
                                <a href="special/${item.firstBlogId}.html">
                                    <img src="${imgSrc}">
                                </a>
                            <#else>
                                <img src="${imgSrc}">
                            </#if>
                        </div>
                        <div class="category-info">
                            <div class="category-title" title="${item.categoryName}">
                                <#if item.firstBlogId??>
                                    <a href="special/${item.firstBlogId}.html">${item.categoryName}</a>
                                </#if>
                            </div>
                            <div class="category-desc" title="${item.categoryDesc!''}">${(item.categoryDesc)!''}</div>
                            <div class="blog-count">文章数：${item.blogCount!0}</div>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
        <#if (categoryResult.list?size==0)>
            <div class="no-data">空空如也</div>
        </#if>
        <div style="padding:0px 0px 10px 20px;">
            <@p.pagination pageNo=categoryResult.pageNo  pageTotal=categoryResult.pageTotal toURL="special" />
        </div>

    </div>
</div>
<#import "./common/footer.ftl" as f/>
<@f.footer icon="./resource/static/beian.png" />
</body>
</html>