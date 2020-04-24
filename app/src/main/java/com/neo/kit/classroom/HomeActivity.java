package com.neo.kit.classroom;

import android.content.Context;
import android.content.Intent;

import com.neo.kit.main.BaseActivity;

/**
 * @author neo.duan
 * @date 2020/3/21
 * @desc 请描述文件
 */
public class HomeActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, HomeActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void initTop() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
