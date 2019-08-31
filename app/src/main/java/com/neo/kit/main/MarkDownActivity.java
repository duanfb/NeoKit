package com.neo.kit.main;

import android.content.Context;
import android.content.Intent;

import com.mukesh.MarkdownView;
import com.neo.kit.R;

import butterknife.BindView;

/**
 * @author neo.duan
 * @date 2019-08-26 14:26
 * @desc 请输入文件描述
 */
public class MarkDownActivity extends BaseActivity {
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_PATH = "extra_path";

    @BindView(R.id.tv_mark_down)
    MarkdownView mTvMarkDown;

    private String mTitle;
    private String mAssetsFilePath;

    public static void start(Context context, String title, String assetsFilePath) {
        Intent starter = new Intent(context, MarkDownActivity.class);
        starter.putExtra(EXTRA_TITLE, title);
        starter.putExtra(EXTRA_PATH, assetsFilePath);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_markdown;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        mTitle = intent.getStringExtra(EXTRA_TITLE);
        mAssetsFilePath = intent.getStringExtra(EXTRA_PATH);
    }

    @Override
    protected void initTop() {
        setTitle(mTitle);
        enableRight("实例", view -> {

        });
    }

    @Override
    protected void initView() {

    }

//    MarkdownView markdownView = (MarkdownView) findViewById(R.id.markdown_view);
//markdownView.setMarkDownText("# Hello World\nThis is a simple markdown"); //Displays markdown text
//...
//        markdownView.loadMarkdownFromAssets("README.md"); //Loads the markdown file from the assets folder
//...
//    File markdownFile=new File("filePath");
//markdownView.loadMarkdownFromFile(markdownFile); //Loads the markdown file.

    @Override
    protected void initData() {
        mTvMarkDown.loadMarkdownFromAssets(mAssetsFilePath);
    }

}
