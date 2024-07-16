package com.happyblog.controller;


import com.happyblog.entity.constants.Constants;
import com.happyblog.entity.dto.SessionAdminUserDto;
import com.happyblog.entity.enums.ResponseCodeEnum;
import com.happyblog.entity.enums.ResponseStatusEnum;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.entity.vo.ResponseVO;
import com.happyblog.exception.BusinessException;
import com.happyblog.utils.CopyTools;

import javax.servlet.http.HttpSession;

public class ABaseController {
    protected static final String STATUC_SUCCESS = "success";

    protected static final String STATUC_ERROR = "error";

    protected ResponseVO getServerErrorResponseVO() {
        ResponseVO vo = new ResponseVO();
        vo.setCode(ResponseCodeEnum.CODE_500.getCode());
        vo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        vo.setStatus(ResponseStatusEnum.ERROR.getStatus());
        return vo;
    }


    protected <T> ResponseVO getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO();
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected ResponseVO getBusinessErrorResponseVO(BusinessException e) {
        ResponseVO vo = new ResponseVO();
        if (e.getCode() == null) {
            vo.setCode(ResponseCodeEnum.CODE_600.getCode());
        } else {
            vo.setCode(e.getCode());
        }
        vo.setInfo(e.getMessage());
        vo.setStatus(ResponseStatusEnum.ERROR.getStatus());
        return vo;
    }

    protected SessionAdminUserDto getSessionUserInfo(HttpSession session) {
        SessionAdminUserDto sessionUserDto = (SessionAdminUserDto) session.getAttribute(Constants.ADMIN_SESSION_KEY);
        return sessionUserDto;
    }

    protected PaginationResult convertPo2VO(PaginationResult result, Class classZ) {
        PaginationResult voResult = new PaginationResult<>();
        voResult.setList(CopyTools.copyList(result.getList(), classZ));
        voResult.setPageNo(result.getPageNo());
        voResult.setPageSize(result.getPageSize());
        voResult.setTotalCount(result.getTotalCount());
        voResult.setPageTotal(result.getPageTotal());
        return voResult;
    }
}
