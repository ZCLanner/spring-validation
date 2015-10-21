package me.lanner.spring.validation.validators;

import me.lanner.spring.validation.constraint.NotNull;

/**
 * Created by zhaochen.zc on 15/10/19.
 */
public class NotNullValidator implements Validator<NotNull> {
    @Override
    public String valid(Object object, NotNull notNull) {
        if (object == null) {
            return notNull.value();
        }
        return null;
    }
}
