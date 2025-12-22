package com.alone.coder.framework.snmp.config;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class SnmpTrapThreadPoolConfig {

    /**
     * 核心线程数：线程池中始终保留的线程数，即使它们处于空闲状态
     */
    @Value("${snmp.trap.core-pool-size:10}")
    private int corePoolSize = 10;

    /**
     * 最大线程数：线程池允许的最大线程数
     */
    @Value("${snmp.trap.max-pool-size:50}")
    private int maxPoolSize = 50;

    /**
     * 队列容量：用于缓存等待执行任务的队列大小
     */
    @Value("${snmp.trap.queue-capacity:20}")
    private int queueCapacity = 20;

    /**
     * 空闲线程存活时间（秒）：超过 corePoolSize 的线程空闲超过该时间会被回收
     */
    @Value("${snmp.trap.keep-alive-seconds:60}")
    private long keepAliveSeconds = 60;

    /**
     * 线程名前缀，方便日志排查
     */
    @Value("${snmp.trap.thread-name-prefix:trap-sync-}")
    private String threadNamePrefix = "trap-sync-";

    /**
     * 拒绝策略：
     * CallerRunsPolicy：队列满时调用者线程执行任务
     * AbortPolicy：抛 RejectedExecutionException
     * DiscardPolicy：直接丢弃任务
     * DiscardOldestPolicy：丢弃队列中最旧任务
     */
    private final RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();

    private ThreadPoolExecutor executor;

    @Bean(name = "trapSyncExecutor")
    public ThreadPoolExecutor deviceSyncExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(
                    corePoolSize,
                    maxPoolSize,
                    keepAliveSeconds,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(queueCapacity),
                    r -> {
                        Thread t = new Thread(r);
                        t.setName(threadNamePrefix + t.getId());
                        t.setDaemon(false);
                        return t;
                    },
                    rejectedExecutionHandler
            );
            // 可选：允许核心线程空闲时也回收
            executor.allowCoreThreadTimeOut(true);
        }
        return executor;
    }

    @PreDestroy
    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
