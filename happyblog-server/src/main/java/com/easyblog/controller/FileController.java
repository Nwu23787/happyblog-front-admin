package com.happyblog.controller;

import com.happyblog.entity.config.AppConfig;
import com.happyblog.entity.constants.Constants;
import com.happyblog.entity.vo.ResponseVO;
import com.happyblog.exception.BusinessException;
import com.happyblog.utils.ProcessUtils;
import com.happyblog.utils.ScaleFilter;
import com.happyblog.utils.StringTools;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController("fileController")
@RequestMapping("/api/file")
public class FileController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Resource
    private AppConfig appConfig;

    /**
     * @Description: 通用下载
     * @auther: 多多洛
     * @date: 21:57 2020/11/28
     * @param: [request, response, downloadFileName, fileName]
     * @return: void
     */
    @RequestMapping("download/{fileName}/{downloadFileName}")
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable("fileName") String fileName,
                         @PathVariable("downloadFileName") String downloadFileName) {
        InputStream in = null;
        OutputStream out = null;
        downloadFileName = StringTools.isEmpty(downloadFileName) ? fileName : downloadFileName;
        String filePath = appConfig.getFileFolder() + Constants.FILE_FOLDER_TEMP + fileName;
        File file = new File(filePath);
        try {
            in = new FileInputStream(file);
            out = response.getOutputStream();
            response.setContentType("application/x-msdownload; charset=UTF-8");
            // 解决中文文件名乱码问题
            if (request.getHeader("User-Agent").toLowerCase().indexOf("msie") > 0) {//IE浏览器
                downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
            } else {
                downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\"");
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len); // write
            }
            out.flush();
        } catch (Exception e) {
            logger.error("下载异常", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                logger.error("IO异常", e);
            }
            try {
                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                logger.error("IO异常", e);
            }

            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description: 件上传
     * @auther: 多多洛
     * @date: 2021/1/30
     * @param: [file]
     * @return: com.easypay.entity.vo.AjaxResponseVO
     */
    @RequestMapping("uploadImage")
    public ResponseVO uploadImage(MultipartFile file, Integer type) {
        try {
            String fileName = file.getOriginalFilename();
            String fileExtName = StringTools.getFileSuffix(fileName);
            if (!ArrayUtils.contains(Constants.IMAGE_SUFFIX, fileExtName)) {
                throw new BusinessException("请选择图片文件上传");
            }
            String path = copyFile(file, type);
            Map<String, String> fileMap = new HashMap();
            fileMap.put("fileName", path);
            return getSuccessResponseVO(fileMap);
        } catch (BusinessException e) {
            logger.error("上传文件异常", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("上传文件异常", e);
            return getServerErrorResponseVO();
        }
    }

    /**
     * 富文本编辑器
     *
     * @param request
     * @return
     */
    @RequestMapping("uploadImage4WangEditor")
    public Map uploadImage4WangEditor(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
            result.put("errno", 0);
            Map<String, String> data = new HashMap<>();
            result.put("data", data);
            while (fileNames.hasNext()) {
                MultipartFile file = multipartHttpServletRequest.getFile(fileNames.next());
                String path = copyFile(file, Constants.IMAGE_TYPE_IMAGE);
                data.put("url", Constants.READ_IMAGE_PATH + path);
            }
            return result;
        } catch (Exception e) {
            logger.error("上传文件异常", e);
            result.put("errno", 1);
            result.put("msg", "上传错误");
            return result;
        }
    }

    private String copyFile(MultipartFile file, Integer type) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileExtName = StringTools.getFileSuffix(fileName);
        String fileRealName = StringTools.getRandomString(Constants.LENGTH_30) + fileExtName;
        String folderPath = appConfig.getFileFolder() + Constants.FILE_FOLDER_TEMP;
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File uploadFile = new File(folderPath + File.separator + fileRealName);
        file.transferTo(uploadFile);
        //生成缩略图
        if (Constants.IMAGE_TYPE_COVER.equals(type)) {
            if (appConfig.getImageCompress()) {
                String newFileRealName = StringTools.getRandomString(Constants.LENGTH_30) + fileExtName;
                Boolean compress = ScaleFilter.createThumbnailWidthFFmpeg(uploadFile, 200, new File(folderPath + File.separator + newFileRealName));
                if (compress) {
                    fileRealName = newFileRealName;
                }
            } else {
                ScaleFilter.createThumbnail(uploadFile, 200, 200, uploadFile);
            }
        } else {
            //压缩图片
            if (appConfig.getImageCompress() && file.getSize() >= Constants.FILE_SIZE_500KB) {
                fileRealName = StringTools.getRandomString(Constants.LENGTH_30) + fileExtName;
                ScaleFilter.compressImageWidthPercentage(uploadFile, new BigDecimal(0.7), new File(folderPath + File.separator + fileRealName));
            }
        }

        return Constants.FILE_FOLDER_TEMP_2 + "/" + fileRealName;
    }

    @RequestMapping("/getImage/{imageFolder}/{imageName}")
    public void readPic(@PathVariable("imageFolder") String imageFolder, @PathVariable("imageName") String imageName, HttpServletResponse response) {
        ServletOutputStream sos = null;
        FileInputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            if (StringTools.isEmpty(imageFolder) || StringUtils.isBlank(imageName)) {
                return;
            }
            String imageSuffix = StringTools.getFileSuffix(imageName);
            String filePath = appConfig.getFileFolder() + Constants.FILE_FOLDER_IMAGE + imageFolder + "/" + imageName;
            if (Constants.FILE_FOLDER_TEMP_2.equals(imageFolder)) {
                filePath = appConfig.getFileFolder() + imageFolder + "/" + imageName;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            imageSuffix = imageSuffix.replace(".", "");
            response.setHeader("Cache-Control", "max-age=2592000");
            response.setContentType("image/" + imageSuffix);
            in = new FileInputStream(file);
            sos = response.getOutputStream();
            baos = new ByteArrayOutputStream();
            int ch = 0;
            while (-1 != (ch = in.read())) {
                baos.write(ch);
            }
            sos.write(baos.toByteArray());
        } catch (Exception e) {
            logger.error("读取图片异常", e);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
    }
}
