package com.neo.kit.adapter;

import android.content.Context;

import com.neo.kit.R;

/**
 * @author neo.duan
 * @date 2019-08-07 11:38
 * @desc 首页adapter
 */
public class MainAdapter extends XBaseAdapter<String> {


    public MainAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_main;
    }

    @Override
    protected void convert(XBaseViewHolder helper, String name) {
        helper.setText(R.id.tv_name, name);
    }
}
