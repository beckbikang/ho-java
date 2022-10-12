package cn.beckbi.config;

import cn.beckbi.util.ThreadPoolBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cn.beckbi.util.SimpleThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.concurrent.ThreadPoolExecutor;


/**
 * @program: ho-java
 * @description:
 * @author: bikang
 * @create: 2022-10-12 22:48
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {


    @Bean
    public ThreadPoolExecutor getMessagePool() {
         return ThreadPoolBuilder.buildThreadPool(
                 "mc-to-kafka-pool", -1,-1,Runtime.getRuntime().availableProcessors(),
                 Runtime.getRuntime().availableProcessors() * 2
         );
    }



}
