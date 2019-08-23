package com.neo.kit.adapter;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.BaseAnimation;

import java.util.List;

/**
 * Author: neo.duan
 * Date: 2017/02/18
 * Desc: 再次封装BaseViewHolder
 */
public abstract class XBaseAdapter<T> extends BaseQuickAdapter<T, XBaseViewHolder> {

    //holder.addOnClickListener();需要注意顺序，先add子View的Id，再add父View的id，否则父类会覆盖点击子View事件

    public XBaseAdapter(Context context) {
        super(null);
        this.mContext = context;
        this.mLayoutResId = getLayoutResId(0);

        //设置打开动画并前10个数据不用执行动画
//        openLoadAnimation();
//        setNotDoAnimationCount(10);

        closeLoadAnimation();
    }

    /**
     * 关闭加载动画
     */
    public void closeLoadAnimation() {
        //关闭item执行动画
        openLoadAnimation(new BaseAnimation() {
            @Override
            public Animator[] getAnimators(View view) {
                return new Animator[0];
            }
        });
    }

    public void update(List<T> list) {
        setNewData(list);
    }

    /**
     * 更新某个位置item
     */
    public void update(int position) {
        if (position != -1) {
            notifyItemChanged(position + getHeaderLayoutCount());
        }
    }

    public void remove(T t) {
        if (t != null && mData.contains(t)) {
            remove(mData.indexOf(t));
        }
    }

    @Override
    public XBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mLayoutResId = getLayoutResId(viewType);
        return super.onCreateViewHolder(parent, viewType);
    }

    protected abstract int getLayoutResId(int viewType);
}
