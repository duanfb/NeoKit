package com.neo.kit.gaofang;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neo.kit.R;
import com.neo.kit.adapter.MainAdapter;
import com.neo.kit.main.BaseActivity;

import java.util.Arrays;

import butterknife.BindView;

/**
 * @author neo.duan
 * @date 2019-09-02 10:52
 * @desc 请输入文件描述
 */
public class GaoFangActivity extends BaseActivity {

    private static final String[] ITEMS = {
            "仿淘宝公告",

    };

    @BindView(R.id.rcv_gaofang)
    RecyclerView mRecyclerView;

    MainAdapter mAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, GaoFangActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_gaofang;
    }

    @Override
    protected void initTop() {
        setTitle("高仿");
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter = new MainAdapter(this));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (mAdapter.getItem(position)) {
                case "仿淘宝公告":
                    ViewFlipperActivity.start(mContext, mAdapter.getData().get(position));
                    break;
                case "高仿":
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void initData() {
        mAdapter.setNewData(Arrays.asList(ITEMS));
    }
}
