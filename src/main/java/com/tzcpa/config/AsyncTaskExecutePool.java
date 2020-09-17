package com.tzcpa.config;

import com.tzcpa.constant.TaskThreadPoolConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName AsyncTaskExecutePool
 * @Description 配置类实现AsyncConfigurer接口，并重写getAsyncExecutor方法，并返回一个ThreadPoolTaskExecutor，
 * @Author hanxf
 * @Date 2019/5/6 10:32
 * @Version 1.0
 **/
@Component
public class AsyncTaskExecutePool implements AsyncConfigurer {
    private Logger logger = LoggerFactory.getLogger(AsyncTaskExecutePool.class);


    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(TaskThreadPoolConsts.COREPOOLSIZE);
        executor.setMaxPoolSize(TaskThreadPoolConsts.MAXPOOLSIZE);
        executor.setQueueCapacity(TaskThreadPoolConsts.QUEUECAPACITY);
        executor.setKeepAliveSeconds(TaskThreadPoolConsts.KEEPALIVESECONDS);
        executor.setThreadNamePrefix(TaskThreadPoolConsts.THREADNAMEPREFIX);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        executor.initialize();
        return executor;
    }


    /**
     * 异步任务中异常处理
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> logger.info("异常处理throwable={}，异常信息={}，异常方法名称={}", throwable, throwable.getMessage(), method.getName());
    }
}
