package com.phoenix.user.aspect;

//import com.phoenix.user.client.FilterServiceClient;
import com.phoenix.common.annotation.FilterEntity;
import com.phoenix.common.annotation.FilterField;
import com.phoenix.common.annotation.FilterNeeded;
import com.phoenix.common.client.FilterServiceClient;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class FilterAspect {

    final FilterServiceClient filterServiceClient;

    @Pointcut(value = "execution(* com.phoenix.user.core.controller.*Controller.*(..))")
    public void point(){
    }
    @Before(value = "point()")
    public void filter(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //接口是否需要被过滤
        if (method.isAnnotationPresent(FilterNeeded.class)){
            Object[] args = joinPoint.getArgs();
            for (Object o:args){
                Class<?> clazz = o.getClass();
                //参数类是否需要被过滤
                if (clazz.isAnnotationPresent(FilterEntity.class)){
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field:fields){
                        field.setAccessible(true);
                        //参数中的字段是否需要被过滤
                        if (field.getType().equals(String.class)&&field.isAnnotationPresent(FilterField.class)){
                            try {
                                field.set(o, filterServiceClient.filterText((String) field.get(o)));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }


    }
}
