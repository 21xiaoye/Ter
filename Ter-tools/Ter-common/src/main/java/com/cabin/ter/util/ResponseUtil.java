package com.cabin.ter.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cabin.ter.constants.ApiResponse;
import com.cabin.ter.constants.IStatus;
import com.cabin.ter.constants.Status;
import com.cabin.ter.exception.BaseException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;


/**
 * <p>
 *     response 通用工具类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-27 17:16
 */
@Slf4j
public class ResponseUtil {
    /**
     * 往 response 写出 json
     *
     * @param response  响应
     * @param status    状态
     * @param data      返回数据
     */
    public static void renderJson(HttpServletResponse response, IStatus status, Object data){
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(200);

            // FIXME: hutool 的 BUG：JSONUtil.toJsonStr()
            //  将JSON转为String的时候，忽略null值的时候转成的String存在错误
            response.getWriter().write(JSONUtil.toJsonStr(new JSONObject(ApiResponse.ofStatus(status, data), false)));
        }catch (IOException e){
            log.error("Response写出JSON异常"+e);
            throw new BaseException(Status.ERROR);
        }
    }

    /**
     * 往 response 写出 json
     *
     * @param response  响应
     * @param exception 异常
     */
    public static void renderJson(HttpServletResponse response, BaseException exception) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(200);

            // FIXME: hutool 的 BUG：JSONUtil.toJsonStr()
            //  将JSON转为String的时候，忽略null值的时候转成的String存在错误
            response.getWriter().write(JSONUtil.toJsonStr(new JSONObject(ApiResponse.ofException(exception), false)));
        } catch (IOException e) {
            log.error("Response写出JSON异常，", e);
            throw new BaseException(Status.ERROR);
        }
    }
}
