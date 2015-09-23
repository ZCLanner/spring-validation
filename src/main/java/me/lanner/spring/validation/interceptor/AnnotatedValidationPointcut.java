package me.lanner.spring.validation.interceptor;

import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * Created by zhaochen.zc on 15/9/23.
 */
public class AnnotatedValidationPointcut extends StaticMethodMatcherPointcut {
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return false;
    }
}
