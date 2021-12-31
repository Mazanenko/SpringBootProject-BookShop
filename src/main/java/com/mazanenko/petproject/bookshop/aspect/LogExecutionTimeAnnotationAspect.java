package com.mazanenko.petproject.bookshop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogExecutionTimeAnnotationAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.mazanenko.petproject.bookshop.annotation.LogExecutionTime)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object ExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long proceedingTime = System.currentTimeMillis() - start;

        logger.info("Method {} was executed in {} ms", joinPoint.getSignature(), proceedingTime);
        return proceed;
    }

}
