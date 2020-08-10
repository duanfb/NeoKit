package com.neo.kit.upgrade.proxy.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.neo.kit.upgrade.entity.UpdateEntity;
import com.neo.kit.upgrade.proxy.IPrompterProxy;
import com.neo.kit.upgrade.proxy.IUpdateProxy;
import com.neo.kit.upgrade.service.OnFileDownloadListener;


/**
 * 默认版本更新提示器代理
 *
 * @author xuexiang
 * @since 2020/6/9 12:19 AM
 */
public class DefaultPrompterProxyImpl implements IPrompterProxy {

    private IUpdateProxy mIUpdateProxy;

    public DefaultPrompterProxyImpl(IUpdateProxy proxy) {
        mIUpdateProxy = proxy;
    }

    @Override
    public void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener) {
        if (mIUpdateProxy != null) {
            mIUpdateProxy.startDownload(updateEntity, downloadListener);
        }
    }

    @Override
    public void backgroundDownload() {
        if (mIUpdateProxy != null) {
            mIUpdateProxy.backgroundDownload();
        }
    }

    @Override
    public void cancelDownload() {
        if (mIUpdateProxy != null) {
            mIUpdateProxy.cancelDownload();
        }
    }

    @Override
    public void recycle() {
        if (mIUpdateProxy != null) {
            mIUpdateProxy.recycle();
            mIUpdateProxy = null;
        }
    }

}
