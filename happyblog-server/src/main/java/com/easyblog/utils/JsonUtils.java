package com.happyblog.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.happyblog.entity.enums.ResponseCodeEnum;
import com.happyblog.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static String convertObj2Json(Object obj) {
        if (null == obj) {
            return null;
        }
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static <T> T convertJson2Obj(String json, Class<T> classz) {
        return JSONObject.parseObject(json, classz);
    }

    public static <T> List<T> convertJsonArray2List(String json, Class<T> classz) throws BusinessException {
        try {
            return JSONArray.parseArray(json, classz);
        } catch (Exception e) {
            logger.error("convertJsonArray2List,json:{},异常:{}", json, e);
            throw new BusinessException(ResponseCodeEnum.CODE_601.getMsg(), ResponseCodeEnum.CODE_601.getCode());
        }
    }

}
