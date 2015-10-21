package me.lanner.spring.validation.validators;

import java.lang.annotation.Annotation;

/**
 * Created by zhaochen.zc on 15/10/19.
 */
public interface Validator<A extends Annotation> {
    String valid(Object object, A annotation);
}
