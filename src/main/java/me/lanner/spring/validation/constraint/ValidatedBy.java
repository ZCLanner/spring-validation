package me.lanner.spring.validation.constraint;

import me.lanner.spring.validation.validators.Validator;

import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Created by zhaochen.zc on 15/10/19.
 */
@Target({ ANNOTATION_TYPE })
@Retention( RUNTIME )
public @interface ValidatedBy {
    Class<? extends Validator> validator();
}
