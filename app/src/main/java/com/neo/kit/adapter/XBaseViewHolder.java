package com.neo.kit.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;


/**
 * Author: neo.duan
 * Date: 2017/02/21
 * Desc: 再次封装BaseViewHolder
 */
public class XBaseViewHolder extends BaseViewHolder {

    public XBaseViewHolder(View view) {
        super(view);
    }

    /**
     * 设置图片url
     *
     * @param viewId
     * @param url
     * @return
     */
    public BaseViewHolder setImageUrl(int viewId, String url) {
//        XImageView imageView = getView(viewId);
//        if (imageView != null) {
//            imageView.setImageUrl(url);
//        }
        return this;
    }
}
