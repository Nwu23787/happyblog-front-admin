<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>博客专栏 - 我是程序员</title>
    <link rel="stylesheet" href="resource/static/css/category.css">
</head>
<#import "./common/pagination.ftl" as p/>
<#import "./common/header.ftl" as t/>
<body>
<@t.top activePath="category.html" prePath=false/>
<div class="body-container">
    <div class="category-list">
        <#list categoryList as item>
            <div class="category-panel">
                <div class="category-panel-inner">
                    <div class="category-image">
                        <#if item.cover??&&item.cover!=''>
                            <img src="resource/image/${item.cover}">
                        <#else>
                            <img src="resource/static/default_img.png">
                        </#if>
                    </div>
                    <div class="category-info">
                        <div class="category-title" title="${item.categoryName}"><a href="category/${item.categoryId?c}.html">${item.categoryName}</a></div>
                        <div class="category-desc" title="${item.categoryDesc!''}">${(item.categoryDesc)!''}</div>
                        <div class="blog-count">文章数：${item.blogCount!0}</div>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</div>
<#import "./common/footer.ftl" as f/>
<@f.footer icon="./resource/static/beian.png" />
</body>
</html>