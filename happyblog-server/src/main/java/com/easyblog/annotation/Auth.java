package com.happyblog.annotation;

import com.happyblog.entity.enums.RoleTypeEnum;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface Auth {
    RoleTypeEnum roleType();
}
