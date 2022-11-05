package com.lw.yizhiyuya.jobmsg.executor.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * @author lw
 * @create 2022-11-05-22:43
 */
@Component
public class SpringBeanFactoryUtil implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringBeanFactoryUtil.beanFactory = beanFactory;
    }

    public static <T> T getBeanByType(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }
}
