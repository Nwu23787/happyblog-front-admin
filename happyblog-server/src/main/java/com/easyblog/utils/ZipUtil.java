package com.happyblog.utils;

import com.happyblog.entity.constants.Constants;
import com.happyblog.exception.BusinessException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    static Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    private static final int BUFFER_SIZE = 2 * 1024;


    public static void unzip(String zipFilePath, String targetPath) throws BusinessException {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFilePath, Charset.forName("GBK"));
            Enumeration<?> entryEnum = zipFile.entries();
            if (null != entryEnum) {
                while (entryEnum.hasMoreElements()) {
                    OutputStream os = null;
                    InputStream is = null;
                    try {
                        ZipEntry zipEntry = (ZipEntry) entryEnum.nextElement();
                        if (!zipEntry.isDirectory()) {
                            File targetFile = new File(targetPath + File.separator + zipEntry.getName());
                            if (!targetFile.getParentFile().exists()) {
                                targetFile.getParentFile().mkdirs();
                            }
                            os = new BufferedOutputStream(new FileOutputStream(targetFile));
                            is = zipFile.getInputStream(zipEntry);
                            byte[] buffer = new byte[4096];
                            int readLen = 0;
                            while ((readLen = is.read(buffer, 0, 4096)) > 0) {
                                os.write(buffer, 0, readLen);
                            }
                        }
                    } finally {
                        if (is != null)
                            is.close();
                        if (os != null)
                            os.close();
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException("解压失败");
        } finally {
            try {
                if (null != zipFile) {
                    zipFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String zip(String srcDir, String outPuthPath, Boolean delSourceFile)
            throws BusinessException {
        ZipOutputStream zos = null;
        OutputStream out = null;
        String fileName = System.currentTimeMillis() + Constants.ZIP_SUFFIX;
        try {
            if (srcDir.endsWith("/") || srcDir.endsWith("\\")) {
                srcDir = srcDir.substring(0, srcDir.length() - 1);
            }
            out = new FileOutputStream(outPuthPath + "/" + fileName);
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), true);
            if (delSourceFile) {
                FileUtils.forceDelete(new File(srcDir));
            }
        } catch (Exception e) {
            logger.error("压缩文件异常", e);
            throw new BusinessException("压缩文件失败");
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }

                }
            }
        }
    }
}
