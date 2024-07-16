package com.happyblog.controller;

import com.happyblog.entity.constants.Constants;
import com.happyblog.entity.dto.CreateImageCode;
import com.happyblog.entity.dto.SessionAdminUserDto;
import com.happyblog.entity.po.BlogTeamUser;
import com.happyblog.entity.vo.BlogTeamUserVO;
import com.happyblog.entity.vo.ResponseVO;
import com.happyblog.exception.BusinessException;
import com.happyblog.service.BlogTeamUserService;
import com.happyblog.utils.CopyTools;
import com.happyblog.utils.ImageUtils;
import com.happyblog.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController("accountController")
@RequestMapping("/api")
public class AccountController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


    @Resource
    private BlogTeamUserService blogTeamUserService;

    @Resource
    private ImageUtils imageUtils;

    /**
     * @Description: 验证码
     * @auther: 多多洛
     * @date: 14:50 2020/11/7
     * @param: [request, response, session]
     * @return: void
     */
    @RequestMapping(value = "/checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws
            IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        session.setAttribute(Constants.CHECK_CODE_KEY, code);
        vCode.write(response.getOutputStream());
    }

    @RequestMapping("/login")
    public ResponseVO login(HttpSession session, String account, String password, String checkCode) throws BusinessException {
        try {
            if (StringTools.isEmpty(checkCode) || !checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
                throw new BusinessException("图片验证码不正确");
            }
            SessionAdminUserDto sessionUserDto = blogTeamUserService.login(account, password);
            session.setAttribute(Constants.ADMIN_SESSION_KEY, sessionUserDto);
            return getSuccessResponseVO(sessionUserDto);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }


    @RequestMapping("getUserInfo")
    public ResponseVO getUserInfo(HttpSession session) throws BusinessException {
        BlogTeamUser user = this.blogTeamUserService.getBlogTeamUserByUserId(getSessionUserInfo(session).getUserId());
        return getSuccessResponseVO(CopyTools.copy(user, BlogTeamUserVO.class));
    }

    @RequestMapping("logout")
    public ResponseVO logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        return getSuccessResponseVO(null);
    }

    @RequestMapping("saveMyInfo")
    public ResponseVO saveMyInfo(HttpSession session, BlogTeamUser teamUser) throws BusinessException {
        teamUser.setUserId(getSessionUserInfo(session).getUserId());
        String avatar = imageUtils.resetImage(teamUser.getAvatar(), null);
        teamUser.setAvatar(avatar);
        teamUser.setRoleType(null);
        teamUser.setStatus(null);
        teamUser.setPassword(null);
        this.blogTeamUserService.saveTeamUser(teamUser);
        SessionAdminUserDto sessionAdminUserDto = getSessionUserInfo(session);
        sessionAdminUserDto.setNickName(teamUser.getNickName());
        session.setAttribute(Constants.ADMIN_SESSION_KEY, sessionAdminUserDto);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("saveAvatar")
    public ResponseVO saveAvatar(HttpSession session, String avatar) {
        BlogTeamUser teamUser = new BlogTeamUser();
        avatar = imageUtils.resetImage(avatar, null);
        teamUser.setAvatar(avatar);
        this.blogTeamUserService.updateByUserId(teamUser, getSessionUserInfo(session).getUserId());
        return getSuccessResponseVO(null);
    }

    @RequestMapping("updateMyPassword")
    public ResponseVO updateMyPassword(HttpSession session, String password) {
        BlogTeamUser teamUser = new BlogTeamUser();
        teamUser.setUserId(getSessionUserInfo(session).getUserId());
        teamUser.setPassword(StringTools.encodeByMD5(password));
        this.blogTeamUserService.updateByUserId(teamUser, teamUser.getUserId());
        return getSuccessResponseVO(null);
    }
}
