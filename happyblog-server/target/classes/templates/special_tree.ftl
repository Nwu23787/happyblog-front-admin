<#import "./special_tree.ftl" as b/>
<#macro tree dataList level>
    <#list dataList as item>
        <div>
            <#assign extPadding=26>
            <#if item.children??>
                <#assign extPadding=10>
            </#if>
            <div class="title" style="padding-left:${level*16+extPadding}px" id="${item.blogId}">
                <#if item.children??>
                    <span class="icon-arrow arrow-down"></span>
                </#if>
                <a href="${item.blogId}.html" title="${item.title}">${item.title}</a>
            </div>
            <#if item.children??>
                <div class="sub-title">
                    <@b.tree dataList=item.children level=level+1/>
                </div>
            </#if>
        </div>
    </#list>
</#macro>
