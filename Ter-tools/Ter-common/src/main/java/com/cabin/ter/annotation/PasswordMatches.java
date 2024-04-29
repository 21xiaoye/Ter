package com.cabin.ter.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * <p>
 *     密码校验注解
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-23 15:09
 */
@Constraint(validatedBy = PasswordMatches.PassWordValidator.class)
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PasswordMatches.List.class)
public @interface PasswordMatches {
    // 正则表达式
    String regexp() default "";

    // 校验不通过时的提示信息
    String message() default "密码格式不正确，请输入8-20位的密码，必须包含数字和字母，支持特殊符号~!@#$%^*";

    // 分组
    Class<?>[] groups() default {};

    // 集合校验
    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        PasswordMatches[] value();
    }

    class PassWordValidator implements ConstraintValidator<PasswordMatches, String> {

        private String regexp = "^(?![0-9]+$)(?![a-zA-Z~!@#$%^*]+$)[0-9A-Za-z~!@#$%^*]{8,20}$";

        @Override
        public void initialize(PasswordMatches constraintAnnotation) {
            if (StringUtils.isNotBlank(constraintAnnotation.regexp())) {
                this.regexp = constraintAnnotation.regexp();
            }
        }

        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isBlank(s) || !s.matches(regexp)) {
                return false;
            }
            return true;
        }
    }

}
