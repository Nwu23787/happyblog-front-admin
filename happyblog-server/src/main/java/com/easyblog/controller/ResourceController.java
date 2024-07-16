package com.happyblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Controller("resourceController")
@RequestMapping("/resource")
public class ResourceController {
    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @RequestMapping("/**")
    public void getResource(HttpServletRequest request, HttpServletResponse response) {
        InputStream in = null;
        OutputStream out = null;
        String url = request.getRequestURI();
        try {
            url = url.replace("/resource", "");
            ClassPathResource classPathResource = new ClassPathResource(url);
            in = classPathResource.getInputStream();
            out = response.getOutputStream();
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len); // write
            }
            out.flush();
        } catch (Exception e) {
            logger.error("读取文件失败,文件路径:{}", url);
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

        }
    }
}
