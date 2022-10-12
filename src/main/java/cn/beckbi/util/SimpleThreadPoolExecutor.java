package cn.beckbi.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
/**
 * @program: ho-java
 * @description:
 * @author: bikang
 * @create: 2022-10-12 22:46
 */
@Slf4j
public class SimpleThreadPoolExecutor extends ThreadPoolExecutor {

    public SimpleThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                    TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null && r instanceof Future<?>) {
            Future<?> future = (Future<?>) r;
            if (future.isDone()) {
                try {
                    future.get();
                } catch (Exception e) {
                    log.error("task_occur_error", e);
                }
            }
        }
        if (t != null) {
            log.error("task_occur_error", t);
        }
    }
}
