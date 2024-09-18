package com.phoenix.base.aspect;

import com.phoenix.common.annotation.AuthorizationRequired;
import com.phoenix.base.context.TokenContext;
import com.phoenix.common.enumeration.Role;
import com.phoenix.common.exceptions.clientException.PermissionDeniedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(value = 0)
public class AuthorizationAspect {
    @Pointcut(value = "execution(* com.phoenix.base.core.controller.*Controller.*(..))")
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
