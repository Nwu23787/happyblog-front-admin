package com.happyblog.utils;

import com.happyblog.entity.config.AppConfig;
import com.happyblog.entity.dto.SysSettingDto;
import com.happyblog.exception.BusinessException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("sysCacheUtils")
public class SysCacheUtils {
    private Logger logger = LoggerFactory.getLogger(SysCacheUtils.class);
    private static Map<String, Object> SYS_CACHE_MAP = new ConcurrentHashMap();

    private static final String KEY_SYS_SETTING = "sysSetting";

    @Resource
    private AppConfig appConfig;

    public SysSettingDto getSysSetting() {
        return (SysSettingDto) SYS_CACHE_MAP.get(KEY_SYS_SETTING);
    }

    public void refreshSysSettingCache(SysSettingDto sysSettingDto) throws BusinessException {
        try {
            String fileName = appConfig.getFileFolder() + "/sysSetting.config";

            if (null != sysSettingDto && !StringTools.isEmpty(appConfig.getFileFolder())) {
                FileUtils.writeStringToFile(new File(fileName), JsonUtils.convertObj2Json(sysSettingDto),
                        StandardCharsets.UTF_8.name());
            }
            if (new File(fileName).exists()) {
                String sysJson = FileUtils.readFileToString(new File(fileName), StandardCharsets.UTF_8.name());
                if (!StringTools.isEmpty(sysJson)) {
                    SYS_CACHE_MAP.put(KEY_SYS_SETTING, JsonUtils.convertJson2Obj(sysJson, SysSettingDto.class));
                }
            } else {
                sysSettingDto = new SysSettingDto();
                sysSettingDto.setOpenCommentType(0);
                sysSettingDto.setShowIcp(0);
                SYS_CACHE_MAP.put(KEY_SYS_SETTING, sysSettingDto);
            }
        } catch (Exception e) {
            logger.info("刷新系统缓存失败", e);
            throw new BusinessException("刷新系统设置缓存失败");
        }
    }
}
