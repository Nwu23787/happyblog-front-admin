<#--
            =(==)     :判断两个值是否相等

             !=           :............不相等

             >(gt)      :判断左边是否大于右边

             >=(gte)  :.....

             <(lt)       :.....

             <=(lte)    :.....
             -->
<#macro pagination pageNo pageTotal toURL >
    <#if pageTotal gt 1>
        <div class="pagination">
            <#assign start=pageNo-5 />
            <#if start lte 0>
                <#assign start=1 />
            <#elseif start + 10 gt pageTotal>
                <#assign  start=pageTotal - 9 />
            </#if>
            <#if start lte 0>
                <#assign  start=1 />
            </#if>
            <#assign end=start+9 />
            <#if end gt pageTotal>
                <#assign end=pageTotal />
            </#if>

            <#if (end - start) gt 9>
                <#assign  start=end -9 />
            </#if>

            <#if pageTotal gt 1 && pageNo gt 1>
                <a href="${toURL}${(pageNo-1==1)?string('','_'+(pageNo-1))}.html" class="page-no">上一页</a>
            </#if>
            <#list start..end as i>
                <a href="${toURL}${(i-1==0)?string('','_'+i)}.html" class="page-no ${(pageNo == i)?string('active','')}">${ i}</a>
            </#list>
            <#if pageTotal gt 1 && pageNo lt pageTotal>
                <a href="${toURL}_${pageNo+1}.html" class="page-no">下一页</a>
            </#if>

        </div>
    </#if>

</#macro>