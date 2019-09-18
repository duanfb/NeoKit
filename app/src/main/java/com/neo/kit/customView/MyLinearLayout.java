package com.neo.kit.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author neo.duan
 * @date 2019-08-31 18:18
 * @desc 自定义LinearLayout
 */
public class MyLinearLayout extends ViewGroup {


    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 通过测量子View宽高，设置存储自己的宽高
     *
     * @param widthMeasureSpec  父类分发的widthSpec
     * @param heightMeasureSpec 父类分发的heightSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取模式
        /**
         * 模式的与xml关系为：
         * wrap_content -> MeasureSpec.AT_MOST:子元素至多能达到指定大小的值
         * match_parent -> MeasureSec.EXACTLY:父元素决定子元素的确定大小
         * 具体值(x dp) ->  MeasureSec.EXACTLY
         */
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        //获取值
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        //经过一系列计算，计算自身的宽高
        int width = 0;
        int height = 0;
        /**
         * 遍历子View测量
         */
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            //对子View测量
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            //处理margin问题
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + (lp.leftMargin + lp.rightMargin);
            int childHeight = childView.getMeasuredHeight() + (lp.topMargin + lp.bottomMargin);

            //宽度
            width = Math.max(width, childWidth);
            height += childHeight;
        }


        //设置存储宽高值:如果用户设置了具体值，则使用系统计算的值
        setMeasuredDimension(measureWidthMode == MeasureSpec.EXACTLY ? measureWidth : width,
                measureHeightMode == MeasureSpec.EXACTLY ? measureHeight : height);
    }

    /**
     * getMeasureWidth():在measure完成后可以获取到宽高值,因为需要靠setMeasureDimension进行设置值
     * getWidth():在layout完成后可以获取到宽高值,因为要靠layout进行设置值
     * 当View绘制显示出来后，这2个值相等
     *
     */

    /**
     * 对子View进行layout
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //遍历子View进行layout
        int top = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int measuredWidth = childView.getMeasuredWidth();
            int measuredHeight = childView.getMeasuredHeight();

            //处理margin问题
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            measuredWidth += lp.leftMargin + lp.rightMargin;
            measuredHeight += lp.topMargin + lp.bottomMargin;

            childView.layout(0, top, measuredWidth, top + measuredHeight);
            top += measuredHeight;
        }
    }

    /**
     * 重写下面创建默认LayoutParams方法，默认的创建的LayoutParams只能获取子View到宽高，
     * 我们return MarginLayoutParams可以获取子View的margin值
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
