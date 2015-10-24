package me.lanner.spring.validation.config;

import me.lanner.spring.validation.handler.ConstraintViolationRaiseExceptionHandler;
import me.lanner.spring.validation.interceptor.BeanFactoryAnnotatedValidationPointcutAdvisor;
import me.lanner.spring.validation.interceptor.DefaultValidatorInterceptorSupport;
import me.lanner.spring.validation.interceptor.ValidationInterceptor;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by lanner on 15/9/23.
 */
public class AnnotationDrivenBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(parserContext, element);
        Object eleSource = parserContext.extractSource(element);

        RootBeanDefinition interceptorSupportDef = new RootBeanDefinition(DefaultValidatorInterceptorSupport.class);
        interceptorSupportDef.setSource(eleSource);
        interceptorSupportDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        parserContext.getReaderContext().registerWithGeneratedName(interceptorSupportDef);

        RootBeanDefinition violationHandlerDef = new RootBeanDefinition(ConstraintViolationRaiseExceptionHandler.class);
        violationHandlerDef.setSource(eleSource);
        violationHandlerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        parserContext.getReaderContext().registerWithGeneratedName(violationHandlerDef);

        RootBeanDefinition interceptorDef = new RootBeanDefinition(ValidationInterceptor.class);
        interceptorDef.setSource(eleSource);
        interceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        interceptorDef.getPropertyValues().add("validatorInterceptorSupport", interceptorSupportDef);
        interceptorDef.getPropertyValues().add("constraintViolationHandler", violationHandlerDef);
        String interceptorName = parserContext.getReaderContext().registerWithGeneratedName(interceptorDef);

        RootBeanDefinition advisorDef = new RootBeanDefinition(BeanFactoryAnnotatedValidationPointcutAdvisor.class);
        advisorDef.setSource(eleSource);
        advisorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
        String validationAdvisorBeanName = parserContext.getReaderContext().registerWithGeneratedName(advisorDef);

        CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), eleSource);
        compositeDef.addNestedComponent(new BeanComponentDefinition(interceptorDef, interceptorName));
        compositeDef.addNestedComponent(new BeanComponentDefinition(advisorDef, validationAdvisorBeanName));
        parserContext.registerComponent(compositeDef);

        return null;
    }

    private void registerValidatorBeans() {

    }
}
