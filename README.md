# spring-validation

基于Spring AOP的参数验证框架，在日常的开发过程中，经常会遇到参数校验的问题，使用这个扩展可以把参数校验的逻辑从业务代码中解藕出来，成为单独的模块，使业务代码看起来更清爽。

环境要求

spring-aop 3.2.6.RELEASE +

基本使用方法
----

1. 假设有一个类叫`PlainObject`，它有一个方法叫`functionA(String str)`

   如果你想给`PlainObject#functionA(String)`加一个约束，让`functionA`的参数不能为空，你可以这样声明`functionA`

    ```java
    public class PlainObject {
        public void functionA(@NotNull("Str cannot be null") String str) {

        }
    }
    ```

2. 把`PlainObject`加到spring IOC的容器中，可以通过xml配置的方式，也可以通过Annotation的方式

3. 在spring的配置文件中开启参数自动检测功能

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:validation="http://www.lanner.me/schema/spring-extension-validation"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.lanner.me/schema/spring-extension-validation
           http://www.lanner.me/schema/validation/spring-extension-validation.xsd">

        <validation:annotation-driven>
            <validation:onConstraintViolated>throwException</validation:onConstraintViolated>
        </validation:annotation-driven>

    </beans>
    ```
    如果想启动自动检测功能，需要在spring的配置文件中配置`<validation:annotation-driven />`。如果spring的IOC容器中，有一个类它的public方法的参数中带有`@Valid`或`@NotNull`之类的参数限制类注解，参数校验扩展会自动为它生成一个动态代理类，在该方法真正被调用之前，对参数进行校验。
    你可以通过`<onConstraintViolated>`标签来订制当注解定义的约束被破坏时，参数校验框架采用什么方式来处理这种情况：是抛出异常（throwException）还是把错误信息放到本地线程变量ViolationMessageHolder中（默认）

4. 经过以上3步配置，当你用一个空的参数传入`PlainObject#functionA(String)`时，你会得到一个异常，异常信息为`"Str cannot be null"`

自定义约束
----

如果你想支持除了`@NotNull`以外更多的约束，你可以自定义约束。想完成自定义约束，你需要多做一些工作。

比如要自定义一个限定整数最小值的约束`@Min`

1. 自定义校验器Validator，Validator是参数校验框架的一个接口，全名是`me.lanner.spring.validation.validators.Validator`，它有一个方法`valid`，主要是对传入的参数进行校验，如果不符合约束，返回错误信息。

    ```java
    public class MinValidator extends Validator<Min> {
        public String valid(Object param, Min minAnnotation) {
            int minValue = min.minValue();
            // 比较param与minValue的关系，如果param比minValue还要小，返回错误信息
        }
    }
    ```

2. 自定义约束注解`@Min`，在自定义是要注意三点：
    
    - 注解必须在`RUNTIME`时生效
    - 注解必须可以被放在参数上
    - 注解必须用`@ValidatedBy`指名由哪一个`Validator`来校验这个约束
    
    ```java
    @ValidatedBy(validator = MinValidator.class)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NotNull {
        String minValue();
    }
    ```
    
3. 把`MinValidator`放到Spring的IOC容器中

4. 尽情使用你自定义的注解吧

__提醒：由于MinValidator是被放到IOC容器中的，所以你可以像使用一般的Spring的Bean一样，向MinValidator中注入其他Spring的Bean。__