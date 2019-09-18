package com.neo.kit.android;

import androidx.recyclerview.widget.RecyclerView;

import com.neo.kit.R;
import com.neo.kit.main.BaseActivity;

import butterknife.BindView;

/**
 * @author neo.duan
 * @date 2019-09-02 15:24
 * @desc 请输入文件描述
 */
public class RecyclerViewActivity extends BaseActivity {

    @BindView(R.id.rcv)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recyclerview;
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
