package com.cabin.ter.exception;

import com.cabin.ter.constants.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 异常基类
 * </p>
 * @author xiaoye
 * @date Created in 2024-4-19 15:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException{
    private Integer code;
    private String message;
    private Object data;

    public BaseException(Status status){
        super(status.getMessage());
        this.code = status.getStatus();
        this.message = status.getMessage();
    }

    public BaseException(Status status,Object data){
        this(status);
        this.data = data;
    }
    public BaseException(Integer code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(Integer code, String message, Object data){
        this(code, message);
        this.data = data;
    }
}
