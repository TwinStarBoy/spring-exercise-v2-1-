package com.spring.exercise.framework.beans.support;

import com.spring.exercise.framework.beans.config.GPBeanDefinition;
import com.spring.exercise.framework.context.support.GPAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {
    //存储注册信息的BeanDefinition
    protected final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,GPBeanDefinition>();

}
