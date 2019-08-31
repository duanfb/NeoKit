package com.neo.kit.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.neo.baselib.util.ToastUtil;
import com.neo.kit.R;

import butterknife.ButterKnife;

/**
 * @author neo.duan
 * @date 2019/6/8
 * @desc 请描述该类
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    Toolbar mToolbar;
    TextView mTvRight;
    ViewGroup mContainer;
    ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        super.setContentView(R.layout.activity_base);

        initBaseView();
    }

    private void initBaseView() {
        mToolbar = findViewById(R.id.base_toolBar);
        mTvRight = findViewById(R.id.tv_base_right);
        mContainer = findViewById(R.id.fl_content_container);

        supportActionBar(mToolbar);
        setContentView(getLayoutResId());
        initIntent(getIntent());
        initTop();
        initView();
        initData();
    }

    protected void supportActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("");
        }
        mToolbar.setNavigationOnClickListener(
                (v) -> onBackPressed()
        );
    }

    @Override
    public void setContentView(int layoutResID) {
        if (layoutResID != 0) {
            setContentView(View.inflate(this, layoutResID, null));
        }
    }

    @Override
    public void setContentView(View contentView) {
        mContainer.removeAllViews();
        mContainer.addView(contentView);
        ButterKnife.bind(this, contentView);
    }

    public void initIntent(Intent intent) {

    }

    protected void enableTop(boolean enable) {
        mToolbar.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    protected void enableBack(boolean enable) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enable);
        }
    }

    protected void enableNav(boolean enable) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(enable);
            actionBar.setDisplayShowHomeEnabled(enable);
        }
    }

    protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void enableRight(boolean enable) {
        mTvRight.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    protected void enableRight(String text, View.OnClickListener listener) {
        enableRight(!TextUtils.isEmpty(text));
        mTvRight.setText(text);
        mTvRight.setOnClickListener(listener);
    }

    public void showLoading() {
        showLoading("正在加载");
    }

    public void showLoading(String message) {
        if (this.isFinishing()) {
            return;
        }
        if (mLoadingDialog == null) {
            synchronized (ProgressDialog.class) {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new ProgressDialog(mContext);
                }
            }
        }
        mLoadingDialog.setMessage(message);
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void hideLoading() {
        if (null != mLoadingDialog) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        }
    }

    protected void showMessage(String msg) {
        ToastUtil.show(getApplication(), msg);
    }

    protected abstract int getLayoutResId();

    protected abstract void initTop();

    protected abstract void initView();

    protected abstract void initData();

}
