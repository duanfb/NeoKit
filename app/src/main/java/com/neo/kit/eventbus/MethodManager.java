package com.neo.kit.eventbus;

import java.lang.reflect.Method;

/**
 * @author neo.duan
 * @date 2020-01-05 18:32
 * @desc 定义事件的实体
 */
public class MethodManager {
    /**
     * 方法所在的线程
     */
    ThreadMode threadMode;
    /**
     * 方法
     */
    Method method;
    /**
     * 形式参数
     */
    Class<?> type;

    public MethodManager(ThreadMode threadMode, Method method, Class<?> type) {
        this.threadMode = threadMode;
        this.method = method;
        this.type = type;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
