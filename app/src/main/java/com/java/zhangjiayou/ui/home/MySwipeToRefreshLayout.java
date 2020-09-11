package com.java.zhangjiayou.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MySwipeToRefreshLayout extends SwipeRefreshLayout {
    public MySwipeToRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (isRefreshing())
            return true;
        return super.dispatchTouchEvent(ev);
    }
}
