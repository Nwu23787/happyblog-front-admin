package com.happyblog.controller;

import com.happyblog.annotation.Auth;
import com.happyblog.entity.enums.ResponseCodeEnum;
import com.happyblog.entity.enums.RoleTypeEnum;
import com.happyblog.entity.enums.UserStatusEnum;
import com.happyblog.entity.po.BlogTeamUser;
import com.happyblog.entity.query.BlogTeamUserQuery;
import com.happyblog.entity.vo.BlogTeamUserVO;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.entity.vo.ResponseVO;
import com.happyblog.exception.BusinessException;
import com.happyblog.service.BlogTeamUserService;
import com.happyblog.utils.ImageUtils;
import com.happyblog.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


/**
 * 博客成员 控制层
 */
@RestController("blogTeamUserController")
@RequestMapping("/api/setting")
public class BlogTeamUserController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(BlogTeamUserController.class);

    @Resource
    private BlogTeamUserService blogTeamUserService;


    @Resource
    private ImageUtils imageUtils;

    /**
     * 分页查询方法
     */
    @RequestMapping("/loadUser")
    public ResponseVO loadUser(BlogTeamUserQuery teamUserQuery) {
        teamUserQuery.setOrderBy("user_id desc");
        PaginationResult result = this.blogTeamUserService.findListByPage(teamUserQuery);
        return getSuccessResponseVO(convertPo2VO(result, BlogTeamUserVO.class));
    }

    @RequestMapping("/saveTeamUser")
    @Auth(roleType = RoleTypeEnum.ROLE_ADMIN)
    public ResponseVO saveTeamUser(Integer userId, String nickName, String phone, String password, Integer editorType,
                                   String profession, String avatar, String introduction) {
        try {
            BlogTeamUser teamUser = new BlogTeamUser();
            teamUser.setUserId(userId);
            teamUser.setNickName(nickName);
            teamUser.setPhone(phone);
            teamUser.setPassword(password);
            teamUser.setEditorType(editorType);
            teamUser.setProfession(profession);
            teamUser.setAvatar(avatar);
            teamUser.setIntroduction(introduction);
            teamUser.setAvatar(imageUtils.resetImage(teamUser.getAvatar(), null));
            this.blogTeamUserService.saveTeamUser(teamUser);
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            logger.error("保存成员信息失败", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("保存成员信息失败", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("delUser")
    @Auth(roleType = RoleTypeEnum.ROLE_ADMIN)
    public ResponseVO delUser(HttpSession session, Integer userId) {
        try {
            if (getSessionUserInfo(session).getUserId().equals(userId)) {
                throw new BusinessException("自己不能删除自己");
            }
            this.blogTeamUserService.deleteByUserId(userId);
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            logger.error("删除用户信息异常", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("删除用户信息异常", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("updatePassword")
    @Auth(roleType = RoleTypeEnum.ROLE_ADMIN)
    public ResponseVO updatePassword(Integer userId, String password) {
        try {
            BlogTeamUser blogTeamUser = new BlogTeamUser();
            blogTeamUser.setPassword(StringTools.encodeByMD5(password));
            this.blogTeamUserService.updateByUserId(blogTeamUser, userId);
            return getSuccessResponseVO(null);
        } catch (Exception e) {
            logger.error("修改用户密码异常", e);
            return getServerErrorResponseVO();
        }
    }

    @RequestMapping("updateStatus")
    @Auth(roleType = RoleTypeEnum.ROLE_ADMIN)
    public ResponseVO updateStatus(HttpSession session, Integer userId, Integer status) {
        try {
            if (getSessionUserInfo(session).getUserId().equals(userId)) {
                throw new BusinessException("自己不能修改自己的状态");
            }
            UserStatusEnum statusEnum = UserStatusEnum.getByStatus(status);
            if (statusEnum == null) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            BlogTeamUser blogTeamUser = new BlogTeamUser();
            blogTeamUser.setStatus(status);
            this.blogTeamUserService.updateByUserId(blogTeamUser, userId);
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            logger.error("修改用户状态异常", e);
            return getBusinessErrorResponseVO(e);
        } catch (Exception e) {
            logger.error("修改用户状态异常", e);
            return getServerErrorResponseVO();
        }
    }

}