package com.example.utils.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zfl
 * @create 2022/1/29 15:49
 * @description
 */
public class ThreadPoolUtils {

    /**
     * 根据cpu的数量动态的配置核心线程数和最大线程数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数 = CPU核心数 + 1
     * IO密集型：2CPU，计算密集型：CPU + 1
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    /**
     * 线程池最大线程数 = CPU核心数 * 2 + 1
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 非核心线程闲置时超时时间
     */
    private static final int KEEP_ALIVE = 60;
    /**
     * 线程池的对象
     */
    private ThreadPoolExecutor executor;
    /**
     * 唯一实例
     */
    public static ThreadPoolUtils instance = newInstance();
    /**
     * 要确保该类只有一个实例对象，避免产生过多对象消费资源，所以采用单例模式
     */
    private ThreadPoolUtils() {}

    /**
     * 获取实例
     *
     * @return 当前类实例
     */
    private static ThreadPoolUtils newInstance() {
        ThreadPoolUtils instance = new ThreadPoolUtils();
        instance.executor = instance.newPool();
        return instance;
    }

    /**
     * 新建线程池
     * corePoolSize:核心线程数
     * maximumPoolSize：线程池所容纳最大线程数(workQueue队列满了之后才开启)
     * keepAliveTime：非核心线程闲置时间超时时长
     * unit：keepAliveTime的单位
     * workQueue：等待队列，存储还未执行的任务
     * threadFactory：线程创建的工厂
     * handler：异常处理机制
     *
     * @return 线程池
     */
    private ThreadPoolExecutor newPool() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000),
                CustomDefaultThreadFactory.newDefaultThreadFactory("MetaDataThreadPool"),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 开启一个无返回结果的线程
     *
     * @param r the r
     */
    public void execute(Runnable r) {
        // 把一个任务丢到了线程池中
        executor.execute(r);
    }
}
