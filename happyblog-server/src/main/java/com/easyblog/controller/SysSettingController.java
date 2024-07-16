package com.happyblog.controller;

import com.happyblog.annotation.Auth;
import com.happyblog.entity.config.AppConfig;
import com.happyblog.entity.constants.Constants;
import com.happyblog.entity.dto.SysSettingDto;
import com.happyblog.entity.enums.RoleTypeEnum;
import com.happyblog.entity.vo.ResponseVO;
import com.happyblog.exception.BusinessException;
import com.happyblog.utils.SysCacheUtils;
import com.happyblog.utils.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;

@RestController("sysSettingController")
@RequestMapping("/api/sysSetting")
public class SysSettingController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private SysCacheUtils sysCacheUtils;

    @Resource
    private AppConfig appConfig;

    @RequestMapping("getSysSetting")
    @Auth(roleType = RoleTypeEnum.ROLE_ADMIN)
    public ResponseVO getSysSetting() {
        try {
            return getSuccessResponseVO(sysCacheUtils.getSysSetting());
        } catch (Exception e) {
            logger.error("获取系统设置失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("saveSysSetting")
    @Auth(roleType = RoleTypeEnum.ROLE_ADMIN)
    public ResponseVO saveSysSetting(SysSettingDto sysSettingDto) {
        try {
            sysCacheUtils.refreshSysSettingCache(sysSettingDto);
            return getSuccessResponseVO(null);
        } catch (Exception e) {
            logger.error("保存系统设置失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("createZip")
    @Auth(roleType = RoleTypeEnum.ROLE_ADMIN)
    public ResponseVO createZip() {
        try {
            String path = appConfig.getHtmlOutFolder();
            File file = new File(path);
            if (!file.exists() || !file.isDirectory()) {
                throw new BusinessException("生成文件目录不存在");
            }
            String fileName = ZipUtil.zip(path, appConfig.getFileFolder() + Constants.FILE_FOLDER_TEMP, false);
            return getSuccessResponseVO(fileName);
        } catch (BusinessException e) {
            logger.error("生成zip文件异常", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("生成zip文件异常", e);
            return getServerErrorResponseVO();
        }
    }
}
