package com.spring.exercise.framework.annotation;

import java.lang.annotation.*;

/**
 * 业务逻辑入口
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPService {
}
