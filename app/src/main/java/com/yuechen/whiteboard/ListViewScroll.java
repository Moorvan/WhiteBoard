package com.yuechen.whiteboard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListViewScroll extends ListView {


    public ListViewScroll(Context context) {
        super(context);
    }

    public ListViewScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
