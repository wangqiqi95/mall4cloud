package com.mall4j.cloud.common.util;


import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Scope
@Aspect
@Slf4j
public class ParameterValidInterceptor {

    @Pointcut("@annotation(com.mall4j.cloud.common.util.annotation.ParameterValid)")
    public void validCut() {
    }

    @Around("validCut()")
    public ServerResponseEntity around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取BindingResult
        BindingResult bindingResult = (BindingResult)Arrays
                .stream(joinPoint.getArgs())
                .filter(e -> e instanceof BindingResult)
                .findAny()
                .orElse(null);
        //判断BindingResult是否存在错误信息
        if (Objects.nonNull(bindingResult) && bindingResult.hasFieldErrors()){
            List<String> paramErrorMassage = bindingResult.getFieldErrors().stream().map(e -> {
                return e.getDefaultMessage();
            }).collect(Collectors.toList());
            System.out.println();
            return ServerResponseEntity.fail( 400,paramErrorMassage);
        }
        return (ServerResponseEntity)joinPoint.proceed();
    }
}
