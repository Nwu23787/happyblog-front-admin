package com.happyblog.task;

import com.happyblog.entity.config.AppConfig;
import com.happyblog.entity.constants.Constants;
import com.happyblog.utils.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

@Component("sysTask")
public class SysTask {

    @Resource
    private AppConfig appConfig;

    /**
     * 清除临时文件
     */
    @Scheduled(cron = "0 0 5 * * ?")
    public void cleanTempImage() {
        String folderPath = appConfig.getFileFolder() + Constants.FILE_FOLDER_IMAGE + Constants.FILE_FOLDER_TEMP_2;
        File folder = new File(folderPath);
        Date hour5Ago = DateUtil.hourAgo(5);
        if (folder != null) {
            File[] fileList = folder.listFiles();
            for (File f : fileList) {
                if (f.lastModified() < hour5Ago.getTime()) {
                    f.delete();
                }
            }
        }
    }
}
