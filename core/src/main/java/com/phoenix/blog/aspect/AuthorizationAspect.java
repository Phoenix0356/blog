package com.phoenix.blog.aspect;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.exceptions.clientException.PermissionDeniedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuthorizationAspect {
    @Pointcut(value = "execution(* com.phoenix.blog.core.controller.*Controller.*(..))")
    public void point(){
    }
    @Before(value = "point()")
    public void authorizeRole(JoinPoint joinPoint){

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        int requiredRoleLevel = 0;
        if (method.isAnnotationPresent(AuthorizationRequired.class)) {
            AuthorizationRequired authorizationRequired = method.getAnnotation(AuthorizationRequired.class);
            requiredRoleLevel = authorizationRequired.value().getLevel();
        }

        //接口要求权限为最低直接放行
        if (requiredRoleLevel == 0){
            return;
        }
        String userRole = TokenContext.getUserRole();
        int userRoleLevel = Role.getLevel(userRole);
        if (userRoleLevel < requiredRoleLevel) {
            throw new PermissionDeniedException();
        }
    }
}
