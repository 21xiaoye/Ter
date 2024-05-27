package com.cabin.ter.util;


import com.cabin.ter.constants.dto.RequestInfoDTO;

/**
 * @author xiaoye
 * @date Created in 2024-05-27 10:27
 */
public class RequestHolderUtil {

    private static final ThreadLocal<RequestInfoDTO> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfoDTO requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfoDTO get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
