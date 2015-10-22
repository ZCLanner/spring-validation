package me.lanner.spring.validation.interceptor;

import me.lanner.spring.validation.constraint.ValidatedBy;
import me.lanner.spring.validation.validators.Validator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zhaochen.zc on 15/10/22.
 */
public class DefaultValidatorInterceptorSupport implements ValidatorInterceptorSupport, ApplicationContextAware {

    private BeanFactory beanFactory;

    /**
     * A cache map of annotations and its corresponding validator
     */
    private ConcurrentMap<Class<? extends Annotation>, Validator<? extends Annotation>> validatorCache = new ConcurrentHashMap<>();

    /**
     * A cache set of annotations which doesn't have corresponding validator
     */
    private ConcurrentMap<Class<? extends Annotation>, Boolean> noValidatorAnnotationCache = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanFactory = applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Validator<? extends Annotation> getValidator(Annotation annotation) {

        Validator<? extends Annotation> validator = validatorCache.get(annotation.annotationType());
        if (validator != null) {
            return validator;
        }

        // If the annotation is in #noValidatorAnnotationCache, it means that we have tried to look for its corresponding validator bean but failed.
        // So return null directly
        Boolean notFound = noValidatorAnnotationCache.get(annotation.annotationType());
        if (notFound != null) {
            return null;
        }

        ValidatedBy validatorClazz = annotation.annotationType().getAnnotation(ValidatedBy.class);
        if (validatorClazz == null) {
            // Fail to find the corresponding validator. Put it into failedCache
            noValidatorAnnotationCache.putIfAbsent(annotation.annotationType(), false);
            return null;
        }

        validator = beanFactory.getBean(validatorClazz.validator());
        if (validator == null) {
            noValidatorAnnotationCache.putIfAbsent(annotation.annotationType(), false);
            return null;
        }

        // Success to find corresponding validator. Put it into validatorCache to avoid looking for it again.
        validatorCache.putIfAbsent(annotation.annotationType(), validator);
        return validator;
    }
}
