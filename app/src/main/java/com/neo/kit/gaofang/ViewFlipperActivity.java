package com.neo.kit.gaofang;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.neo.kit.R;
import com.neo.kit.gaofang.widgt.UPMarqueeView;
import com.neo.kit.main.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author neo.duan
 * @date 2019-09-02 10:52
 * @desc 请输入文件描述
 */
public class ViewFlipperActivity extends BaseActivity {

    @BindView(R.id.uPMarqueeView)
    UPMarqueeView mUPMarqueeView;

    public static void start(Context context, String title) {
        Intent starter = new Intent(context, ViewFlipperActivity.class);
        starter.putExtra("title", title);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_viewflipper;
    }

    @Override
    protected void initTop() {
        setTitle(getIntent().getStringExtra("title"));
    }

    @Override
    protected void initView() {
        //创建公告View
        List<View> views = new ArrayList<View>();
        for (int i = 0; i < 10; i++) {
            views.add(createNoticeView());
        }
        mUPMarqueeView.setViews(views);
        mUPMarqueeView.startFlipping();
    }

    @Override
    protected void initData() {

    }

    private View createNoticeView() {
        return View.inflate(mContext, R.layout.item_main_notice, null);
    }
}
