package com.spring.exercise.framework.beans.support;

import com.spring.exercise.framework.beans.config.GPBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GPBeanDefinitionReader {
    private List<String> registryBeanClasses = new ArrayList<String>();

    private Properties config = new Properties();

    //固定配置文件中的key
    private final String SCAN_PACKAGE = "scanPackage";

    public GPBeanDefinitionReader(String... locations){
        //通过url定位找到找到其对应的文件，然后转换为文件流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));

        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));

        File classPath = new File(url.getFile());

        for (File file : classPath.listFiles()){
            if (file.isDirectory()){
                doScanner(scanPackage +"." + file.getName());
            }else {
                if (!file.getName().endsWith(".class")){
                    continue;
                }

                String className = scanPackage + "." + file.getName().replaceAll(".class","");

                registryBeanClasses.add(className);
            }

        }

    }

    public Properties getConfig() {
        return config;
    }

    //把配置文件中扫描到的所有配置信息转换为GPBeanDefinition对象，以便以后IOC操作方便
    public List<GPBeanDefinition> loadBeanDefinitions(){
        List<GPBeanDefinition> result = new ArrayList<GPBeanDefinition>();
        try {
            for (String className : registryBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                //如果是一个接口，是不能被实例化的
                //用他实现类来实例化
                if (beanClass.isInterface()){
                    continue;
                }

                //beanName有三种情况
                //1.默认是类名首字母小写
                //2.自定义名字
                //3.接口注入
                result.add(doCreateBeanDefiniton(toLowerFistCase(beanClass.getSimpleName()),beanClass.getName()));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    //如果类名本身是小写字母，确实会出问题
    //但是我要说明的是：这个方法是我自己用，private的
    //传值也是自己传，类也都遵循了驼峰命名法
    //默认传入的值，存在首字母小写的情况，也不可能出现非字母的情况

    //为了简化程序逻辑，就不做其他判断了，大家了解就OK
    //其实用写注释的时间都能够把逻辑写完了
    private String toLowerFistCase(String simpleName) {
        char[] chars = simpleName.toCharArray();

        chars[0] += 32;

        return String.valueOf(chars);
    }

    //把每一个配置信息解析成一个BeanDefinition
    private GPBeanDefinition doCreateBeanDefiniton(String factoryBeanName ,String beanClassName){
        GPBeanDefinition gpBeanDefinition = new GPBeanDefinition();
        gpBeanDefinition.setBeanClassName(beanClassName);
        gpBeanDefinition.setFactoryBeanName(factoryBeanName);
        return gpBeanDefinition;
    }
}
