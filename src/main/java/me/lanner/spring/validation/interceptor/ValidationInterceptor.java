package me.lanner.spring.validation.interceptor;

import me.lanner.spring.validation.constraint.ValidatedBy;
import me.lanner.spring.validation.validators.Validator;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.lang.annotation.Annotation;

/**
 * Created by zhaochen.zc on 15/9/23.
 */
public class ValidationInterceptor implements MethodInterceptor, BeanFactoryAware {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Annotation[][] paramAnnotations = invocation.getMethod().getParameterAnnotations();
        Object[] params = invocation.getArguments();
        String errMsg = "";
        if (paramAnnotations != null) {
            for (int i = 0; i < paramAnnotations.length; i++) {
                Annotation[] annotations = paramAnnotations[i];
                Object param = params[i];
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Valid) {
                        continue;
                    }
                    ValidatedBy validatedBy = annotation.annotationType().getAnnotation(ValidatedBy.class);
                    if (validatedBy == null) {
                        continue;
                    }
                    Class<? extends Validator> validatorClass = validatedBy.validator();
                    Validator validator = beanFactory.getBean(validatorClass);
                    if (validator == null) {
                        continue;
                    }
                    errMsg = validator.valid(param);
                    if (!StringUtils.isEmpty(errMsg)) {
                        break;
                    }
                }
                if (!StringUtils.isEmpty(errMsg)) {
                    break;
                }
            }
        }
        if (!StringUtils.isEmpty(errMsg)) {
            throw new RuntimeException(errMsg);
        }
        return invocation.proceed();
    }

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
