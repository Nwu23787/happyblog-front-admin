package com.happyblog.controller;

import com.happyblog.entity.constants.Constants;
import com.happyblog.entity.dto.BlogSaveDto;
import com.happyblog.entity.dto.SessionAdminUserDto;
import com.happyblog.entity.enums.*;
import com.happyblog.entity.po.Blog;
import com.happyblog.entity.po.BlogCategory;
import com.happyblog.entity.po.BlogTeamUser;
import com.happyblog.entity.query.BlogQuery;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.entity.vo.ResponseVO;
import com.happyblog.exception.BusinessException;
import com.happyblog.service.BlogCategoryService;
import com.happyblog.service.BlogService;
import com.happyblog.service.BlogTeamUserService;
import com.happyblog.utils.CopyTools;
import com.happyblog.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController("blogController")
@RequestMapping("/api/blog")
public class BlogController extends ABaseController {


    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Resource
    private BlogService blogService;

    @Resource
    private BlogCategoryService blogCategoryService;

    @Resource
    private BlogTeamUserService blogTeamUserService;

    @RequestMapping("/loadBlog")
    public ResponseVO saveBlog(BlogQuery blogQuery) {
        blogQuery.setOrderBy("create_time desc");
        blogQuery.setDelType(BlogDelTypeEnum.NORMAL.getType());
        blogQuery.setBlogType(BlogTypeEnum.BLOG.getType());
        PaginationResult result = this.blogService.findListByPage(blogQuery);
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/getBlogById")
    public ResponseVO getBlogById(String blogId) {
        Blog blog = this.blogService.getBlogByBlogId(blogId);
        return getSuccessResponseVO(blog);
    }

    private void checkAuth(HttpSession session, String blogId) throws BusinessException {
        SessionAdminUserDto sessionAdminUserDto = getSessionUserInfo(session);
        //非管理员只能操作自己的博客
        if (!StringTools.isEmpty(blogId) && !sessionAdminUserDto.getRoleType().equals(RoleTypeEnum.ROLE_ADMIN.getType())) {
            Blog dbInfo = blogService.getBlogByBlogId(blogId);
            if (dbInfo != null && !dbInfo.getUserId().equals(sessionAdminUserDto.getUserId())) {
                throw new BusinessException(ResponseCodeEnum.CODE_902);
            }
        }
    }

    @RequestMapping("/saveBlog")
    public ResponseVO saveBlog(HttpSession session, BlogSaveDto blogSaveDto) {
        try {
            this.checkAuth(session, blogSaveDto.getBlogId());
            SessionAdminUserDto userDto = getSessionUserInfo(session);
            Blog blog = CopyTools.copy(blogSaveDto, Blog.class);
            blog.setUserId(userDto.getUserId());
            blog.setNickName(userDto.getNickName());
            blog.setStatus(BlogStatusEnum.STATUS_1.getStatus());
            blog.setDelType(BlogDelTypeEnum.NORMAL.getType());
            blog.setBlogType(BlogTypeEnum.BLOG.getType());
            this.blogService.saveBlog(blog);
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            logger.error("保存博客失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("保存博客失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("/autoSaveBlog")
    public ResponseVO autoSaveBlog(HttpSession session, String blogId, Integer editorType, String title, String content, String markdownContent) {
        try {
            this.checkAuth(session, blogId);
            SessionAdminUserDto userDto = getSessionUserInfo(session);
            Blog blog = new Blog();
            if (StringTools.isEmpty(blogId)) {
                blogId = StringTools.getRandomString(Constants.LENGTH_10);
                blog.setBlogId(blogId);
                blog.setTitle(title);
                blog.setEditorType(editorType);
                blog.setUserId(userDto.getUserId());
                blog.setNickName(userDto.getNickName());
                blog.setStatus(BlogStatusEnum.STATUS_0.getStatus());
                blog.setContent(content);
                blog.setMarkdownContent(markdownContent);
                blog.setCreateTime(new Date());
                blog.setDelType(BlogDelTypeEnum.NORMAL.getType());
                blog.setBlogType(BlogTypeEnum.BLOG.getType());
                blogService.add(blog);
            } else {
                blog.setBlogId(blogId);
                blog.setTitle(title);
                blog.setContent(content);
                blog.setMarkdownContent(markdownContent);
                blogService.updateByBlogId(blog, blog.getBlogId());
            }
            return getSuccessResponseVO(blog.getBlogId());
        } catch (BusinessException e) {
            logger.error("保存博客失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("保存博客失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("/recoveryBlog")
    public ResponseVO recoveryBlog(HttpSession session, String blogId) {
        try {
            this.checkAuth(session, blogId);
            Blog blog = new Blog();
            blog.setDelType(BlogDelTypeEnum.DEL.getType());
            Blog dbInfo = blogService.getBlogByBlogId(blogId);
            if (BlogTypeEnum.SPECIAL.getType().equals(dbInfo.getBlogType())) {
                BlogQuery blogQuery = new BlogQuery();
                blogQuery.setpBlogId(dbInfo.getBlogId());
                blogQuery.setBlogType(BlogTypeEnum.SPECIAL.getType());
                blogQuery.setDelType(BlogDelTypeEnum.NORMAL.getType());
                Integer childCount = blogService.findCountByParam(blogQuery);
                if (childCount > 0) {
                    throw new BusinessException("请先删除下级文章");
                }
            }
            this.blogService.updateByBlogId(blog, blogId);
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            logger.error("删除博客失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("删除博客失败", e);
            return getServerErrorResponseVO();
        }
    }


    @RequestMapping("/loadRecoveryList")
    public ResponseVO loadRecoveryList(BlogQuery blogQuery) {
        blogQuery.setOrderBy("last_update_time desc");
        blogQuery.setDelType(BlogDelTypeEnum.DEL.getType());
        PaginationResult result = this.blogService.findListByPage(blogQuery);
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/delBlog")
    public ResponseVO delBlog(HttpSession session, String blogId) {
        try {
            this.checkAuth(session, blogId);
            this.blogService.deleteByBlogId(blogId);
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            logger.error("删除博客失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("删除博客失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("/reductionBlog")
    public ResponseVO reductionBlog(HttpSession session, String blogId) {
        try {
            this.checkAuth(session, blogId);
            Blog blog = new Blog();
            blog.setDelType(BlogDelTypeEnum.NORMAL.getType());
            this.blogService.updateByBlogId(blog, blogId);
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            logger.error("恢复博客失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("恢复博客失败", e);
            return getServerErrorResponseVO();
        }
    }

    //专题
    @RequestMapping("/loadBlog4Special")
    public ResponseVO loadBlog4special(BlogQuery blogQuery) {
        blogQuery.setOrderBy("create_time desc");
        blogQuery.setDelType(BlogDelTypeEnum.NORMAL.getType());
        blogQuery.setBlogType(BlogTypeEnum.SPECIAL.getType());
        PaginationResult result = this.blogService.findListByPage(blogQuery);
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/getSpecialInfo")
    public ResponseVO getSpecialInfo(Integer categoryId, Integer showType) {
        try {
            List<Blog> resultList = blogService.getBlogListTree(categoryId, showType);
            return getSuccessResponseVO(resultList);
        } catch (Exception e) {
            logger.error("获取专题详情失败", e);
            return getServerErrorResponseVO();
        }
    }


    @RequestMapping("/saveBlog4Special")
    public ResponseVO saveBlog4special(HttpSession session, BlogSaveDto blogSaveDto) {
        try {
            this.checkAuth(session, blogSaveDto.getBlogId());
            SessionAdminUserDto userDto = getSessionUserInfo(session);
            Blog blog = CopyTools.copy(blogSaveDto, Blog.class);
            blog.setUserId(userDto.getUserId());
            blog.setNickName(userDto.getNickName());
            blog.setStatus(BlogStatusEnum.STATUS_1.getStatus());
            blog.setDelType(BlogDelTypeEnum.NORMAL.getType());
            blog.setBlogType(BlogTypeEnum.SPECIAL.getType());
            this.blogService.saveBlog(blog);
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            logger.error("保存专题博客失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("保存专题博客失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("/addBlogTitle4Special")
    public ResponseVO addBlogTitle4Special(HttpSession session, String pBlogId, Integer categoryId, String title) {
        try {
            SessionAdminUserDto userDto = getSessionUserInfo(session);
            BlogTeamUser teamUser = blogTeamUserService.getBlogTeamUserByUserId(userDto.getUserId());
            Blog blog = new Blog();
            String blogId = StringTools.getRandomString(Constants.LENGTH_10);
            blog.setBlogId(blogId);
            blog.setpBlogId(pBlogId);
            blog.setTitle(title);
            if (categoryId != null) {
                BlogCategory blogCategory = blogCategoryService.getBlogCategoryByCategoryId(categoryId);
                if (blogCategory != null) {
                    blog.setCategoryId(categoryId);
                    blog.setCategoryName(blogCategory.getCategoryName());
                }
            }
            blog.setEditorType(teamUser.getEditorType());
            blog.setUserId(userDto.getUserId());
            blog.setNickName(userDto.getNickName());
            blog.setStatus(BlogStatusEnum.STATUS_0.getStatus());
            blog.setCreateTime(new Date());
            blog.setDelType(BlogDelTypeEnum.NORMAL.getType());
            blog.setBlogType(BlogTypeEnum.SPECIAL.getType());
            blogService.add(blog);
            return getSuccessResponseVO(blog.getBlogId());
        } catch (BusinessException e) {
            logger.error("新增专题博客失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("新增专题博客失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("/autoSaveBlog4Special")
    public ResponseVO autoSaveBlog4special(HttpSession session, Integer categoryId, String pBlogId, String blogId, Integer editorType, String title, String content,
                                           String markdownContent) {
        try {
            this.checkAuth(session, blogId);
            SessionAdminUserDto userDto = getSessionUserInfo(session);
            Blog blog = new Blog();
            if (StringTools.isEmpty(blogId)) {
                blogId = StringTools.getRandomString(Constants.LENGTH_10);
                blog.setBlogId(blogId);
                blog.setTitle(title);
                blog.setEditorType(editorType);
                blog.setUserId(userDto.getUserId());
                blog.setNickName(userDto.getNickName());
                blog.setStatus(BlogStatusEnum.STATUS_0.getStatus());
                blog.setContent(content);
                blog.setMarkdownContent(markdownContent);
                blog.setCreateTime(new Date());
                blog.setDelType(BlogDelTypeEnum.NORMAL.getType());
                blog.setBlogType(BlogTypeEnum.SPECIAL.getType());
                blog.setCategoryId(categoryId);
                blog.setpBlogId(pBlogId);
                blogService.add(blog);
            } else {
                blog.setBlogId(blogId);
                blog.setTitle(title);
                blog.setContent(content);
                blog.setMarkdownContent(markdownContent);
                blogService.updateByBlogId(blog, blog.getBlogId());
            }
            return getSuccessResponseVO(blog.getBlogId());
        } catch (BusinessException e) {
            logger.error("保存博客失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("保存博客失败", e);
            return getServerErrorResponseVO();
        }
    }


    /**
     * @Description: 修改博客排序
     * @auther: 多多洛
     * @date: 2022/9/21
     * @param: [blogId, pBlogId, blogIds]
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("/updateSpecialBlogSort")
    public ResponseVO updateSpecialBlogSort(String blogId, String pBlogId, String blogIds) {
        try {
            blogService.updateSpecialBlogSort(blogId, pBlogId, blogIds);
            return getSuccessResponseVO(null);
        } catch (Exception e) {
            logger.error("修改博客排序失败", e);
            return getServerErrorResponseVO();
        }
    }
}
