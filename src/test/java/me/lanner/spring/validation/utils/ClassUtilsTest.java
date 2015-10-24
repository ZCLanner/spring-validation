package me.lanner.spring.validation.utils;

import org.junit.Test;
import org.springframework.util.StringUtils;
import sun.security.validator.Validator;

import java.io.IOException;
import java.util.Set;

/**
 * Created by zhaochen.zc on 15/10/24.
 */
public class ClassUtilsTest {

    @Test
    public void testLoadClasses() throws IOException, ClassNotFoundException {
        Set<Class<?>> classSet =
                ClassUtils.loadClassesImplementsTheInterface("me.lanner.spring.validation.validators", Validator.class);
        System.out.println(StringUtils.collectionToDelimitedString(classSet, ","));
    }

}
