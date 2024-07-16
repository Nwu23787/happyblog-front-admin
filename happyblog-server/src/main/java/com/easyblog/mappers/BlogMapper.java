package com.happyblog.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 数据库操作接口
 */
public interface BlogMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 根据BlogId更新
     */
    Integer updateByBlogId(@Param("bean") T t, @Param("blogId") String blogId);


    /**
     * 根据BlogId删除
     */
    Integer deleteByBlogId(@Param("blogId") String blogId);


    /**
     * 根据BlogId获取对象
     */
    T selectByBlogId(@Param("blogId") String blogId);


    void updateCategoryNameByCategoryId(@Param("categoryName") String categoryName, @Param("categoryId") Integer categoryId);

    void updateNickNameByUserId(@Param("nickName") String nickName, @Param("userId") Integer userId);

    void cleanBlogCategory(@Param("categoryId") Integer categoryId);
}
