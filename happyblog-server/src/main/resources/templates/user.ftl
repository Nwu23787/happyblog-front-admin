<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>博客成员 - 我是程序员</title>
    <link rel="stylesheet" href="resource/static/css/user.css">
    <script src="resource/static/plugin/jquery/jquery.min.js"></script>
    <script>
        $(document).ready(function (e) {
            let url = document.location.href;
            if (url.indexOf("#") != -1) {
                let id = url.substr(url.indexOf("#") + 1);
                $("html,body").animate({scrollTop: $("#" + id).offset().top - 80}, 100);
            }
        });
    </script>
</head>
<#import "./common/header.ftl" as t/>
<body>
<@t.top activePath="user.html" prePath=false/>
<div class="body-container">
    <div class="user-container">
        <#list teamUserList as item>
            <div class="user-item" id="${item.userId}">
                <div class="user-icon">
                    <#if item.avatar??&&item.avatar!=''>
                        <img src="resource/image/${item.avatar}">
                    <#else>
                        <img src="resource/static/default_avatar.png">
                    </#if>
                </div>
                <div class="user-info">
                    <div class="nick-name">${item.nickName}</div>
                    <div class="profession">
                        <span>职位：${item.profession!'--'}</span>
                        <span class="blog-count">博客：${(item.blogCount)!0}篇</span>
                    </div>
                    <div class="introduction">${(item.introduction)!'这个人超级懒，啥也没留下^_^'}</div>
                </div>
            </div>
        </#list>
    </div>
</div>
<#import "./common/footer.ftl" as f/>
<@f.footer icon="./resource/static/beian.png" />
</body>
</html>