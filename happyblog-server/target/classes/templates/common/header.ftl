<#macro top activePath prePath>
    <#assign navList=[
    {'path':'index.html','name':'博客'},
    {'path':'category.html','name':'分类专栏'},
    {'path':'special.html','name':'专题'},
    {'path':'user.html','name':'成员'},
    {'path':'comment.html','name':'留言板'}
    ]/>
    <div class="top">
        <div class="top-container">
            <div class="logo">
                <#if logoPath??&&logoPath!=''>
                    <img src="${logoPath}">
                <#else>
                    <a href="<#if prePath>../</#if>${navList[0].path}">我是程序员</a>
                </#if>
            </div>
            <#list navList as item>
                <a href="<#if prePath>../</#if>${item.path}" class="nav ${(item.path==activePath)?string('active','')}">${item.name}</a>
            </#list>
        </div>
    </div>
</#macro>