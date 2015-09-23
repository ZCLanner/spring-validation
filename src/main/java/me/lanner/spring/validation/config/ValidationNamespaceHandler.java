package me.lanner.spring.validation.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by lanner on 15/9/23.
 */
public class ValidationNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
    }
}
