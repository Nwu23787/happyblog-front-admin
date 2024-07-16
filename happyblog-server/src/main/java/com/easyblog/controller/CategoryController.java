package com.happyblog.controller;

import com.happyblog.annotation.Auth;
import com.happyblog.entity.dto.SessionAdminUserDto;
import com.happyblog.entity.enums.CategoryTypeEnum;
import com.happyblog.entity.enums.PageSize;
import com.happyblog.entity.enums.RoleTypeEnum;
import com.happyblog.entity.po.Blog;
import com.happyblog.entity.po.BlogCategory;
import com.happyblog.entity.query.BlogCategoryQuery;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.entity.vo.ResponseVO;
import com.happyblog.exception.BusinessException;
import com.happyblog.service.BlogCategoryService;
import com.happyblog.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * 博客分类 控制层
 */
@RestController("categoryController")
@RequestMapping("/api/category")
public class CategoryController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Resource
    private BlogCategoryService blogCategoryService;

    @Resource
    private ImageUtils imageUtils;


    @RequestMapping("/loadAllCategory4Blog")
    public ResponseVO loadAllCategory4Blog() {
        BlogCategoryQuery param = new BlogCategoryQuery();
        param.setOrderBy("sort asc");
        param.setQueryBlogCount(true);
        param.setCategoryType(CategoryTypeEnum.BLOG.getType());
        List<BlogCategory> result = this.blogCategoryService.findListByParam(param);
        return getSuccessResponseVO(result);
    }

    /**
     * @Description: 保存博客
     * @auther: 多多洛
     * @param: [blogCategory]
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("/saveCategory4Blog")
    @Auth(roleType = RoleTypeEnum.ROLE_ADMIN)
    public ResponseVO saveCategory4Blog(HttpSession session, BlogCategory blogCategory) {
        try {
            SessionAdminUserDto sessionAdminUserDto = getSessionUserInfo(session);
            blogCategory.setCategoryType(CategoryTypeEnum.BLOG.getType());
            blogCategory.setCover(imageUtils.resetImage(blogCategory.getCover(), null));
            this.blogCategoryService.saveBlogCategory(blogCategory, sessionAdminUserDto.getRoleType());
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            logger.error("保存博客分类失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("保存博客分类失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("/changeCategorySort4Blog")
    @Auth(roleType = RoleTypeEnum.ROLE_ADMIN)
    public ResponseVO changeCategorySort(@RequestBody List<BlogCategory> categoryList) {
        try {
            this.blogCategoryService.changeCategorySort(categoryList);
            return getSuccessResponseVO(null);
        } catch (Exception e) {
            logger.error("修改博客分类排序失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("/delCategory4Blog")
    @Auth(roleType = RoleTypeEnum.ROLE_ADMIN)
    public ResponseVO delCategory4Blog(Integer categoryId) {
        try {
            this.blogCategoryService.deleteByCategoryId(categoryId);
            return getSuccessResponseVO(null);
        } catch (Exception e) {
            logger.error("删除博客分类失败", e);
            return getServerErrorResponseVO();
        }
    }

    /**
     * 分页获取专题
     */

    @RequestMapping("/loadCategory4Special")
    public ResponseVO loadCategory4Special(BlogCategoryQuery param) {
        param.setOrderBy("category_id desc");
        param.setQueryBlogCount(true);
        param.setCategoryType(CategoryTypeEnum.SPECIAL.getType());
        PaginationResult<BlogCategory> result = this.blogCategoryService.findListByPage(param);
        return getSuccessResponseVO(result);
    }

    /**
     * @Description: 保存博客
     * @auther: 多多洛
     * @param: [blogCategory]
     * @return: com.happyblog.entity.vo.AjaxResponseVO
     */
    @RequestMapping("/saveCategory4Special")
    public ResponseVO saveCategory4Special(HttpSession session, BlogCategory blogCategory) {
        try {
            SessionAdminUserDto sessionAdminUserDto = getSessionUserInfo(session);
            blogCategory.setCategoryType(CategoryTypeEnum.SPECIAL.getType());
            blogCategory.setCover(imageUtils.resetImage(blogCategory.getCover(), null));
            blogCategory.setUserId(sessionAdminUserDto.getUserId());
            blogCategory.setNickName(sessionAdminUserDto.getNickName());
            this.blogCategoryService.saveBlogCategory(blogCategory, sessionAdminUserDto.getRoleType());
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            logger.error("保存博客分类失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("保存博客分类失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("/delCategory4Special")
    public ResponseVO delCategory4Special(HttpSession session, Integer categoryId) {
        try {
            //管理员可以删除所有分类，用户只能删除自己
            SessionAdminUserDto sessionAdminUserDto = getSessionUserInfo(session);
            if (RoleTypeEnum.ROLE_ADMIN.getType().equals(sessionAdminUserDto.getRoleType())) {
                this.blogCategoryService.deleteByCategoryId(categoryId);
            } else {
                this.blogCategoryService.deleteByCategoryId4Special(categoryId, sessionAdminUserDto.getUserId());
            }
            return getSuccessResponseVO(null);
        } catch (Exception e) {
            logger.error("删除博客分类失败", e);
            return getServerErrorResponseVO();
        }
    }
}