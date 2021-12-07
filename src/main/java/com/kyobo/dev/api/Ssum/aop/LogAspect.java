package com.kyobo.dev.api.Ssum.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("within(com.kyobo.dev.api.Ssum.controller..*)")
    public void onRequest() {}

    // Pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 호출 전에 적용
    @Before("onRequest()")
    public void beforeParameterLog(JoinPoint joinPoint) {

        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);

        log.info("======= method name = {} =======", method.getName());

        // 파라미터 받아오기
        Object[] args = joinPoint.getArgs();
        if (args.length <= 0) log.info("no parameter");
        for (Object arg : args) {
            log.info("parameter type = {}", arg.getClass().getSimpleName());
            log.info("parameter value = {}", arg);
        }
    }

    // Poincut에 의해 필터링된 경로로 들어오는 경우 메서드 리턴 후에 적용
    @AfterReturning(value = "onRequest()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {

        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);

        log.info("return type = {}", returnObj.getClass().getSimpleName());
        log.info("return value = {}", returnObj);

        log.info("======= method name = {} =======", method.getName());
    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
