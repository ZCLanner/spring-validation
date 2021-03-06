package me.lanner.spring.validation.config;

import me.lanner.spring.validation.handler.ConstraintViolationHandler;
import me.lanner.spring.validation.handler.ConstraintViolationRaiseExceptionHandler;
import me.lanner.spring.validation.handler.ConstraintViolationThreadLocalMessageHandler;
import me.lanner.spring.validation.interceptor.BeanFactoryAnnotatedValidationPointcutAdvisor;
import me.lanner.spring.validation.interceptor.DefaultValidatorInterceptorSupport;
import me.lanner.spring.validation.interceptor.ValidationInterceptor;
import me.lanner.spring.validation.utils.ClassUtils;

import java.io.IOException;
import java.util.Set;

import me.lanner.spring.validation.validators.Validator;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

        Class<? extends ConstraintViolationHandler> constraintViolationHandlerClazz = getConstraintViolationHandlerClazz(element);
        RootBeanDefinition violationHandlerDef = new RootBeanDefinition(constraintViolationHandlerClazz);
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
        
        registerValidatorBeans(eleSource, parserContext);

        return null;
    }

    private void registerValidatorBeans(Object eleSource, ParserContext parserContext) {
    	try {
    		Set<Class<?>> validatorClazzSet = ClassUtils.loadClassesImplementsTheInterface(
    			"me.lanner.spring.validation.validators", Validator.class);
    		for (Class<?> validatorClazz : validatorClazzSet) {
    			RootBeanDefinition validatorDef = new RootBeanDefinition(validatorClazz);
    			validatorDef.setSource(eleSource);
    			validatorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    			parserContext.getReaderContext().registerWithGeneratedName(validatorDef);
    		}
    	} catch (IOException | ClassNotFoundException ex) {
    		ex.printStackTrace();
    	}
    }

    private Class<? extends ConstraintViolationHandler> getConstraintViolationHandlerClazz(Element element) {
        Class<? extends ConstraintViolationHandler> constraintViolationHandlerClazz =
                ConstraintViolationThreadLocalMessageHandler.class;
        NodeList childNodeList = element.getChildNodes();
        for (int i = 0; i < childNodeList.getLength(); i++) {
            Node node = childNodeList.item(i);
            if (!"onConstraintViolated".equals(node.getLocalName())) {
                continue;
            }
            if ("throwException".equals(node.getTextContent())) {
                return ConstraintViolationRaiseExceptionHandler.class;
            }
            return ConstraintViolationThreadLocalMessageHandler.class;
        }
        return constraintViolationHandlerClazz;
    }
}
