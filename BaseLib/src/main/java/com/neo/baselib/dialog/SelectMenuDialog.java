package com.neo.baselib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.neo.baselib.R;


/**
 * @author : neo.duan
 * @date : 	 2016/10/24
 * @desc : 底部弹出菜单选择框
 */
public class SelectMenuDialog extends Dialog implements OnClickListener {

    private Context mContext;
    private View mMenuView;
    private Button mBtnCancel;
    private LinearLayout mMenuContainer;

    public SelectMenuDialog(Context context) {
        super(context, R.style.MyDialogStyleBottom);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_select_menu, null);

        setContentView(mMenuView);

        //设置BottomDialog的宽高属性
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

            //让Dialog显示在屏幕的底部
            window.setGravity(Gravity.BOTTOM);
        }


//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        setBackgroundDrawable(dw);

        // menuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.select_menu_layout_root).getTop();
                int y = (int) event.getY();
                System.out.println("height：" + height + "=====y:" + y);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        initView();
        initListener();
    }

    private void initView() {
        mBtnCancel = (Button) mMenuView.findViewById(R.id.btn_select_menu_cancel);
        mMenuContainer = (LinearLayout) mMenuView.findViewById(R.id.ll_select_menu_container);
    }

    private void initListener() {
        mBtnCancel.setOnClickListener(this);
    }

    /**
     * 设置菜单
     * s
     *
     * @param menu     菜单数组
     * @param listener 菜单点击监听器
     */
    public void setMenu(String[] menu, final SelectMenuClickListener listener) {
        if (menu == null || menu.length < 1) {
            throw new IllegalArgumentException("the menu string array is null or length < 1");
        }
        for (int i = 0; i < menu.length; i++) {
            View itemView = View.inflate(mContext, R.layout.layout_select_menu_item, null);
            Button btn = (Button) itemView.findViewById(R.id.tv_select_menu_item);
            btn.setText(menu[i]);
            //最后一个item，隐藏底部线条
            if (i == menu.length - 1) {
                itemView.findViewById(R.id.select_menu_item_line).setVisibility(View.GONE);
            }
            //添加事件监听器
            btn.setTag(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onSelectMenuClick(SelectMenuDialog.this, view, (int) view.getTag());
                    }
                }
            });
            //添加到容器
            mMenuContainer.addView(itemView, i);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_select_menu_cancel) {
            dismiss();
        } else if (i == R.id.ll_select_menu_container) {

        }
    }

    /**
     * 菜单点击监听器
     */
    public interface SelectMenuClickListener {
        /**
         * 回调方法
         *
         * @param dialog
         * @param item
         * @param position
         */
        void onSelectMenuClick(Dialog dialog, View item, int position);
    }
}
