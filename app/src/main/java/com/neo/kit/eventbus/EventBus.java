package com.neo.kit.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author neo.duan
 * @date 2020-01-05 18:28
 * @desc 手写EventBus, 单例
 */
public class EventBus {

    private static EventBus instance;
    /**
     * 定义一个map存注解获取的类，类中定义的事件方法(可能有多个事件)
     */
    private Map<Object, List<MethodManager>> map;

    private EventBus() {
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    // 注册
    public void register(Object object) {
        //先查询缓存缓存中有没有事件，没有则find
        List<MethodManager> methodManagers = map.get(object);
        if (methodManagers == null) {
            findObject(object);
        }
    }

    // 注销
    public void unRegister(Object object) {
        if (map.get(object) != null) {
            map.remove(object);
        }
    }

    private void findObject(Object object) {
        //获取类的字节码
        Class<?> aClass = object.getClass();
        //获取该类中所有的方法
        Method[] declaredMethods = aClass.getDeclaredMethods();
        List<MethodManager> list = new ArrayList<>();
        for (Method declaredMethod : declaredMethods) {
            //遍历方法，看哪个方法加了事件注解
            Subscribe annotation = declaredMethod.getAnnotation(Subscribe.class);
            if (annotation == null) {
                continue;
            }

            //获取方法注解中的线程标记
            //线程标记
            ThreadMode threadMode = annotation.threadMode();

            //获取方法中参数事件class类型
            Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            if (parameterTypes.length > 1) {
                continue;
            }

            //保存事件类型到集合中
            Class<?> parameterType = parameterTypes[0];
            MethodManager methodManager = new MethodManager(threadMode, declaredMethod, parameterType);
            list.add(methodManager);
        }
        //保存事件类型到集合中，使用object作为key
        map.put(object, list);
    }

    /**
     * 发送消息其实就是对集合进行遍历
     */
    public void post(final Object object) {
        Set<Object> objects = map.keySet();
        Iterator<Object> iterator = objects.iterator();
        while (iterator.hasNext()) {
            final Object next = iterator.next();
            List<MethodManager> list = map.get(next);
            //遍历所有的key中所有事件实体，如果匹配到object类型，则回调invoke反射调用
            for (final MethodManager methodManager : list) {
                //比较发送端的消息类型和接受者的消息类型是否一致
                if (methodManager.getType().isAssignableFrom(object.getClass())) {
                    ThreadMode threadMode = methodManager.getThreadMode();
                    invoke(next, object, methodManager.getMethod());
                }
            }
        }
    }

    /**
     * 找到匹配的方法了, 就去执行改方法
     *
     * @param next
     * @param object
     * @param method
     */
    private void invoke(Object next, Object object, Method method) {
        try {
            //通过反射调用 ，next为类对象，object为method参数对象
            method.invoke(next, object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
