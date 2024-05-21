package com.cabin.ter.util;

import cn.hutool.core.util.ObjectUtil;
import com.cabin.ter.constants.enums.IStatus;
import com.cabin.ter.constants.enums.Status;
import com.cabin.ter.exception.BaseException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 *     校验工具类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-23 16:55
 */
public class AsserUtil {
    /**
     * 校验失败直接结束
     */
    private static Validator failFastValidator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory().getValidator();
    /**
     * 全部校验
     */
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    /**
     * 注解校验参数，校验到失败直接结束
     */
    public static <T> void fastFailValidate(T obj){
        Set<ConstraintViolation<T>> constraintViolations = failFastValidator.validate(obj);
        if(constraintViolations.size() >0){
            throwException(Status.PARAM_NOT_MATCH,constraintViolations.iterator().next().getMessage());
        }
    }
    /**
     * 注解验证参数，全部校验，抛出异常
     */
    public static <T> void allCheckValidateThrow(T obj){
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if(constraintViolations.size()>0){
            StringBuilder errorsBuild = new StringBuilder();
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()){
                ConstraintViolation<T> violation = iterator.next();
                errorsBuild.append(violation.getPropertyPath().toString()).append(": ").append(violation.getMessage());
            }
            throwException(Status.PARAM_NOT_MATCH, errorsBuild.toString().substring(0, errorsBuild.length()-1));
        }
    }

    /**
     * 不是非空对象，抛出异常
     */
    public static void isNotEmpty(Object obj, String msg){
        if((isEmpty(obj))){
            throwException(msg);
        }
    }

    public static boolean isEmpty(Object obj){
        return ObjectUtil.isEmpty(obj);
    }
    private static void throwException(String msg){
        throwException(null, msg);
    }
    private static void throwException(IStatus status, Object... arg) {
        if (Objects.isNull(status)) {
            status = Status.PARAM_NOT_MATCH;
        }
        throw new BaseException(status.getStatus(), MessageFormat.format(status.getMessage(), arg));
    }
}
