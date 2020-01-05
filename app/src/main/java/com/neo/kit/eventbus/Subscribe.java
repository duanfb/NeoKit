package com.neo.kit.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author neo.duan
 * @date 2020-01-05 18:30
 * @desc 自定义订阅注解
 */
@Target(ElementType.METHOD) //定义在方法上
@Retention(RetentionPolicy.RUNTIME) //定义运行时注解,在运行时起作用
public @interface Subscribe {
    ThreadMode threadMode() default ThreadMode.POSTING;
}
