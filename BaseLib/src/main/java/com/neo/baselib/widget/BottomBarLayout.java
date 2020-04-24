package com.neo.baselib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.neo.baselib.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author neo.duan
 * @date 2017/02/18
 * @desc 自定义底部导航BottomBar
 */
public class BottomBarLayout extends LinearLayout {
    private final String TAG = BottomBarLayout.class.getSimpleName();

    /**
     * 底部文本数组
     */
    private String[] mTextArr;

    /**
     * 底部选中图片数组
     */
    private TypedArray mSelectedArr;

    /**
     * 底部未选中图片数组
     */
    private TypedArray mUnSelectedArr;

    private int mTextColorOn;
    private int mTextColorOff;

    /**
     * 存放底部每个item集合
     */
    private List<View> mItemViewList = new ArrayList<>();

    /**
     * 当前点击的position
     */
    private int mCurrentPosition = 0;

    private OnBottomItemSelectedListener mListener;

    public BottomBarLayout(Context context) {
        this(context, null);
        initView(context);
    }

    public BottomBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttr(context, attrs);
        initView(context);
    }

    public void setOnBottomItemSelectedListener(OnBottomItemSelectedListener listener) {
        mListener = listener;
    }

    /**
     * 初始化自定义属性
     */
    private void initCustomAttr(Context context, AttributeSet attrs) {
        TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.BottomBarLayout);

        // 获取文本颜色
        mTextColorOn = a.getColor(R.styleable.BottomBarLayout_textColorOn,
                ContextCompat.getColor(context, android.R.color.black));
        mTextColorOff = a.getColor(R.styleable.BottomBarLayout_textColorOff,
                ContextCompat.getColor(context, android.R.color.black));

        // 获取自定义属性资源ID
        int textArrResId = a.getResourceId(R.styleable.BottomBarLayout_defaultText, 0);
        int selectedArrResId = a.getResourceId(
                R.styleable.BottomBarLayout_defaultIconSelected, 0);
        int unSelectedArrResId = a.getResourceId(
                R.styleable.BottomBarLayout_defaultIconUnSelected, 0);

        // 初始化底部文本数组和图片数组
        if (textArrResId != 0) {
            mTextArr = getResources().getStringArray(textArrResId);
        }
        if (selectedArrResId != 0) {
            mSelectedArr = getResources().obtainTypedArray(selectedArrResId);
        }
        if (unSelectedArrResId != 0) {
            mUnSelectedArr = getResources().obtainTypedArray(unSelectedArrResId);
        }

        a.recycle();
    }

    private void initView(Context context) {
        // 设置LinearLayout水平
        this.setOrientation(LinearLayout.HORIZONTAL);

        // 底部item的数目以textArray为准
        for (int i = 0; i < mTextArr.length; i++) {
            View itemView = initItem(context, i);
            // 初始化item上的数据
            initItemData(itemView, i);
            // 增加到集合中
            mItemViewList.add(itemView);
            // 填充到整个布局
            this.addView(itemView);
        }
        // 默认第0个选中,其他不选中
        setItemSelected(mCurrentPosition);
    }

    public String[] getTextArr() {
        return mTextArr;
    }

    public void setTextArr(String[] mTextArr) {
        this.mTextArr = mTextArr;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable superData = super.onSaveInstanceState();
        bundle.putParcelable("bottom_bar_data", superData);
        bundle.putInt("position", mCurrentPosition);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable("bottom_bar_data");
        mCurrentPosition = bundle.getInt("position");
        setItemSelected(mCurrentPosition);
        super.onRestoreInstanceState(superData);
    }

    /**
     * 初始化每个item
     */
    private View initItem(Context context, int position) {
        // 创建item布局
        View itemView = View.inflate(context, R.layout.item_bottom_bar, null);
        // 配置item参数
        final LayoutParams params = new LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);

        params.gravity = Gravity.CENTER;

        itemView.setLayoutParams(params);
        params.weight = 1.0f;
        // 设置item点击事件
        itemView.setTag(position);
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // 点击后显示选中状态并回调
                Integer position = (Integer) view.getTag();
                setItemSelected(position);
            }
        });

        return itemView;
    }

    /**
     * 初始化item上数据显示
     *
     * @param itemView itemView
     * @param position position
     */
    private void initItemData(View itemView, int position) {
        ImageView ivImg = itemView
                .findViewById(R.id.iv_tab_item_img);
        TextView tvName = itemView
                .findViewById(R.id.ctv_tab_item_name);

        ivImg.setImageDrawable(mUnSelectedArr.getDrawable(position));
        tvName.setText(mTextArr[position]);
        tvName.setTextColor(mTextColorOn);
    }

    /**
     * 设置item是否选中
     *
     * @param position position
     */
    public void setItemSelected(int position) {
        // 标记当前选中
        this.mCurrentPosition = position;
        // 选中项设置选中状态
        // 遍历其他的不选中状态
        for (int i = 0; i < mItemViewList.size(); i++) {
            if (i == position) {
                setItemSelectedState(i);
            } else {
                setItemUnSelectedState(i);
            }
        }
        // 回调方法应该跟着选项走
        if (mListener != null) {
            mListener.onBottomItemSelected(mItemViewList.get(mCurrentPosition), mCurrentPosition);
        }
    }

    /**
     * 设置某个位置item选中
     *
     * @param position position
     */
    private void setItemSelectedState(int position) {
        View itemView = mItemViewList.get(position);
        ImageView ivImg = itemView
                .findViewById(R.id.iv_tab_item_img);
        TextView tvName = itemView
                .findViewById(R.id.ctv_tab_item_name);

        ivImg.setImageDrawable(mSelectedArr.getDrawable(position));
        tvName.setText(mTextArr[position]);
        tvName.setTextColor(mTextColorOn);
    }

    /**
     * 设置某个位置item不选中
     *
     * @param position position
     */
    private void setItemUnSelectedState(int position) {
        View itemView = mItemViewList.get(position);
        ImageView ivImg = itemView
                .findViewById(R.id.iv_tab_item_img);
        TextView tvName = itemView
                .findViewById(R.id.ctv_tab_item_name);

        tvName.setText(mTextArr[position]);
        ivImg.setImageDrawable(mUnSelectedArr.getDrawable(position));
        tvName.setTextColor(mTextColorOff);
    }


    /**
     * 设置小红点是否显示
     *
     * @param position 显示小红点位置
     * @param count    数量
     */
    public void displayRedDot(int position, int count) {
        if (position > mItemViewList.size() - 1) {
            return;
        }
        String countStr;
        if (count <= 0) {
            count = 0;
        }
        if (count > 9) {
            countStr = "9+";
        } else {
            countStr = String.valueOf(count);
        }

        View itemView = mItemViewList.get(position);
        TextView tvRedPoint = itemView.findViewById(R.id.tv_red_point);
        tvRedPoint.setText(countStr);
        tvRedPoint.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int position) {
        this.mCurrentPosition = position;
    }

    /**
     * 底部item选中监听器
     */
    public interface OnBottomItemSelectedListener {

        /**
         * tab 选中
         *
         * @param item     item
         * @param position position
         */
        void onBottomItemSelected(View item, int position);
    }
}
