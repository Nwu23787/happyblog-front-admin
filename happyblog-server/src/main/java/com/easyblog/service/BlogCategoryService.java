package com.happyblog.service;

import com.happyblog.entity.po.BlogCategory;
import com.happyblog.entity.query.BlogCategoryQuery;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.exception.BusinessException;

import java.util.List;


/**
 * 博客分类 业务接口
 */
public interface BlogCategoryService {

    /**
     * 根据条件查询列表
     */
    List<BlogCategory> findListByParam(BlogCategoryQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(BlogCategoryQuery param);

    /**
     * 分页查询
     */
    PaginationResult<BlogCategory> findListByPage(BlogCategoryQuery param);

    /**
     * 新增
     */
    Integer add(BlogCategory bean) throws BusinessException;

    /**
     * 批量新增
     */
    Integer addBatch(List<BlogCategory> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<BlogCategory> listBean);

    /**
     * 根据CategoryId修改
     */
    Integer updateByCategoryId(BlogCategory bean, Integer categoryId);


    /**
     * 根据CategoryId删除
     */
    Integer deleteByCategoryId(Integer categoryId);


    /**
     * 根据CategoryId查询对象
     */
    BlogCategory getBlogCategoryByCategoryId(Integer categoryId);

    void saveBlogCategory(BlogCategory blogCategory, Integer roleType) throws BusinessException;

    void changeCategorySort(List<BlogCategory> blogCategoryList);

    Integer deleteByCategoryId4Special(Integer categoryId, Integer userId) throws BusinessException;
}