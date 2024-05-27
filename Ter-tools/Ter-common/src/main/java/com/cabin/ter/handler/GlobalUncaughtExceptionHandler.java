package com.cabin.ter.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局线程异常捕获
 *
 * @author xiaoye
 * @date Created in 2024-05-27 13:48
 */
@Slf4j
public class GlobalUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final GlobalUncaughtExceptionHandler instance = new GlobalUncaughtExceptionHandler();

    private GlobalUncaughtExceptionHandler() {
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread {} ", t.getName(), e);
    }

    public static GlobalUncaughtExceptionHandler getInstance() {
        return instance;
    }

}
