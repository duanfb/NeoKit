package com.neo.kit.eventbus;

/**
 * @author neo.duan
 * @date 2020-01-05 18:29
 * @desc 线程类型
 */
public enum ThreadMode {

    /**
     * 在主线程中回调事件
     */
    MAIN,
    /**
     * 在子线程中回调事件
     */
    BACKGROUND,
    /**
     * 在默认线程中回调事件
     */
    POSTING
}
