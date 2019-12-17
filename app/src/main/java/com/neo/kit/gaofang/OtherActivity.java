package com.neo.kit.gaofang;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.neo.kit.R;
import com.neo.kit.main.BaseActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import butterknife.OnClick;

/**
 * @author neo.duan
 * @date 2019-09-02 10:52
 * @desc 请输入文件描述
 */
public class OtherActivity extends BaseActivity {


    public static void start(Context context) {
        Intent starter = new Intent(context, OtherActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_other;
    }

    @Override
    protected void initTop() {
        setTitle("其他");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.btn_jdbc})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_jdbc:
                break;
            default:
                break;
        }
    }

    /**
     * thanks:https://www.jianshu.com/p/1412eeda4197
     */
    private void jdbc() {
        /**
         * 加入mysql-connector-java-5.1.30-bin.jar包,才能获取到该类
         */

        try {
            Class.forName("com.mysql.jdbc.Driver");//动态加载类
            /**
             *  数据库名，数据表名，用户名，密码
             *         "jdbc:sqlserver://${阿里云给的外网连接地址}:" +
             *                 "3433;databaseName=${数据库名称}","${帐号}","${密码}"
             *                 "jdbc:mysql://云服务器ip地址:3306/访问的数据库名称"
             */
            String url = "jdbc:mysql://mysql.lianfangti.top:3306/public";
            //上面语句中 mysql://mysql.lianfangti.top为你的mysql服务器地址 3306为端口号   public是你的数据库名 根据你的实际情况更改
            Connection conn = DriverManager.getConnection(url, "root", "root");
            //使用 DriverManger.getConnection链接数据库  第一个参数为连接地址 第二个参数为用户名 第三个参数为连接密码  返回一个Connection对象
            if (conn != null) { //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                Log.d("调试", "连接成功");
                Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                String sql = "select * from user"; //要执行的sql语句
                ResultSet rs = stmt.executeQuery(sql); //使用executeQury方法执行sql语句 返回ResultSet对象 即查询的结果
            } else {
                Log.d("调试", "连接失败");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
