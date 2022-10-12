package cn.beckbi.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: ho-java
 * @description:
 * @author: bikang
 * @create: 2022-10-12 22:57
 */
@Slf4j
public class ThreadPoolBuilder {



    private static final int DEFAULT_LEN = 3000;

    private static final int DEFAULT_SECOND = 60;


    public static ThreadPoolExecutor buildThreadPool(String name, int queueLen
            , int second, int corePoolSize, int maxPoolSize){
        if (queueLen <= 0) {
            queueLen  = DEFAULT_LEN;
        }
        if (second <= 0) {
            second = DEFAULT_SECOND;
        }
        return new SimpleThreadPoolExecutor(1, 1,
                second, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueLen),
                getThreadFactoryBuilder().setNameFormat(name+"-%d").build(),
                (r, executor) -> log.error(r.toString()+" is discard"));
    }

    private static ThreadFactoryBuilder getThreadFactoryBuilder(){
        return new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler(
                        (Thread t, Throwable e)-> log.error("thread:"+t.getName()+":"+e.getMessage(),e)
                );
    }


}
