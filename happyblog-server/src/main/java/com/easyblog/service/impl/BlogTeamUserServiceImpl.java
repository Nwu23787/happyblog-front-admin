package com.happyblog.service.impl;

import com.happyblog.entity.constants.Constants;
import com.happyblog.entity.dto.SessionAdminUserDto;
import com.happyblog.entity.enums.PageSize;
import com.happyblog.entity.enums.RoleTypeEnum;
import com.happyblog.entity.enums.UserStatusEnum;
import com.happyblog.entity.po.Blog;
import com.happyblog.entity.po.BlogTeamUser;
import com.happyblog.entity.query.BlogQuery;
import com.happyblog.entity.query.BlogTeamUserQuery;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.entity.vo.SimplePage;
import com.happyblog.exception.BusinessException;
import com.happyblog.mappers.BlogMapper;
import com.happyblog.mappers.BlogTeamUserMapper;
import com.happyblog.service.BlogTeamUserService;
import com.happyblog.utils.ImageUtils;
import com.happyblog.utils.StringTools;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 业务接口实现
 */
@Service("blogTeamUserService")
public class BlogTeamUserServiceImpl implements BlogTeamUserService {

    @Resource
    private BlogTeamUserMapper<BlogTeamUser, BlogTeamUserQuery> blogTeamUserMapper;

    @Resource
    private BlogMapper<Blog, BlogQuery> blogMapper;

    @Resource
    private ImageUtils imageUtils;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<BlogTeamUser> findListByParam(BlogTeamUserQuery param) {
        return this.blogTeamUserMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(BlogTeamUserQuery param) {
        return this.blogTeamUserMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResult<BlogTeamUser> findListByPage(BlogTeamUserQuery param) {
        int count = this.blogTeamUserMapper.selectCount(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();
        int pageNo = 0;
        if (null != param.getPageNo()) {
            pageNo = param.getPageNo();
        }
        SimplePage page = new SimplePage(pageNo, count, pageSize);
        param.setSimplePage(page);
        List<BlogTeamUser> list = this.blogTeamUserMapper.selectList(param);
        PaginationResult<BlogTeamUser> result = new PaginationResult<>(count, pageSize, pageNo, page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(BlogTeamUser bean) throws BusinessException {
        try {
            return this.blogTeamUserMapper.insert(bean);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("信息已经存在");
        }
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<BlogTeamUser> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.blogTeamUserMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<BlogTeamUser> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.blogTeamUserMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据Phone修改
     */
    @Override
    public Integer updateByPhone(BlogTeamUser bean, String phone) {
        return this.blogTeamUserMapper.updateByPhone(bean, phone);
    }


    /**
     * 根据Phone删除
     */
    @Override
    public Integer deleteByPhone(String phone) {
        return this.blogTeamUserMapper.deleteByPhone(phone);
    }


    /**
     * 根据Phone获取对象
     */
    @Override
    public BlogTeamUser getBlogTeamUserByPhone(String phone) {
        return this.blogTeamUserMapper.selectByPhone(phone);
    }


    /**
     * 根据UserId修改
     */
    @Override
    public Integer updateByUserId(BlogTeamUser bean, Integer userId) {
        return this.blogTeamUserMapper.updateByUserId(bean, userId);
    }


    /**
     * 根据UserId删除
     */
    @Override
    public Integer deleteByUserId(Integer userId) {
        return this.blogTeamUserMapper.deleteByUserId(userId);
    }


    /**
     * 根据UserId获取对象
     */
    @Override
    public BlogTeamUser getBlogTeamUserByUserId(Integer userId) {
        return this.blogTeamUserMapper.selectByUserId(userId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTeamUser(BlogTeamUser teamUser) throws BusinessException {
        BlogTeamUser dbInfo = this.blogTeamUserMapper.selectByPhone(teamUser.getPhone());
        String oldNickName = dbInfo == null ? "" : dbInfo.getNickName();
        if (dbInfo != null && teamUser.getUserId() == null && dbInfo != null || dbInfo != null && !dbInfo.getUserId().equals(teamUser.getUserId())) {
            throw new BusinessException("手机号已经存在");
        }
        String introduction = teamUser.getIntroduction();
        if (!StringTools.isEmpty(introduction)) {
            String month = imageUtils.resetImageHtml(introduction);
            introduction = introduction.replace(Constants.FILE_FOLDER_TEMP_2, month);
            teamUser.setIntroduction(introduction);
        }
        if (teamUser.getUserId() == null) {
            teamUser.setRoleType(RoleTypeEnum.ROLE_TEAM_USER.getType());
            teamUser.setCreateTime(new Date());
            teamUser.setStatus(UserStatusEnum.ENABLE.getStatus());
            teamUser.setPassword(StringTools.encodeByMD5(teamUser.getPassword()));
            this.blogTeamUserMapper.insert(teamUser);
        } else {
            teamUser.setRoleType(null);
            this.blogTeamUserMapper.updateByUserId(teamUser, teamUser.getUserId());
        }
        BlogTeamUserQuery teamUserQuery = new BlogTeamUserQuery();
        teamUserQuery.setNickName(teamUser.getNickName());
        Integer count = this.blogTeamUserMapper.selectCount(teamUserQuery);
        if (count > 1) {
            throw new BusinessException("已经有小伙伴在使用该昵称了，换一个吧");
        }
        if (teamUser.getUserId() != null) {
            if (!teamUser.getNickName().equals(oldNickName)) {
                this.blogMapper.updateNickNameByUserId(teamUser.getNickName(), teamUser.getUserId());
            }
        }
    }

    @Override
    public SessionAdminUserDto login(String phone, String password) throws BusinessException {
        if (StringTools.isEmpty(phone) || StringTools.isEmpty(password)) {
            throw new BusinessException("手机号，密码不能为空");
        }
        BlogTeamUser teamUser = this.blogTeamUserMapper.selectByPhone(phone);
        if (teamUser != null && !UserStatusEnum.ENABLE.getStatus().equals(teamUser.getStatus())) {
            throw new BusinessException("账号已禁用");
        }
        if (null == teamUser || !teamUser.getPassword().equals(password)) {
            throw new BusinessException("账号密码错误");
        }
        BlogTeamUser updateInfo = new BlogTeamUser();
        updateInfo.setLastLoginTime(new Date());
        blogTeamUserMapper.updateByUserId(updateInfo, teamUser.getUserId());

        SessionAdminUserDto sessionUserDto = new SessionAdminUserDto();
        sessionUserDto.setUserId(teamUser.getUserId());
        sessionUserDto.setNickName(teamUser.getNickName());
        sessionUserDto.setAvatar(teamUser.getAvatar());
        sessionUserDto.setRoleType(teamUser.getRoleType());
        return sessionUserDto;
    }
}