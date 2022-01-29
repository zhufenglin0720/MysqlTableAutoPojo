package com.example.utils.pool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zfl
 * @create 2022/1/29 11:46
 * @description
 */
public class CustomDefaultThreadFactory {

    /**
     * New default thread factory default thread factory.
     *
     * @param threadNamePrefix the thread name prefix
     * @return the default thread factory
     */
    public static DefaultThreadFactory newDefaultThreadFactory(String threadNamePrefix) {
        return new DefaultThreadFactory(threadNamePrefix);
    }

    /**
     * The type Default thread factory.
     */
    public static class DefaultThreadFactory implements ThreadFactory {

        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        /**
         * Instantiates a new Default thread factory.
         *
         * @param threadName the thread name
         */
        public DefaultThreadFactory(String threadName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = threadName + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
