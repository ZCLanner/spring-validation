package me.lanner.spring.validation.interceptor;

import me.lanner.spring.validation.handler.ConstraintViolationHandler;
import me.lanner.spring.validation.handler.ViolationMessageHolder;
import me.lanner.spring.validation.validators.Validator;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

/**
 * Created by zhaochen.zc on 15/9/23.
 */
public class ValidationInterceptor implements MethodInterceptor {

    private ValidatorInterceptorSupport validatorInterceptorSupport;
    public void setValidatorInterceptorSupport(ValidatorInterceptorSupport support) {
        assert support != null;
        this.validatorInterceptorSupport = support;
    }

    private ConstraintViolationHandler constraintViolationHandler;
    public void setConstraintViolationHandler(ConstraintViolationHandler handler) {
        assert handler != null;
        this.constraintViolationHandler = handler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Annotation[][] paramAnnotations = invocation.getMethod().getParameterAnnotations();
        Object[] params = invocation.getArguments();
        if (paramAnnotations == null) {
            return invocation.proceed();
        }

        String errMsg = "";
        Annotation violatedConstraint = null;
        Object violatingObject = null;
        for (int i = 0; i < paramAnnotations.length; i++) {
            Annotation[] annotations = paramAnnotations[i];
            Object param = params[i];
            for (Annotation annotation : annotations) {
                Validator validator = validatorInterceptorSupport.getValidator(annotation);
                errMsg = validator.valid(param, annotation);
                if (!StringUtils.isEmpty(errMsg)) {
                    violatedConstraint = annotation;
                    violatingObject = param;
                    break;
                }
            }
            if (!StringUtils.isEmpty(errMsg)) {
                break;
            }
        }
        if (!StringUtils.isEmpty(errMsg)) {
            constraintViolationHandler.onConstraintViolation(violatedConstraint, violatingObject, errMsg);
        }
        return invocation.proceed();
    }
}
