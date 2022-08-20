package com.continuing.development.probonorest.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(annotation)")
    public void logBeforeMethodCall(JoinPoint joinPoint, final LogMethod annotation) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] params = joinPoint.getArgs();
        if (ObjectUtils.isNotEmpty(params)) {
            switch (annotation.level()) {
                case ("INFO") -> log.info("PROBONO-REST --> Before:  Class Name: {} Method Name: {} was called with parameters {}.",
                        className, methodName, Arrays.toString(params));
                case ("ERROR") -> log.error("PROBONO-REST --> Before:  Class Name: {} Method Name: {} was called with parameters {}.",
                        className, methodName, Arrays.toString(params));
                default -> log.debug("PROBONO-REST --> Before:  Class Name: {} Method Name: {} was called with parameters {}.",
                        className, methodName, Arrays.toString(params));
            }
        } else {
            switch (annotation.level()) {
                case ("INFO") -> log.info("PROBONO-REST --> Before:  Class Name: {} Method Name: {} was called", className, methodName);
                case ("ERROR") -> log.error("PROBONO-REST --> Before:  Class Name: {} Method Name: {} was called", className, methodName);
                default -> log.debug("PROBONO-REST --> Before:  Class Name: {} Method Name: {} was called", className, methodName);
            }
        }
    }

    @AfterReturning(value = "@annotation(annotation)", returning = "result")
    public void logAfterMethodCall(JoinPoint joinPoint, Object result, final LogMethod annotation) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        switch (annotation.level()) {
            case ("INFO") -> log.info("PROBONO-REST --> After:  Class Name: {} Method Name: {} completed and returned {}",
                    className, methodName, result);
            case ("ERROR") -> log.error("PROBONO-REST --> After:  Class Name: {} Method Name: {} completed and returned {}",
                    className, methodName, result);
            default -> log.debug("PROBONO-REST --> After:  Class Name: {} Method Name: {} completed and returned {}",
                    className, methodName, result);
        }
    }

    @Around("@annotation(annotation)")
    public Object executionTime(ProceedingJoinPoint point, final LogMethod annotation) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object object = point.proceed();
        long endTime = System.currentTimeMillis();
        switch (annotation.level()) {
            case ("INFO") -> log.info("PROBONO-REST --> Execution Time:  Class Name: " +
                    point.getSignature().getDeclaringTypeName() + ". Method Name: " + point.getSignature().getName() +
                    ". Time taken for Execution is: " + (endTime - startTime) + "ms");
            case ("ERROR") -> log.error("PROBONO-REST --> Execution Time:  Class Name: " + point.getSignature().getDeclaringTypeName() +
                    ". Method Name: " + point.getSignature().getName() +
                    ". Time taken for Execution is: " +
                    (endTime - startTime) + "ms");
            default -> log.debug("PROBONO-REST --> Execution Time:  Class Name: " + point.getSignature().getDeclaringTypeName() +
                    ". Method Name: " + point.getSignature().getName() +
                    ". Time taken for Execution is: " +
                    (endTime - startTime) + "ms");
        }
        return object;
    }
}
