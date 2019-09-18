package com.neo.kit.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author neo.duan
 * @date 2019-08-31 16:53
 * @desc 请输入文件描述
 */
public class BasisView extends View {

    private Paint mPaint;

    public BasisView(Context context) {
        this(context, null);
    }

    public BasisView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasisView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        //设置填充样式
        mPaint.setStyle(Paint.Style.STROKE);
        //画笔宽度
        mPaint.setStrokeWidth(50);
    }

    /**
     * canvas 画布图层，相当于纸,此为绘图的关键，用canvas.drawXXX绘制相关图形
     * Paint相当于画笔
     * <p>
     * canvas背景设置
     * void drawColor()
     * void drawARGB()
     * void drawRGB()
     * <p>
     * 画直线
     * canvas.drawLine()
     * 画点
     * canvas.drawPoint()
     * 画矩形
     * canvas.drawRec
     * <p>
     * 每次调用canvas.drawXXX()都会产生一个新的图层
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆:圆心坐标，半径
        canvas.drawCircle(100, 100, 150, mPaint);
    }
}
