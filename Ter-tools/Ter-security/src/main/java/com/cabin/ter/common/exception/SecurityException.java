package com.cabin.ter.common.exception;

import com.cabin.ter.constants.Status;
import com.cabin.ter.exception.BaseException;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *     全局异常
 * </p>
 * @author xiaoye
 * @date Created in 2024-04-19 17:37
 */
@EqualsAndHashCode(callSuper = true)
public class SecurityException extends BaseException {
    public SecurityException(Status status){
        super(status);
    }

    public SecurityException(Status status, Object data){
        super(status, data);
    }

    public SecurityException(Integer code, String message){
        super(code, message);
    }
    public SecurityException(Integer code, String message, Object data){
        super(code, message, data);
    }
}
