<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${blog.title} - 我是程序员</title>
    <#if blog.tag??&&blog.tag!=''>
        <meta name="keywords" content="${blog.tag}">
    <#else>
        <meta name="keywords" content="${blog.title}">
    </#if>
    <#if blog.summary??&&blog.summary!=''>
        <meta name="description" content="${blog.summary}">
    <#else>
        <meta name="description" content="${blog.title}">
    </#if>

    <link rel="stylesheet" href="../resource/static/plugin/highlight/styles/github.min.css">
    <link rel="stylesheet" href="../resource/static/plugin/viewer/viewer.min.css">
    <script src="../resource/static/plugin/highlight/highlight.min.js"></script>
    <script src="../resource/static/plugin/jquery/jquery.min.js"></script>
    <script src="../resource/static/plugin/viewer/viewer-jquery.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <link rel="stylesheet" href="../resource/static/css/blog.detail.css">
    <script>
        $(document).ready(function (e) {
            createToc();
            $("#content").viewer({
                navbar: false
            });

            $("#content a").each(function (index, element) {
                $(element).attr("target", "_blank");
            })

            $(".icon-arrow").click(function () {
                let nextLiDom = $(this).parent().next("div").eq(0)
                if ($(this).hasClass("arrow-down")) {
                    $(this).removeClass("arrow-down");
                    $(this).addClass("arrow-right");
                    nextLiDom.hide();
                } else {
                    $(this).removeClass("arrow-right");
                    $(this).addClass("arrow-down");
                    nextLiDom.show();
                }
            });

            //目录展开收齐
            $("#toc-title").click(function () {
                let spanDom = $(this).find(".icon-arrow")
                if (spanDom.hasClass("arrow-down")) {
                    spanDom.removeClass("arrow-down");
                    spanDom.addClass("arrow-right");
                    $("#toc").hide();
                } else {
                    spanDom.removeClass("arrow-right");
                    spanDom.addClass("arrow-down");
                    $("#toc").show();
                }
            });
        });

        const createToc = () => {
            const hTagList = ["h1", "h2", "h3", "h4", "h5", "h6", "h7"]
            const children = document.querySelector("#content").children;
            const hList = [];
            for (let i = 0; i < children.length; i++) {
                const node = children[i];
                let tagName = node.tagName;
                tagName = tagName == undefined ? "" : tagName.toLowerCase();
                if (hTagList.indexOf(tagName) != -1) {
                    const id = "hash-node-" + i;
                    node.setAttribute("id", id);
                    hList.push({
                        pId: "0",
                        id: id,
                        level: Number.parseInt(tagName.substring(1, 2)),
                        name: node.innerText,
                    });
                }
            }
            for (let i = 0; i < hList.length; i++) {
                findParentId(hList, hList[i], i);
            }
            const newDataList = convertLine2Tree(hList, "0");
            if (newDataList.length == 0) {
                $("<div class='no-data'>暂无目录信息</div>").appendTo($("#toc"));
                return;
            }
            newDataList.forEach(element => {
                createTree($("#toc"), element);
            })
        }

        const createTree = (pNode, data) => {
            const currentNode = $("<div></div>").appendTo(pNode);
            let extPadding = 26;
            if (data.children.length > 0) {
                extPadding = 10
            }
            const paddingLeft = (data.level - 1) * 16 + extPadding;
            const titleNode = $("<div class='title' style='padding-left:" + paddingLeft + "px'></div>").appendTo(currentNode);
            if (data.children.length > 0) {
                $("<span class='icon-arrow arrow-down'></span>").appendTo(titleNode);
            }
            $("<a href='#" + data.id + "' title='" + data.name + "'>" + data.name + "</a>").appendTo(titleNode);

            if (data.children.length > 0) {
                let subNode = $("<div class='sub-title'></div>").appendTo(currentNode);
                data.children.forEach(item => {
                    createTree(subNode, item);
                })
            }
        }

        //查找父级节点
        const findParentId = (hList, item, index) => {
            for (let i = index - 1; i >= 0; i--) {
                const pNode = hList[i];
                if (pNode.level < item.level) {
                    item.pId = pNode.id;
                    break;
                }
            }
        }

        //将数据转换为树形
        const convertLine2Tree = (hList, pId) => {
            const children = [];
            hList.forEach(element => {
                if (element.pId === pId) {
                    children.push(element);
                    element.children = convertLine2Tree(hList, element.id);
                }
            });
            return children;
        }

        $(window).scroll(function () {
            //console.log($(document).scrollTop());
            $("#right-container").css("top", $(document).scrollTop() + "px");
        });

        function scroll(markId) {
            let url = document.location.href;
            if (url.indexOf("#") == -1) {
                document.location.href = url + "#" + markId;
            } else {
                document.location.href = url.substring(0, url.indexOf("#") + 1) + markId;
            }
            $("html,body").animate({scrollTop: $("#" + markId).offset().top - 80}, 100);
        }
    </script>
</head>
<#import "./common/pagination.ftl" as p/>
<#import "./common/header.ftl" as t/>
<body>
<@t.top activePath="index.html" prePath=true/>
<div class="body-container">
    <div class="blog-detail">
        <div class="container">
            <div class="container-content">
                <div>
                    <div class="blog-title">${blog.title}</div>
                    <div class="blog-info">
                        <div class="time">${blog.createTime?string('yyyy-MM-dd hh:mm:ss')}</div>
                        <div class="nick-name">作者：<a href="../user.html#${blog.userId?c}">${blog.nickName}</a></div>
                        <div class="category">分类专栏：
                            <#if blog.categoryId??&&blog.categoryId!=0>
                                <a href="../category/${blog.categoryId?c}.html">${blog.categoryName}</a>
                            <#else>
                                全部
                            </#if>
                        </div>
                        <#if blog.type==1>
                            <div class="reprint-url">转载：<a href="${blog.reprintUrl!''}" target="_blank">查看原文</a></div>
                        </#if>

                    </div>
                    <div class="content" id="content">${blog.content!''}</div>
                </div>
            </div>
            <#if sysSetting??&&sysSetting.openCommentType==1&&blog.allowComment==1>
                <div class="container-comment">
                    <div class="comment-title">评论</div>
                    <#if sysSetting.commentId??>
                        <div><a class="comment-btn" target="_blank" href="https://support.qq.com/product/${sysSetting.commentId}">我要评论</a></div>
                    <#else>
                        <div id="SOHUCS" sid="${blog.blogId}"></div>
                        <script charset="utf-8" type="text/javascript" src="https://cy-cdn.kuaizhan.com/upload/changyan.js"></script>
                        <script type="text/javascript">
                            window.changyan.api.config({
                                appid: "${(sysSetting.changyanAppId)!''}",
                                conf: '${(sysSetting.changyanAppKey)!''}'
                            });
                        </script>
                    </#if>
                </div>
            </#if>
        </div>
        <div class="right">
            <div class="container" id="right-container">
                <div class="right-title toc-title">目录结构</div>
                <div id="toc"></div>

                <div class="right-title category-title">
                    <span>分类专栏</span>
                    <a href="../category.html" class="more">更多>></a>
                </div>
                <#list categoryList as item>
                    <a href="../category/${item.categoryId?c}.html" class="category-item">
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