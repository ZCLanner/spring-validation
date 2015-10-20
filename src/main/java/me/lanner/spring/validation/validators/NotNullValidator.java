package me.lanner.spring.validation.validators;

/**
 * Created by zhaochen.zc on 15/10/19.
 */
public class NotNullValidator implements Validator {
    @Override
    public String valid(Object object) {
        if (object == null) {
            return "NotNull constraints violate";
        }
        return "";
    }
}
