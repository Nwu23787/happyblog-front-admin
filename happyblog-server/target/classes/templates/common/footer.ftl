<#macro footer  icon>
    <#if sysSetting??&&(sysSetting.showIcp)==1>
        <div class="footer">
            <div>
                <span>©2021-${.now?string("yyyy")} ${(sysSetting.icpDomain)!''} All rights reserved. </span>
                <a target="_blank" href="https://beian.miit.gov.cn/">${(sysSetting.icpNo)!''}</a></div>
            <div>
                <img src="${icon}">
                <a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=${(sysSetting.policeNo)!''}">
                    ${(sysSetting.policeProvince)!''}公网安备 ${(sysSetting.policeNo)!''}号
                </a></div>
        </div>
    </#if>
    <script>
        var _hmt = _hmt || [];
        (function () {
            var hm = document.createElement("script");
            hm.src = "https://hm.baidu.com/hm.js?63b3f00d92dbfecbe9376c26282c2599";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>
</#macro>