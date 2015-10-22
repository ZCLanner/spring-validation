package me.lanner.spring.validation.validators;

import me.lanner.spring.validation.interceptor.DefaultValidatorInterceptorSupport;

import javax.validation.Valid;

/**
 * Validator for {@link Valid}
 * @see Validator
 * Created by zhaochen.zc on 15/10/22.
 */
public class ValidValidator implements Validator<Valid> {

    private DefaultValidatorInterceptorSupport validatorInterceptorSupport;
    public void setValidatorInterceptorSupport(DefaultValidatorInterceptorSupport support) {
        assert support != null;
        this.validatorInterceptorSupport = support;
    }

    @Override
    public String valid(Object object, Valid annotation) {
        return null;
    }
}
