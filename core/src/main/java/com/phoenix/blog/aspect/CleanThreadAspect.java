package com.phoenix.blog.aspect;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.context.TokenContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class CleanThreadAspect {
    @Pointcut(value = "execution(* com.phoenix.blog.core.controller.*Controller.*(..))")
    public void point() {
    }

    @After(value = "point()")
    public void cleanThreadLocal(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (method.isAnnotationPresent(AuthorizationRequired.class)) {
            AuthorizationRequired authorizationRequired = method.getAnnotation(AuthorizationRequired.class);
            if (authorizationRequired.value().getLevel()<1){
                return;
            }
            TokenContext.removeClaims();
        }
    }
}
