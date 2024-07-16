package com.happyblog;

import com.happyblog.exception.BusinessException;
import com.happyblog.utils.SysCacheUtils;
import com.happyblog.utils.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component("initRun")
public class InitRun implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);
    private static ExecutorService executor = Executors.newCachedThreadPool();

    @Resource
    private SysCacheUtils sysCacheUtils;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        executor.execute(() -> {
            try {
                //刷新缓存
                sysCacheUtils.refreshSysSettingCache(null);
                //解压jar
                unzipJar();
            } catch (Exception e) {
                logger.error("系统启动异步执行任务失败", e);
            }
        });
        executor.shutdown();
    }

    private void unzipJar() throws BusinessException {
        ApplicationHome ah = new ApplicationHome(this.getClass());
        String jarName = ah.getSource().getName();
        //jar包运行
        if (!jarName.endsWith(".jar")) {
            return;
        }
        String docStorePath = ah.getSource().getParentFile().toString();
        String outFolderName = jarName.substring(0, jarName.lastIndexOf("."));
        ZipUtil.unzip(docStorePath + "/" + jarName, docStorePath + "/" + outFolderName);
        logger.info("博客后端服务已经启动成功，可以愉快的开发拉!");
    }
}
