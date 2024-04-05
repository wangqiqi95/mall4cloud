package com.mall4j.cloud.common.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 该注解需要结合javax.validation的@Valid、javax.validation.constraints下的参数校验注解 以及 BindingResult 进行使用
 * 该注解仅仅是为了结合BindingResult来捕获异常并返回相关的错误信息
 * 在基本使用中如果不使用BindingResult来进行相关异常捕获和返回会一直输出栈信息，存在内存泄漏相关的隐患
 * </p>
 *
 * @author ben
 * @since 2022-11-04
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterValid {
}
