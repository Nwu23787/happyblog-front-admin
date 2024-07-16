package com.happyblog.interceptor;

import com.alibaba.fastjson.JSON;
import com.happyblog.annotation.Auth;
import com.happyblog.entity.config.AppConfig;
import com.happyblog.entity.constants.Constants;
import com.happyblog.entity.dto.SessionAdminUserDto;
import com.happyblog.entity.enums.ResponseCodeEnum;
import com.happyblog.entity.enums.ResponseStatusEnum;
import com.happyblog.entity.enums.RoleTypeEnum;
import com.happyblog.entity.po.BlogTeamUser;
import com.happyblog.entity.query.BlogTeamUserQuery;
import com.happyblog.entity.vo.ResponseVO;
import com.happyblog.service.BlogTeamUserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;


@Component("appInterceptor")
public class AppInterceptor implements HandlerInterceptor {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

    @Resource
    private AppConfig appConfig;

    @Resource
    private BlogTeamUserService blogTeamUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (null == handler) {
            return false;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        /**
         * 全局拦截，避免方法未设置拦截器，导致权限，日志等操作没有记录  如果不强制要求设置 GlobalInterceptor注解，可去掉次拦截器
         */
        if (request.getRequestURI().indexOf("checkCode") != -1 || request.getRequestURI().indexOf("login") != -1
                || request.getRequestURI().indexOf("getImage") != -1) {
            return true;
        }
        if (request.getRequestURI().indexOf("view") != -1) {
            if (appConfig.getOpenWebApi()) {
                return true;
            } else {
                response.setHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                response.setStatus(HttpStatus.OK.value());
                PrintWriter writer = response.getWriter();
                ResponseVO ajaxResponse = new ResponseVO();
                ajaxResponse.setCode(ResponseCodeEnum.CODE_404.getCode());
                ajaxResponse.setInfo(ResponseCodeEnum.CODE_404.getMsg());
                ajaxResponse.setStatus(ResponseStatusEnum.ERROR.getStatus());
                writer.print(JSON.toJSON(ajaxResponse));
                return false;
            }
        }
        Object object = request.getSession().getAttribute(Constants.ADMIN_SESSION_KEY);
        if (object == null && appConfig.getTest()) {
            SessionAdminUserDto userDto = new SessionAdminUserDto();
            BlogTeamUserQuery userQuery = new BlogTeamUserQuery();
            userQuery.setRoleType(RoleTypeEnum.ROLE_ADMIN.getType());
            List<BlogTeamUser> list = blogTeamUserService.findListByParam(userQuery);
            if (!list.isEmpty()) {
                BlogTeamUser teamUser = list.get(0);
                userDto.setNickName(teamUser.getNickName());
                userDto.setUserId(teamUser.getUserId());
                userDto.setAvatar(teamUser.getAvatar());
                userDto.setRoleType(teamUser.getRoleType());
                request.getSession().setAttribute(Constants.ADMIN_SESSION_KEY, userDto);
                object = userDto;
            }
        }

        if (object == null) {
            response.setHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE);
            response.setStatus(HttpStatus.OK.value());
            PrintWriter writer = response.getWriter();
            ResponseVO ajaxResponse = new ResponseVO();
            ajaxResponse.setStatus(ResponseStatusEnum.ERROR.getStatus());
            ajaxResponse.setCode(ResponseCodeEnum.CODE_901.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_901.getMsg());
            writer.print(JSON.toJSON(ajaxResponse));
            return false;
        }

        HandlerMethod method = (HandlerMethod) handler;
        Auth auth = method.getMethodAnnotation(Auth.class);
        if (auth == null) {
            return true;
        }
        Integer roleType = ((SessionAdminUserDto) object).getRoleType();
        RoleTypeEnum roleTypeEnum = auth.roleType();
        if (!roleTypeEnum.getType().equals(roleType)) {
            response.setHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE);
            response.setStatus(HttpStatus.OK.value());
            PrintWriter writer = response.getWriter();
            ResponseVO ajaxResponse = new ResponseVO();
            ajaxResponse.setCode(ResponseCodeEnum.CODE_902.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_902.getMsg());
            ajaxResponse.setStatus(ResponseStatusEnum.ERROR.getStatus());
            writer.print(JSON.toJSON(ajaxResponse));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}