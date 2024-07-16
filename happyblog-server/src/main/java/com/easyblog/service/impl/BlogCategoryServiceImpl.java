package com.happyblog.service.impl;

import com.happyblog.entity.enums.CategoryTypeEnum;
import com.happyblog.entity.enums.PageSize;
import com.happyblog.entity.enums.ResponseCodeEnum;
import com.happyblog.entity.enums.RoleTypeEnum;
import com.happyblog.entity.po.Blog;
import com.happyblog.entity.po.BlogCategory;
import com.happyblog.entity.query.BlogCategoryQuery;
import com.happyblog.entity.query.BlogQuery;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.entity.vo.SimplePage;
import com.happyblog.exception.BusinessException;
import com.happyblog.mappers.BlogCategoryMapper;
import com.happyblog.mappers.BlogMapper;
import com.happyblog.service.BlogCategoryService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 博客分类 业务接口实现
 */
@Service("blogCategoryService")
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Resource
    private BlogCategoryMapper<BlogCategory, BlogCategoryQuery> blogCategoryMapper;

    @Resource
    private BlogMapper<Blog, BlogQuery> blogMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<BlogCategory> findListByParam(BlogCategoryQuery param) {
        return this.blogCategoryMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(BlogCategoryQuery param) {
        return this.blogCategoryMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResult<BlogCategory> findListByPage(BlogCategoryQuery param) {
        int count = this.blogCategoryMapper.selectCount(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();
        int pageNo = 0;
        if (null != param.getPageNo()) {
            pageNo = param.getPageNo();
        }
        SimplePage page = new SimplePage(pageNo, count, pageSize);
        param.setSimplePage(page);
        List<BlogCategory> list = this.blogCategoryMapper.selectList(param);
        PaginationResult<BlogCategory> result = new PaginationResult<>(count, pageSize, pageNo, page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(BlogCategory bean) throws BusinessException {
        try {
            return this.blogCategoryMapper.insert(bean);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("信息已经存在");
        }
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<BlogCategory> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.blogCategoryMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<BlogCategory> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.blogCategoryMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据CategoryId修改
     */
    @Override
    public Integer updateByCategoryId(BlogCategory bean, Integer categoryId) {
        return this.blogCategoryMapper.updateByCategoryId(bean, categoryId);
    }


    /**
     * 根据CategoryId删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByCategoryId(Integer categoryId) {
        this.blogMapper.cleanBlogCategory(categoryId);
        return this.blogCategoryMapper.deleteByCategoryId(categoryId);
    }


    /**
     * 根据CategoryId获取对象
     */
    @Override
    public BlogCategory getBlogCategoryByCategoryId(Integer categoryId) {
        return this.blogCategoryMapper.selectByCategoryId(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBlogCategory(BlogCategory blogCategory, Integer roleType) throws BusinessException {
        if (blogCategory.getCategoryId() == null) {
            Integer categoryCount = this.blogCategoryMapper.selectCount(new BlogCategoryQuery());
            blogCategory.setSort(categoryCount + 1);
            this.blogCategoryMapper.insert(blogCategory);
        } else {
            BlogCategory dbInfo = blogCategoryMapper.selectByCategoryId(blogCategory.getCategoryId());
            if (null == dbInfo) {
                throw new BusinessException("博客分类不存在");
            }
            //成员只能修改自己的专题
            if (RoleTypeEnum.ROLE_TEAM_USER.getType().equals(roleType) && CategoryTypeEnum.SPECIAL.equals(dbInfo.getCategoryType()) && !blogCategory.getUserId().equals(dbInfo.getUserId())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            //更新博客中，分类名称
            if (!dbInfo.getCategoryName().equals(blogCategory.getCategoryName())) {
                this.blogMapper.updateCategoryNameByCategoryId(blogCategory.getCategoryName(), blogCategory.getCategoryId());
            }
            blogCategory.setSort(null);
            this.blogCategoryMapper.updateByCategoryId(blogCategory, blogCategory.getCategoryId());
        }
        BlogCategoryQuery blogCategoryQuery = new BlogCategoryQuery();
        blogCategoryQuery.setCategoryName(blogCategory.getCategoryName());
        Integer count = this.blogCategoryMapper.selectCount(blogCategoryQuery);
        if (count > 1) {
            throw new BusinessException("名称已经存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeCategorySort(List<BlogCategory> blogCategoryList) {
        for (int i = 0; i < blogCategoryList.size(); i++) {
            BlogCategory blogCategory = blogCategoryList.get(i);
            blogCategory.setCover(null);
            blogCategory.setCategoryName(null);
            blogCategory.setSort(i);
            this.blogCategoryMapper.updateByCategoryId(blogCategory, blogCategory.getCategoryId());
        }
    }

    @Override
    public Integer deleteByCategoryId4Special(Integer categoryId, Integer userId) throws BusinessException {
        BlogCategory blogCategory = this.blogCategoryMapper.selectByCategoryId(categoryId);
        if (blogCategory == null || !blogCategory.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        return this.blogCategoryMapper.deleteByCategoryId(categoryId);
    }
}