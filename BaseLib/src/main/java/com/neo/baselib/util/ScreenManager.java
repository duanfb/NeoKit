package com.neo.baselib.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author neo.duan
 * @date 2017/02/18
 * @desc 以堆栈方式管理历史Activity
 */
public class ScreenManager implements Application.ActivityLifecycleCallbacks {
    private static Stack<Activity> activityStack;
    private static ScreenManager instance;

    private ScreenManager() {
    }

    /**
     * 视图管理器，用于完全退出
     */
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 回收堆栈中指定的activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            activityStack.remove(activity);
            activity = null;
        }
    }


    /**
     * 获取堆栈的栈顶activity
     *
     * @return 栈顶activity
     */
    public Activity currentActivity() {
        try {
            Activity activity = null;
            if (!activityStack.isEmpty()) {
                activity = activityStack.peek(); //获取栈顶对象而不移除它
            }
            return activity;
        } catch (Exception ex) {
            System.out.println("ScreenManager:currentActivity---->"
                    + ex.getMessage());

        }
        return null;
    }

    /**
     * 将activity压入堆栈
     *
     * @param activity 需要压入堆栈的activity
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.push(activity);
    }

    /**
     * 回收顶部Activity
     */
    public void popTopActivity() {
        popActivity(currentActivity());
    }

    /**
     * 关闭指定类名的所有Activity
     *
     * @param cls cls
     */
    public void popActivity(Class<?> cls) {
        try {
            Iterator<Activity> iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (activity == null) {
                    iterator.remove();
                    continue;
                }
                if (activity.getClass().equals(cls)) {
                    iterator.remove();
                    activity.finish();
                }
            }
        } catch (Exception e) {
            System.out.println("ScreenManager:currentActivity---->"
                    + e.getMessage());
        }
    }

    /**
     * 回收堆栈中所有Activity
     */
    public void popAllActivity() {
        Activity activity = null;
        try {
            while (!activityStack.isEmpty()) {
                activity = currentActivity();
                if (activity != null) {
                    popActivity(activity);
                }
            }
        } catch (Exception ex) {
            System.out.println("ScreenManager:popAllActivity---->"
                    + ex.getMessage());
        } finally {
            activity = null;
        }
    }

    /**
     * 当前堆栈Activity的数量
     */
    public int size() {
        if (activityStack == null) {
            return 0;
        } else {
            return activityStack.size();
        }
    }

    private int mCounter;
    private boolean mIsRunInBackground;

    public boolean isRunInBackground() {
        return mIsRunInBackground;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        pushActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        mCounter++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //后台回到前台
        if (mIsRunInBackground) {
            mIsRunInBackground = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        mCounter--;
        if (mCounter == 0) {
            mIsRunInBackground = true;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        popActivity(activity);
    }
}