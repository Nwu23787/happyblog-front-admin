package com.happyblog.entity.config;

import com.happyblog.utils.StringTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class AppConfig {

    /**
     * 文件目录
     */
    @Value("${file.folder:}")
    private String fileFolder;

    @Value("${html.out.folder:}")
    private String htmlOutFolder;

    @Value("${isTest}")
    private Boolean isTest;

    @Value("${web.api.open:false}")
    private Boolean openWebApi;


    @Value("${image.compress:false}")
    private Boolean imageCompress;

    public String getHtmlOutFolder() {
        if (!StringTools.isEmpty(htmlOutFolder) && !htmlOutFolder.endsWith("/")) {
            htmlOutFolder = htmlOutFolder + "/";
        }
        return htmlOutFolder;
    }

    public Boolean getTest() {
        return isTest;
    }

    public String getFileFolder() {
        if (!StringTools.isEmpty(fileFolder) && !fileFolder.endsWith("/")) {
            fileFolder = htmlOutFolder + "/";
        }
        return fileFolder;
    }

    public Boolean getOpenWebApi() {
        return openWebApi;
    }

    public Boolean getImageCompress() {
        return imageCompress;
    }
}
