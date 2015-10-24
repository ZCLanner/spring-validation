package me.lanner.spring.validation.interceptor;

import me.lanner.spring.validation.constraint.ValidatedBy;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by zhaochen.zc on 15/9/23.
 */
public class AnnotatedValidationPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return matches(method);
    }

    private boolean matches(Method method) {
        Annotation[][] annotations = method.getParameterAnnotations();
        for (Annotation[] annotationArray : annotations) {
            if (annotationArray == null) {
                continue;
            }
            for (Annotation annotation : annotationArray) {
                // 如果一个注解他是Valid或者这个注解被ValidatedBy标注了，那么这个方法就需要被进行校验
                if (annotation instanceof Valid) {
                    return true;
                }
                ValidatedBy validatedByAnnotation = annotation.annotationType().getAnnotation(ValidatedBy.class);
                if (validatedByAnnotation != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
