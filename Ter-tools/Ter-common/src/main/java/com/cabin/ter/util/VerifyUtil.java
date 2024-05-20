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
    public static String generateCode() {
        String charList = "ABCDEFGHIJKLMNPQRSTUVWXY";
        String numList = "0123456789";
        String rev = "";
        int maxNumCount = 4;
        int length = 6;
        Random f = new Random();
        for (int i = 0; i < length; i++) {
            if (f.nextBoolean() && maxNumCount > 0) {
                maxNumCount--;
                rev += numList.charAt(f.nextInt(numList.length()));
            } else {
                rev += charList.charAt(f.nextInt(charList.length()));
            }
        }
        return rev;
    }

}
