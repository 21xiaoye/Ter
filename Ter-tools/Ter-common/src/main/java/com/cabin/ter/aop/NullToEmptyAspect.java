package com.cabin.ter.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 这个切面为响应数据为null的参数设默认值
 */
@Aspect
@Component
public class NullToEmptyAspect {
    private static final Map<Class<?>, Object> defaultValues = new HashMap<>();
    static {
        defaultValues.put(String.class, "");
        defaultValues.put(Long.class, 0L);
        defaultValues.put(Integer.class, 0);
        defaultValues.put(Double.class, 0.0);
        defaultValues.put(Float.class, 0.0f);
        defaultValues.put(Boolean.class, false);
        defaultValues.put(Character.class, '\u0000');
        defaultValues.put(Byte.class, (byte) 0);
        defaultValues.put(Short.class, (short) 0);
    }
    @AfterReturning(value = "@annotation(com.cabin.ter.annotation.NullToEmpty)", returning = "result")
    public void processNullValues(Object result) throws IllegalAccessException {
        if (result != null) {
            replaceNulls(result);
        }
    }
    private void replaceNulls(Object obj) throws IllegalAccessException {
        if (Objects.isNull(obj)) return;
        if (obj instanceof Collection<?> collection) {
            for (Object item : collection) {
                processObjectFields(item);
            }
        } else {
            processObjectFields(obj);
        }
    }
    private void processObjectFields(Object obj) throws IllegalAccessException {
        if (Objects.isNull(obj)) return;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (Objects.isNull(value)) {
                field.set(obj, defaultValues.get(field.getType()));
            }
            if (Objects.nonNull(value) && !field.getClass().isPrimitive()) {
                processObjectFields(value);
            }
        }
    }
}
