package com.yuechen.whiteboard.layoutUtil;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint ;
    /**
     * 所有初始化可以都放在构造方法中，来初始化一些基本参数
     */
    public MyItemDecoration(){
        mPaint = new Paint() ;
        mPaint.setTextSize(0.5f);
        mPaint.setAntiAlias(true);  //设置抗锯齿
        mPaint.setColor(Color.GRAY); //设置画笔颜色
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        if (position != 0){
            outRect.top = 0 ;
        }
    }



    // 绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount() ;


        // 指定绘制的区域
        Rect rect = new Rect() ;
        rect.left = parent.getPaddingLeft() ;
        rect.right = parent.getWidth() - parent.getPaddingRight() ;
        // 头部第一个不需要绘制分割线，所以直接从第二个开始
        for (int i = 1; i < childCount; i++) {
            // 分割线的底部就是 item的头部
            rect.bottom = parent.getChildAt(i).getTop() ;
            rect.top = rect.bottom - 10 ;
            c.drawRect(rect , mPaint);
        }
    }
}
