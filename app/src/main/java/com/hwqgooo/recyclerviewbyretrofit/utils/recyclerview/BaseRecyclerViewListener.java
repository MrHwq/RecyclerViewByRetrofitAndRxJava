package com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by weiqiang on 2016/6/14.
 */
public abstract class BaseRecyclerViewListener<T extends ViewHolderInject> implements
        RecyclerView.OnItemTouchListener {
    private GestureDetector mGestureDetector;

    public BaseRecyclerViewListener(final RecyclerView recyclerView) {
        // 识别并处理手势
        mGestureDetector = new GestureDetector(recyclerView.getContext(), new GestureDetector
                .SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    int position = recyclerView.indexOfChild(childView);
                    onItemClick((T) recyclerView.getChildViewHolder(childView), position);
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // 长按
                // 根据findChildViewUnder(float x, float y)来算出哪个item被选择了
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                // 有item被选则且监听器不为空触发长按事件
                if (childView != null) {
                    onItemLongClick((T) recyclerView.getChildViewHolder(childView), recyclerView
                            .indexOfChild(childView));
                }
            }
        });
    }

    public abstract void onItemClick(T holder, int position);

    public void onItemLongClick(T holder, int position) {
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
