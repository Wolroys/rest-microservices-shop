package com.wolroys.shoputils.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("within(com.wolroys.*.controller.*Controller)")
    void isControllerLayer(){}

    @Pointcut("within(com.wolroys.*.service.*S)")
    void isServiceLayer(){
    }

    @After("isControllerLayer() || isServiceLayer()")
    public void profileControllerMethods(JoinPoint joinPoint) {
        log.info("Invoked class: {} with method: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }

    @AfterThrowing(value = "isControllerLayer() && target(controller)", throwing = "e")
    public void addLoggingForExceptions(Throwable e, Object controller){
        log.error("An error {}: {} was received in the controller: {}", e.getClass().getSimpleName(), e.getMessage(),
                controller.getClass().getSimpleName());
    }
}
