package me.lanner.spring.validation.constraint;

import me.lanner.spring.validation.validators.NotNullValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhaochen.zc on 15/10/19.
 */
@ValidatedBy(validator = NotNullValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
    String value();
}
