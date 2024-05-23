package com.cabin.ter.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * <p>
 *     生成一次性验证码
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-20 14:33
 */
@Slf4j
public class VerifyUtil {
    public static String generateCode(int len) {
        Random r = new Random();
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < len; i++) {
            rs.append(r.nextInt(10));
        }
        return rs.toString();
    }
}
