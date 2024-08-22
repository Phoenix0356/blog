package com.phoenix.filter.filter;

import com.phoenix.common.annotation.FilterField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Component
public class FilterHelper {
    public List<Field> extractStringFields(Object object){
        Class<?> inputClazz = object.getClass();
        Field[] fields = inputClazz.getDeclaredFields();
        List<Field> extractFieldList = new ArrayList<>();
        for (Field field:fields){
            if (field.getType().equals(String.class)&&field.isAnnotationPresent(FilterField.class)){
                field.setAccessible(true);
                extractFieldList.add(field);
            }
        }
        return extractFieldList;
    }
    public void setStringFields(Object object,Field field,String content){
        try {
            field.set(object,content);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
