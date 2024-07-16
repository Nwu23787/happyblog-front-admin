package com.happyblog.controller;

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
import com.happyblog.entity.vo.SimplePage;
import com.happyblog.entity.vo.web.BlogCategoryVO;
import com.happyblog.entity.vo.web.BlogTeamUserVO;
import com.happyblog.entity.vo.web.BlogVO;
import com.happyblog.entity.vo.web.SysSettingVO;
import com.happyblog.exception.BusinessException;
import com.happyblog.service.BlogCategoryService;
import com.happyblog.service.BlogService;
import com.happyblog.service.BlogTeamUserService;
import com.happyblog.utils.CopyTools;
import com.happyblog.utils.StringTools;
import com.happyblog.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("viewController")
@RequestMapping("/api/view")
public class ViewController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(ViewController.class);

    @Resource
    private BlogService blogService;

    @Resource
    private BlogCategoryService blogCategoryService;

    @Resource
    private BlogTeamUserService blogTeamUserService;

    @Resource
    private SysCacheUtils sysCacheUtils;

    /**
     * @Description: 分页获取博客
     * @auther: 多多洛
     * @date: 2022/9/20
     * @param: [pageNo, pageSize]
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("loadBlogList")
    public ResponseVO loadBlogList(Integer pageNo, Integer pageSize, Integer categoryId) {
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setBlogType(BlogTypeEnum.BLOG.getType());
        blogQuery.setDelType(BlogDelTypeEnum.NORMAL.getType());
        blogQuery.setStatus(BlogStatusEnum.STATUS_1.getStatus());
        blogQuery.setOrderBy("create_time desc");
        blogQuery.setPageNo(pageNo);
        blogQuery.setPageSize(pageSize);
        blogQuery.setCategoryId(categoryId);
        blogQuery.setQueryContent(false);
        PaginationResult result = blogService.findListByPage(blogQuery);
        return getSuccessResponseVO(convertPo2VO(result, BlogVO.class));
    }

    /**
     * @Description: 获取分类
     * @auther: 多多洛
     * @date: 2022/9/20
     * @param: [pageSize]
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("loadCategory")
    public ResponseVO loadCategory(Integer pageSize) {
        BlogCategoryQuery categoryQuery = new BlogCategoryQuery();
        categoryQuery.setCategoryType(CategoryTypeEnum.BLOG.getType());
        categoryQuery.setOrderBy("sort asc");
        categoryQuery.setQueryBlogCount(true);
        if (null != pageSize) {
            SimplePage simplePage = new SimplePage(0, pageSize);
            categoryQuery.setSimplePage(simplePage);
        }
        List<BlogCategory> result = blogCategoryService.findListByParam(categoryQuery);
        return getSuccessResponseVO(CopyTools.copyList(result, BlogCategoryVO.class));
    }


    /**
     * @Description: 获取博客成员
     * @auther: 多多洛
     * @date: 2022/9/20
     * @param: [pageSize]
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("loadTeamUser")
    public ResponseVO loadTeamUser(Integer pageSize) {
        BlogTeamUserQuery blogTeamUserQuery = new BlogTeamUserQuery();
        blogTeamUserQuery.setOrderBy("role_type desc");
        blogTeamUserQuery.setStatus(UserStatusEnum.ENABLE.getStatus());
        blogTeamUserQuery.setQueryBlogCount(true);
        blogTeamUserQuery.setPageSize(pageSize);
        if (pageSize != null) {
            SimplePage simplePage = new SimplePage(0, pageSize);
            blogTeamUserQuery.setSimplePage(simplePage);
        }

        List<BlogTeamUser> teamUserList = blogTeamUserService.findListByParam(blogTeamUserQuery);
        return getSuccessResponseVO(CopyTools.copyList(teamUserList, BlogTeamUserVO.class));
    }


    /**
     * @Description: 获取博文详情
     * @auther: 多多洛
     * @date: 2022/9/20
     * @param: [blogId]
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("getBlogDetail")
    public ResponseVO getBlogDetail(String blogId) throws BusinessException {
        if (StringTools.isEmpty(blogId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Blog blog = blogService.getBlogByBlogId(blogId);
        return getSuccessResponseVO(blog);
    }


    /**
     * @Description: 分页获取专题
     * @auther: 多多洛
     * @date: 2022/9/20
     * @param: [pageNo, pageSize]
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("loadSpecial")
    public ResponseVO loadSpecial(Integer pageNo, Integer pageSize) {
        pageSize = pageSize == null ? PageSize.SIZE5.getSize() : pageSize;
        BlogCategoryQuery categoryQuery = new BlogCategoryQuery();
        categoryQuery.setCategoryType(CategoryTypeEnum.SPECIAL.getType());
        categoryQuery.setQueryBlogCount(true);
        categoryQuery.setQueryHaveBlog(true);
        categoryQuery.setPageNo(pageNo);
        categoryQuery.setPageSize(pageSize);
        PaginationResult result = blogCategoryService.findListByPage(categoryQuery);
        return getSuccessResponseVO(convertPo2VO(result, BlogCategoryVO.class));
    }


    /**
     * @Description: 获取分类详情
     * @auther: 多多洛
     * @date: 2022/9/24
     * @param: [pageSize]
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("getCategory")
    public ResponseVO getCategory(Integer categoryId) throws BusinessException {
        if (null == categoryId) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        BlogCategory blogCategory = blogCategoryService.getBlogCategoryByCategoryId(categoryId);
        return getSuccessResponseVO(CopyTools.copy(blogCategory, BlogCategoryVO.class));
    }


    /**
     * @Description: 获取专题文章
     * @auther: 多多洛
     * @date: 2022/9/20
     * @param: [categoryId]
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("getSpecialDetail")
    public ResponseVO getSpecialDetail(Integer categoryId) throws BusinessException {
        if (null == categoryId) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        List<BlogVO> blogList = blogService.getBlogListTree4VO(categoryId, BlogTreeShowTypeEnum.ONLY_BLOG.getType());
        BlogCategory blogCategory = blogCategoryService.getBlogCategoryByCategoryId(categoryId);
        Map<String, Object> result = new HashMap<>();
        result.put("blogList", blogList);
        result.put("blogCategory", CopyTools.copy(blogCategory, BlogCategoryVO.class));
        return getSuccessResponseVO(result);
    }

    /**
     * @Description: 获取系统设置信息
     * @auther: 多多洛
     * @date: 2022/9/24
     * @param: []
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("getSysInfo")
    public ResponseVO getSysInfo() {
        SysSettingDto sysSettingDto = sysCacheUtils.getSysSetting();
        return getSuccessResponseVO(CopyTools.copy(sysSettingDto, SysSettingVO.class));
    }
}
