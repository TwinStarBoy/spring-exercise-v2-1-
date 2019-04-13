package com.spring.exercise.framework.annotation;

import java.lang.annotation.*;

/**
 * 自动注入
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPAutowired {
    String value() default "";
}
