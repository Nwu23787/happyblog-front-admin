<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>留言板 - 我是程序员</title>
    <link rel="stylesheet" href="resource/static/css/comment.css">
</head>
<#import "./common/header.ftl" as t/>
<body>
<@t.top activePath="comment.html" prePath=false/>
<div class="body-container">
    <div class="container-comment">
        <#if sysSetting??&&sysSetting.openCommentType==1>
        <#if sysSetting.commentId??>
            <div><a class="comment-btn" target="_blank" href="https://support.qq.com/product/${sysSetting.commentId}">我要留言</a></div>
        <#elseif blog??>
            <div id="SOHUCS" sid="${blog.blogId}"></div>
            <script charset="utf-8" type="text/javascript" src="https://cy-cdn.kuaizhan.com/upload/changyan.js"></script>
            <script type="text/javascript">
                window.changyan.api.config({
                    appid: "${(sysSetting.changyanAppId)!''}",
                    conf: '${(sysSetting.changyanAppKey)!''}'
                });
            </script>
        </#if>
        <#else>
            <div class="no-data">博主很懒，没有开放留言板</div>
        </#if>
    </div>

</div>
<#import "./common/footer.ftl" as f/>
<@f.footer icon="./resource/static/beian.png" />
</body>
</html>