package com.happyblog.service;

import com.happyblog.entity.po.Blog;
import com.happyblog.entity.query.BlogQuery;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.entity.vo.web.BlogVO;
import com.happyblog.exception.BusinessException;

import java.util.List;


/**
 * 业务接口
 */
public interface BlogService {

    /**
     * 根据条件查询列表
     */
    List<Blog> findListByParam(BlogQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(BlogQuery param);

    /**
     * 分页查询
     */
    PaginationResult<Blog> findListByPage(BlogQuery param);

    /**
     * 新增
     */
    Integer add(Blog bean) throws BusinessException;

    /**
     * 批量新增
     */
    Integer addBatch(List<Blog> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<Blog> listBean);

    /**
     * 根据BlogId修改
     */
    Integer updateByBlogId(Blog bean, String blogId);


    /**
     * 根据BlogId删除
     */
    Integer deleteByBlogId(String blogId);


    /**
     * 根据BlogId查询对象
     */
    Blog getBlogByBlogId(String blogId);

    void saveBlog(Blog blog) throws BusinessException;

    List<Blog> getBlogListTree(Integer categoryId, Integer showType);

    List<BlogVO> getBlogListTree4VO(Integer categoryId, Integer showType);

    void updateSpecialBlogSort(String blogId, String pBlogId, String blogIds);

}