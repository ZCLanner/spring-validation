package me.lanner.spring.validation.interceptor;

import org.springframework.aop.support.StaticMethodMatcherPointcut;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhaochen.zc on 15/9/23.
 */
public class AnnotatedValidationPointcut extends StaticMethodMatcherPointcut {

    private ConcurrentHashMap<Class<?>, Boolean> classMatchingCache = new ConcurrentHashMap<>();

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return matches(targetClass) || matches(method);
    }

    private boolean matches(Class<?> targetClass) {
        if (classMatchingCache.containsKey(targetClass)) {
            return classMatchingCache.get(targetClass);
        }
        Valid validAnnotation = targetClass.getAnnotation(Valid.class);
        boolean matches = (validAnnotation != null);
        classMatchingCache.putIfAbsent(targetClass, matches);
        return matches;
    }

    private boolean matches(Method method) {
        if (method.getAnnotation(Valid.class) != null) {
            return true;
        }
        Annotation[][] annotations = method.getParameterAnnotations();
        if (annotations == null) {
            return false;
        }
        for (Annotation[] annotationArray : annotations) {
            if (annotationArray == null) {
                continue;
            }
            for (Annotation annotation : annotationArray) {
                if (annotation instanceof Valid) {
                    return true;
                }
            }
        }
        return false;
    }
}
