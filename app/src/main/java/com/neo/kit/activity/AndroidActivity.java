package com.neo.kit.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.neo.kit.R;

import butterknife.OnClick;

/**
 * @author neo.duan
 * @date 2019-08-26 14:26
 * @desc 请输入文件描述
 */
public class AndroidActivity extends BaseActivity {
    private static final String EXTRA_TITLE = "extra_title";

    public static void start(Context context, String title) {
        Intent starter = new Intent(context, AndroidActivity.class);
        starter.putExtra(EXTRA_TITLE, title);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_android;
    }

    @Override
    protected void initTop() {
        setTitle(getIntent().getStringExtra(EXTRA_TITLE));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_dagger2, R.id.tv_webView, R.id.tv_rcv})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dagger2:
                MarkDownActivity.start(mContext, "dagger2", "android/dagger2.md");
                break;
            case R.id.tv_webView:
                MarkDownActivity.start(mContext, "WebView", "android/WebView.md");
                break;
            case R.id.tv_rcv:
                MarkDownActivity.start(mContext, "RecyclerView", "android/RecyclerView.md");
                break;
            default:
                break;
        }
    }
}
