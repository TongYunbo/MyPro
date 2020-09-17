package com.tzcpa.constant;

import lombok.Data;

/**
 * @ClassName TaskThreadPoolConsts
 * @Description //异步任务线程池配置
 * @Author hanxf
 * @Date 2019/5/6 10:32
 * @Version 1.0
 **/
public class TaskThreadPoolConsts {
    /**
     * 核心线程数
     */
    public static final int COREPOOLSIZE = 15;

    /**
     * 最大线程数
     */
    public static final int MAXPOOLSIZE = 50;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    public static final int KEEPALIVESECONDS = 60;

    /**
     * 队列长度
     */
    public static final int QUEUECAPACITY = 10000;

    /**
     * 线程名称前缀
     */
    public static final String THREADNAMEPREFIX = "AsyncTask-";
}
