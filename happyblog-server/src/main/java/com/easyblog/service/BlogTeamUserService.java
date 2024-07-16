package com.happyblog.service;

import com.happyblog.entity.dto.SessionAdminUserDto;
import com.happyblog.entity.po.BlogTeamUser;
import com.happyblog.entity.query.BlogTeamUserQuery;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.exception.BusinessException;

import java.util.List;


/**
 * 业务接口
 */
public interface BlogTeamUserService {

    /**
     * 根据条件查询列表
     */
    List<BlogTeamUser> findListByParam(BlogTeamUserQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(BlogTeamUserQuery param);

    /**
     * 分页查询
     */
    PaginationResult<BlogTeamUser> findListByPage(BlogTeamUserQuery param);

    /**
     * 新增
     */
    Integer add(BlogTeamUser bean) throws BusinessException;

    /**
     * 批量新增
     */
    Integer addBatch(List<BlogTeamUser> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<BlogTeamUser> listBean);

    /**
     * 根据Phone修改
     */
    Integer updateByPhone(BlogTeamUser bean, String phone);


    /**
     * 根据Phone删除
     */
    Integer deleteByPhone(String phone);


    /**
     * 根据Phone查询对象
     */
    BlogTeamUser getBlogTeamUserByPhone(String phone);


    /**
     * 根据UserId修改
     */
    Integer updateByUserId(BlogTeamUser bean, Integer userId);


    /**
     * 根据UserId删除
     */
    Integer deleteByUserId(Integer userId);


    /**
     * 根据UserId查询对象
     */
    BlogTeamUser getBlogTeamUserByUserId(Integer userId);

    void saveTeamUser(BlogTeamUser teamUser) throws BusinessException;

    SessionAdminUserDto login(String phone, String password) throws BusinessException;
}