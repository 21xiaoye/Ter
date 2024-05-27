package com.cabin.ter.factory;

import com.cabin.ter.handler.GlobalUncaughtExceptionHandler;
import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;

/**
 * @author xiaoye
 * @date Created in 2024-05-27 13:44
 */
@Slf4j
@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory{
    private final ThreadFactory factory;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = factory.newThread(r);
        thread.setUncaughtExceptionHandler(GlobalUncaughtExceptionHandler.getInstance());
        return thread;
    }
}
