package com.mazanenko.petproject.bookshop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class LogExceptionAnnotationAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.mazanenko.petproject.bookshop.annotation.LogException)")
    public void pointcut() {
    }

    @AfterThrowing(value = "pointcut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        if (joinPoint == null || exception == null) {
            return;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();


        logger.error("Method {} was executed with an error \"{}\". Args names: {}, values: {}",
                joinPoint.getSignature(), exception.getMessage(), parameterNames, joinPoint.getArgs());
    }
}
