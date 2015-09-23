package me.lanner.spring.validation.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * Created by zhaochen.zc on 15/9/23.
 */
public class BeanFactoryAnnotatedValidationPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    @Override
    public Pointcut getPointcut() {
        return null;
    }
}
