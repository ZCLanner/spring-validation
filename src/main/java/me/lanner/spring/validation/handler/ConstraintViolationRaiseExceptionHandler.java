package me.lanner.spring.validation.handler;

import java.lang.annotation.Annotation;

/**
 * Created by zhaochen.zc on 15/10/22.
 */
public class ConstraintViolationRaiseExceptionHandler implements ConstraintViolationHandler {

    @Override
    public void onConstraintViolation(Annotation constraint, Object constraintObject, String violatedMessage) {
        throw new RuntimeException(violatedMessage);
    }
}
