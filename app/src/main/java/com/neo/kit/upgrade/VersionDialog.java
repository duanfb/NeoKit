package com.neo.kit.upgrade;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import hik.common.fp.uikit.R;

/**
 * author: neo.duan
 * date: 2020/08/06
 * desc: 更新版本dialog
 **/
public class VersionDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView mTvMsg;
    private TextView mTvPositive, mTvNegative;
    private CustomDialogOnClickListener mOnClickListener;

    private String mVersionName;
    private String mMsg;
    private boolean mForceUpdate;

    public VersionDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fp_phone_baseline_dialog_version);
        findView();
        initView();
        initListener();

        setCancelable(false);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void findView() {
        mTvNegative = findViewById(R.id.tv_phone_dialog_version_cancel);
        mTvPositive = findViewById(R.id.tv_phone_dialog_version_update);
        mTvMsg = findViewById(R.id.tv_phone_dialog_version_msg);
    }

    private void initView() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.fp_phone_popupAnimation);
        }

        String msg = String.format(mContext.getString(R.string.fp_phone_new_version_text),
                mVersionName) + "\n\n" + mMsg;
        mTvMsg.setText(msg);
        // 判断是否强制更新
        if (mForceUpdate) {
            mTvNegative.setVisibility(View.GONE);
        } else {
            mTvNegative.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        mTvPositive.setOnClickListener(this);
        mTvNegative.setOnClickListener(this);
    }

    public VersionDialog setVersionName(String versionName) {
        this.mVersionName = versionName;
        return this;
    }

    public VersionDialog setMsg(String msg) {
        this.mMsg = msg;
        return this;
    }

    public VersionDialog setForceUpdate(boolean forceUpdate) {
        this.mForceUpdate = forceUpdate;
        return this;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_phone_dialog_version_cancel) {
            // 取消
            dismiss();
        } else if (id == R.id.tv_phone_dialog_version_update) {
            dismiss();
            if (mOnClickListener != null) {
                mOnClickListener.onClick(1);
            }
        }
    }


    public VersionDialog setOnClickListener(CustomDialogOnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        return this;
    }

    public interface CustomDialogOnClickListener {
        void onClick(int which);
    }
}
