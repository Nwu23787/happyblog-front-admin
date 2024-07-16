package com.happyblog.controller;

import com.happyblog.entity.config.AppConfig;
import com.happyblog.entity.constants.Constants;
import com.happyblog.entity.dto.SysSettingDto;
import com.happyblog.entity.enums.*;
import com.happyblog.entity.po.Blog;
import com.happyblog.entity.po.BlogCategory;
import com.happyblog.entity.po.BlogTeamUser;
import com.happyblog.entity.query.BlogCategoryQuery;
import com.happyblog.entity.query.BlogQuery;
import com.happyblog.entity.query.BlogTeamUserQuery;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.entity.vo.ResponseVO;
import com.happyblog.exception.BusinessException;
import com.happyblog.service.BlogCategoryService;
import com.happyblog.service.BlogService;
import com.happyblog.service.BlogTeamUserService;
import com.happyblog.utils.ProcessUtils;
import com.happyblog.utils.StringTools;
import com.happyblog.utils.SysCacheUtils;
import com.happyblog.utils.ZipUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController("createHtmlController")
@RequestMapping("/api")
public class CreateHtmlController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(CreateHtmlController.class);

    private static final String TEMPLATE_HOME = "home.ftl";

    private static final String TEMPLATE_CATEGORY = "category.ftl";
    private static final String TEMPLATE_CATEGORY_BLOG = "category_blog.ftl";
    private static final String TEMPLATE_BLOG_DETAIL = "blog_detail.ftl";
    private static final String TEMPLATE_USER = "user.ftl";
    private static final String TEMPLATE_COMMENT = "comment.ftl";

    private static final String TEMPLATE_SPECIAL_CATEGORY = "special_category.ftl";

    private static final String TEMPLATE_SPECIAL_DETAIL = "special_detail.ftl";

    private static final Integer PAGE_SIZE_RIGHT = 5;

    private static final Integer PAGE_SIZE = 15;

    private static final Integer PAGE_SIZE_SPECIAL = 20;

    private static final String RESOURCE_IMAGE = "../resource/image/";

    private static final String RESOURCE_IMAGE2 = "resource/image/";

    private static ExecutorService executor = Executors.newCachedThreadPool();

    @Resource
    private AppConfig appConfig;

    @Resource
    private SysCacheUtils sysCacheUtils;

    @Resource
    private BlogService blogService;

    @Resource
    private BlogCategoryService blogCategoryService;

    @Resource
    private BlogTeamUserService blogTeamUserService;

    /**
     * 生成进度
     */
    private static Map<String, Object> PROGRESS_MAP = new ConcurrentHashMap<>();

    private static final String KEY_PROGRESS = "progress";
    private static final String KEY_ERROR_MSG = "errorMsg";
    private static final String KEY_RESULT = "result";


    static {
        PROGRESS_MAP.put(KEY_PROGRESS, 0);
    }

    @RequestMapping("/checkProgress")
    public ResponseVO checkProgress() {
        return getSuccessResponseVO(PROGRESS_MAP);
    }

    @RequestMapping("/createHtml")
    public ResponseVO createHtml() {
        PROGRESS_MAP.put(KEY_PROGRESS, 0);
        PROGRESS_MAP.put(KEY_RESULT, 1);
        PROGRESS_MAP.put(KEY_ERROR_MSG, "");
        executor.execute(() -> {
            createHtmlDo();
        });
        return getSuccessResponseVO(null);
    }

    private void createHtmlDo() {
        try {
            String outerFolderPath = appConfig.getHtmlOutFolder();
            if (StringTools.isEmpty(outerFolderPath)) {
                throw new BusinessException("文件输出目录没有指定");
            }
            String imageFolderPath = appConfig.getFileFolder() + Constants.FILE_FOLDER_IMAGE;
            File outerFolder = new File(outerFolderPath);
            if (!outerFolder.exists()) {
                outerFolder.mkdirs();
            }
            PROGRESS_MAP.put(KEY_PROGRESS, 5);
            /**
             * 复制图片
             */
            FileUtils.copyDirectoryToDirectory(new File(imageFolderPath), new File(outerFolderPath + Constants.FILE_FOLDER_RESOURCE));

            PROGRESS_MAP.put(KEY_PROGRESS, 40);
            /**
             * 复制静态资源   打成jar 无法复制文件夹，所以直接将项目总的static目录拷贝到服务器上
             */

            ApplicationHome ah = new ApplicationHome(this.getClass());
            String jarName = ah.getSource().getName();
            //jar包运行
            if (jarName.endsWith(".jar")) {
                FileUtils.copyDirectory(new File(getUnzipJarPath() + "/BOOT-INF/classes/static"), new File(outerFolderPath + Constants.FILE_FOLDER_RESOURCE + "/static"));
            } else {
                ClassPathResource resource = new ClassPathResource("/static");
                FileUtils.copyDirectory(resource.getFile(), new File(outerFolderPath + Constants.FILE_FOLDER_RESOURCE + "/static"));
            }
            PROGRESS_MAP.put(KEY_PROGRESS, 50);
            //查询已删除的博文
            BlogQuery blogQuery = new BlogQuery();
            blogQuery.setBlogType(BlogTypeEnum.BLOG.getType());
            blogQuery.setDelType(BlogDelTypeEnum.DEL.getType());
            List<Blog> delBlogList = this.blogService.findListByParam(blogQuery);
            for (Blog blog : delBlogList) {
                File delFile = new File(appConfig.getHtmlOutFolder() + "blog/" + blog.getBlogId() + ".html");
                if (delFile.isFile()) {
                    delFile.delete();
                }
            }
            //删除专题文章
            blogQuery.setBlogType(BlogTypeEnum.SPECIAL.getType());
            blogQuery.setDelType(BlogDelTypeEnum.DEL.getType());
            delBlogList = this.blogService.findListByParam(blogQuery);
            for (Blog blog : delBlogList) {
                File delFile = new File(appConfig.getHtmlOutFolder() + "special/" + blog.getBlogId() + ".html");
                if (delFile.isFile()) {
                    delFile.delete();
                }
            }
            /**
             * 生成页面
             */
            //分页生成首页
            this.createIndex();

            PROGRESS_MAP.put(KEY_PROGRESS, 60);

            //生成分类页
            this.createCategoryList();

            PROGRESS_MAP.put(KEY_PROGRESS, 70);

            //生成专题
            this.createSpecialCategoryList();

            PROGRESS_MAP.put(KEY_PROGRESS, 80);

            //生成成员页
            this.createUser();

            PROGRESS_MAP.put(KEY_PROGRESS, 90);
            //生成评论
            this.createCommont();

            //发布
            SysSettingDto sysSettingDto = sysCacheUtils.getSysSetting();
            if (appConfig.getTest() != null && !appConfig.getTest() && sysSettingDto != null && Constants.ONE.equals(sysSettingDto.getGitPagesType())) {
                String unzipFolderPath = getUnzipJarPath() + "/BOOT-INF/classes/";
                String cmd = "sh " + unzipFolderPath + "git.sh " + outerFolderPath;
                ProcessUtils.executeCommand(cmd, true);
            }
            PROGRESS_MAP.put(KEY_PROGRESS, 100);
        } catch (Exception e) {
            PROGRESS_MAP.put(KEY_ERROR_MSG, e.getMessage());
            PROGRESS_MAP.put(KEY_RESULT, 0);
            logger.error("生成html失败", e);
        }
    }

    private String getUnzipJarPath() {
        ApplicationHome ah = new ApplicationHome(this.getClass());
        String jarName = ah.getSource().getName();
        String docStorePath = ah.getSource().getParentFile().toString();
        String outFolderName = jarName.substring(0, jarName.lastIndexOf("."));
        String unzipFolderPath = docStorePath + "/" + outFolderName;
        return unzipFolderPath;
    }

    private void createIndex() throws BusinessException {
        //分类
        BlogCategoryQuery blogCategoryQuery = new BlogCategoryQuery();
        blogCategoryQuery.setOrderBy("sort asc");
        blogCategoryQuery.setQueryBlogCount(true);
        blogCategoryQuery.setPageSize(PAGE_SIZE_RIGHT);
        blogCategoryQuery.setPageNo(1);
        blogCategoryQuery.setCategoryType(CategoryTypeEnum.BLOG.getType());
        List<BlogCategory> categoryList = blogCategoryService.findListByParam(blogCategoryQuery);

        //博客成员
        BlogTeamUserQuery blogTeamUserQuery = new BlogTeamUserQuery();
        blogTeamUserQuery.setOrderBy("role_type desc");
        blogTeamUserQuery.setStatus(UserStatusEnum.ENABLE.getStatus());
        blogTeamUserQuery.setQueryBlogCount(true);
        blogTeamUserQuery.setPageSize(PAGE_SIZE_RIGHT);
        blogCategoryQuery.setPageNo(1);
        List<BlogTeamUser> teamUserList = blogTeamUserService.findListByParam(blogTeamUserQuery);

        //专题
        blogCategoryQuery = new BlogCategoryQuery();
        blogCategoryQuery.setOrderBy("sort asc");
        blogCategoryQuery.setQueryBlogCount(true);
        blogCategoryQuery.setPageSize(PAGE_SIZE_RIGHT);
        blogCategoryQuery.setPageNo(1);
        blogCategoryQuery.setCategoryType(CategoryTypeEnum.SPECIAL.getType());
        List<BlogCategory> specialList = blogCategoryService.findListByParam(blogCategoryQuery);
        for (BlogCategory blogCategory : specialList) {
            blogCategory.setFirstBlogId(getFirstBlogId(blogCategory.getCategoryId()));
        }

        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setBlogType(BlogTypeEnum.BLOG.getType());
        blogQuery.setStatus(BlogStatusEnum.STATUS_1.getStatus());
        blogQuery.setDelType(BlogDelTypeEnum.NORMAL.getType());
        blogQuery.setPageSize(PAGE_SIZE);
        blogQuery.setOrderBy("create_time desc");
        PaginationResult blogList = blogService.findListByPage(blogQuery);

        for (int pageNo = 1; pageNo <= blogList.getPageTotal(); pageNo++) {
            blogQuery.setPageNo(pageNo);
            blogQuery.setQueryContent(true);
            blogList = blogService.findListByPage(blogQuery);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("sysSetting", sysCacheUtils.getSysSetting());
            dataMap.put("blogList", blogList);
            dataMap.put("categoryList", categoryList);
            dataMap.put("teamUserList", teamUserList);
            dataMap.put("specialList", specialList);
            dataMap.put("type", 1);
            String htmlName = "index.html";
            if (pageNo > 1) {
                htmlName = String.format("index_%d.html", pageNo);
            }
            createHtml(TEMPLATE_HOME, dataMap, htmlName);
            /**
             * 生成详情页面
             */
            for (int i = 0; i < blogList.getList().size(); i++) {
                Blog blog = (Blog) blogList.getList().get(i);
                if (!StringTools.isEmpty(blog.getTag())) {
                    blog.setTag(blog.getTag().replace("|", ","));
                }
                blog.setContent(replaceImagePath(blog.getContent(), RESOURCE_IMAGE));
                htmlName = "blog/" + blog.getBlogId() + ".html";
                dataMap = new HashMap<>();
                dataMap.put("categoryList", categoryList);
                dataMap.put("blog", blog);
                dataMap.put("sysSetting", sysCacheUtils.getSysSetting());
                createHtml(TEMPLATE_BLOG_DETAIL, dataMap, htmlName);
            }
        }
    }

    //生成分类
    private void createCategoryList() throws BusinessException {
        //分类
        BlogCategoryQuery blogCategoryQuery = new BlogCategoryQuery();
        blogCategoryQuery.setOrderBy("sort asc");
        blogCategoryQuery.setQueryBlogCount(true);
        blogCategoryQuery.setCategoryType(CategoryTypeEnum.BLOG.getType());
        List<BlogCategory> categoryList = blogCategoryService.findListByParam(blogCategoryQuery);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("categoryList", categoryList);
        dataMap.put("sysSetting", sysCacheUtils.getSysSetting());
        createHtml(TEMPLATE_CATEGORY, dataMap, "category.html");

        for (BlogCategory blogCategory : categoryList) {
            BlogQuery blogQuery = new BlogQuery();
            blogQuery.setStatus(BlogStatusEnum.STATUS_1.getStatus());
            blogQuery.setDelType(BlogDelTypeEnum.NORMAL.getType());
            blogQuery.setAutoGetSummary(true);
            blogQuery.setPageSize(PAGE_SIZE);
            blogQuery.setOrderBy("create_time desc");
            blogQuery.setCategoryId(blogCategory.getCategoryId());
            PaginationResult blogList = blogService.findListByPage(blogQuery);

            for (int pageNo = 1; pageNo <= blogList.getPageTotal(); pageNo++) {
                blogQuery.setPageNo(pageNo);
                blogList = blogService.findListByPage(blogQuery);
                dataMap = new HashMap<>();
                dataMap.put("sysSetting", sysCacheUtils.getSysSetting());
                dataMap.put("blogList", blogList);
                dataMap.put("categoryList", categoryList);
                dataMap.put("blogCategory", blogCategory);
                String htmlName = "/category/" + blogCategory.getCategoryId() + ".html";
                if (pageNo > 1) {
                    htmlName = String.format("/category/%d_%d.html", blogCategory.getCategoryId(), pageNo);
                }
                createHtml(TEMPLATE_CATEGORY_BLOG, dataMap, htmlName);
            }
        }
    }

    //生成专题首页
    private void createSpecialCategoryList() throws BusinessException {
        //分类
        BlogCategoryQuery blogCategoryQuery = new BlogCategoryQuery();
        blogCategoryQuery.setOrderBy("sort asc");
        blogCategoryQuery.setQueryBlogCount(true);
        blogCategoryQuery.setQueryHaveBlog(true);
        blogCategoryQuery.setCategoryType(CategoryTypeEnum.SPECIAL.getType());
        blogCategoryQuery.setPageSize(PAGE_SIZE_SPECIAL);
        blogCategoryQuery.setPageNo(1);
        PaginationResult<BlogCategory> categoryResult = blogCategoryService.findListByPage(blogCategoryQuery);

        for (int pageNo = 1; pageNo <= categoryResult.getPageTotal(); pageNo++) {
            blogCategoryQuery.setPageNo(pageNo);
            categoryResult = blogCategoryService.findListByPage(blogCategoryQuery);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("sysSetting", sysCacheUtils.getSysSetting());
            dataMap.put("categoryResult", categoryResult);
            for (BlogCategory blogCategory : categoryResult.getList()) {
                blogCategory.setFirstBlogId(getFirstBlogId(blogCategory.getCategoryId()));
            }
            String htmlName = "special.html";
            if (pageNo > 1) {
                htmlName = String.format("special_%d.html", pageNo);
            }
            createHtml(TEMPLATE_SPECIAL_CATEGORY, dataMap, htmlName);

            for (BlogCategory blogCategory : categoryResult.getList()) {
                createSpecialDetail(blogCategory);
            }
        }
    }

    private String getFirstBlogId(Integer categoryId) {
        List<Blog> blogList = blogService.getBlogListTree(categoryId, BlogTreeShowTypeEnum.ONLY_BLOG.getType());
        if (blogList == null || blogList.isEmpty()) {
            return null;
        }
        return blogList.get(0).getBlogId();
    }

    //生成专题详情页面
    private void createSpecialDetail(BlogCategory blogCategory) throws BusinessException {
        List<Blog> blogList = blogService.getBlogListTree(blogCategory.getCategoryId(), BlogTreeShowTypeEnum.ONLY_BLOG.getType());
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setBlogType(BlogTypeEnum.SPECIAL.getType());
        blogQuery.setStatus(BlogStatusEnum.STATUS_1.getStatus());
        blogQuery.setDelType(BlogDelTypeEnum.NORMAL.getType());
        blogQuery.setCategoryId(blogCategory.getCategoryId());
        blogQuery.setAutoGetSummary(true);
        blogQuery.setQueryContent(true);
        blogQuery.setOrderBy("create_time desc");
        List<Blog> allBlogList = blogService.findListByParam(blogQuery);
        for (int i = 0; i < allBlogList.size(); i++) {
            Blog blog = allBlogList.get(i);
            if (!StringTools.isEmpty(blog.getTag())) {
                blog.setTag(blog.getTag().replace("|", ","));
            }
            blog.setContent(replaceImagePath(blog.getContent(), RESOURCE_IMAGE));
            String htmlName = "special/" + blog.getBlogId() + ".html";
            Map dataMap = new HashMap<>();
            dataMap.put("blogList", blogList);
            dataMap.put("blog", blog);
            dataMap.put("blogCategory", blogCategory);
            dataMap.put("sysSetting", sysCacheUtils.getSysSetting());
            createHtml(TEMPLATE_SPECIAL_DETAIL, dataMap, htmlName);
        }
    }

    private void createUser() throws BusinessException {
        BlogTeamUserQuery blogTeamUserQuery = new BlogTeamUserQuery();
        blogTeamUserQuery.setOrderBy("role_type desc");
        blogTeamUserQuery.setStatus(UserStatusEnum.ENABLE.getStatus());
        blogTeamUserQuery.setQueryBlogCount(true);
        List<BlogTeamUser> teamUserList = blogTeamUserService.findListByParam(blogTeamUserQuery);
        for (BlogTeamUser teamUser : teamUserList) {
            teamUser.setIntroduction(replaceImagePath(teamUser.getIntroduction(), RESOURCE_IMAGE2));
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("sysSetting", sysCacheUtils.getSysSetting());
        dataMap.put("teamUserList", teamUserList);
        createHtml(TEMPLATE_USER, dataMap, "user.html");
    }

    private void createCommont() throws BusinessException {
        Map dataMap = new HashMap<>();
        dataMap.put("sysSetting", sysCacheUtils.getSysSetting());
        dataMap.put("sysSetting", sysCacheUtils.getSysSetting());
        createHtml(TEMPLATE_COMMENT, dataMap, "comment.html");
    }

    private String replaceImagePath(String content, String target) {
        if (StringTools.isEmpty(content)) {
            return content;
        }
        content = content.replaceAll(Constants.READ_IMAGE_PATH, target);
        return content;
    }

    private void createHtml(String templateName, Map<String, Object> dataMap, String htmlName) throws BusinessException {
        ByteArrayOutputStream output = null;
        Writer writer = null;
        try {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
            configuration.setClassForTemplateLoading(this.getClass(), "/templates/");
            configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_22));
            configuration.setDefaultEncoding("UTF-8");
            //获取或创建一个模版。
            Template template = configuration.getTemplate(templateName);
            output = new ByteArrayOutputStream();
            writer = new OutputStreamWriter(output, "UTF-8");
            template.process(dataMap, writer);
            byte[] byteArray = output.toByteArray();
            String html = new String(byteArray, "UTF-8");
            File indexFile = new File(appConfig.getHtmlOutFolder() + htmlName);
            FileUtils.write(indexFile, html, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            logger.error("生成html失败", e);
            throw new BusinessException("生成html失败");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
