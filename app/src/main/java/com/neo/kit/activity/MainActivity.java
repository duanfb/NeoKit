package com.neo.kit.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.neo.baselib.dialog.SelectMenuDialog;
import com.neo.baselib.util.MaterialDialogUtils;
import com.neo.kit.R;
import com.neo.kit.adapter.MainAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author neo.duan
 * @date 2019/08/07
 * @desc 首页
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CAMERA_REQUEST = 0x111;
    private static final int PICK_REQUEST = 0x222;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.rcv_main)
    RecyclerView mRecyclerView;
    MainAdapter mAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTop() {
        setTitle("Home");
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setAdapter(mAdapter = new MainAdapter(this));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            showMessage("" + position);
        });
    }

    @Override
    protected void initData() {
//        mAdapter.setNewData(null);
//        mAdapter.loadMoreEnd();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_saved:
                break;
            case R.id.nav_setting:
                break;
            case R.id.nav_about:
                break;
            default:
                break;

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            MaterialDialogUtils.show(mContext, "Are you sure you want to exit？", (dialogInterface, i) -> finish());
        }
    }
}
