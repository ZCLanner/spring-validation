package me.lanner.spring.validation.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * Created by zhaochen.zc on 15/9/23.
 */
public class BeanFactoryAnnotatedValidationPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private Pointcut pointcut;

    public BeanFactoryAnnotatedValidationPointcutAdvisor() {
        this.pointcut = new AnnotatedValidationPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
}
