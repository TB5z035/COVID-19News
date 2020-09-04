package com.java.zhangjiayou.ui.home;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public abstract class MyOnItemTouchListener extends RecyclerView.SimpleOnItemTouchListener {

    private boolean isFling = false;

    @Override
    public boolean onInterceptTouchEvent(@NonNull final RecyclerView rv, @NonNull MotionEvent e) {
        return new GestureDetector(rv.getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                System.out.println("onDown" + new Date());
                isFling = false;
                return super.onDown(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                System.out.println("onSingleTap" + new Date());

                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                System.out.println("onLongPress" + new Date());

                super.onLongPress(e);
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && !isFling) {
                    int position = rv.getChildLayoutPosition(childView);
                    doOnItemLongClickListener(childView, position);
                }
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                System.out.println("onScroll" + new Date());

//                isFling = Math.abs(e1.getY() - e2.getY()) > MIN_DISTANCE;
                isFling = true;
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        }).onTouchEvent(e);
    }

    abstract void doOnItemLongClickListener(View view, int position);

//    abstract void doOnItemClickListener(View view, int position);
}
