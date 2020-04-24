package com.neo.baselib.util;


import org.greenrobot.eventbus.EventBus;

/**
 * Author: neo.duan
 * Date: 2017/02/18
 * Desc: EventBus工具类
 */
public class EventBusUtil {
    //EventBus3.0使用方式
    /*@Subscribe(threadMode = ThreadMode.MainThread) //在ui线程执行
    public void onUserEvent(UserEvent event) {
    }
    @Subscribe(threadMode = ThreadMode.BackgroundThread) //在后台线程执行
    public void onUserEvent(UserEvent event) {
    }
    @Subscribe(threadMode = ThreadMode.Async) //强制在后台执行
    public void onUserEvent(UserEvent event) {
    }
    @Subscribe(threadMode = ThreadMode.PostThread) //默认方式, 在发送线程执行
    public void onUserEvent(UserEvent event) {
    }*/


    /***
     * 添加到订阅队列
     *
     * @param obj 用于接收消息的对象，该类必须要有一个公用的on***Event(Event) 方法
     */
    public static void register(Object obj) {
        if (!EventBus.getDefault().isRegistered(obj)) {
            EventBus.getDefault().register(obj);
        }
    }


    /**
     * 讲订阅对象从队列中移除
     *
     * @param obj 要移除的订阅对象
     */
    public static void unregister(Object obj) {
        if (EventBus.getDefault().isRegistered(obj)) {
            EventBus.getDefault().unregister(obj);
        }
    }


    /**
     * 发送消息到事件队列中
     *
     * @param msg 消息
     */
    public static void postEvent(Object msg) {
        EventBus.getDefault().post(msg);
    }
}
