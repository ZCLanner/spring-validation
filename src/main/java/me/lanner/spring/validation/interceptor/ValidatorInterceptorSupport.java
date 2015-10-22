package me.lanner.spring.validation.interceptor;

import me.lanner.spring.validation.validators.Validator;

import java.lang.annotation.Annotation;

/**
 * Created by zhaochen.zc on 15/10/22.
 */
public interface ValidatorInterceptorSupport {
    Validator<? extends Annotation> getValidator(Annotation annotation);
}
