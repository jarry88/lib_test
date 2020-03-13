package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SlidingConflictRecyclerView extends RecyclerView {

    public SlidingConflictRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SlidingConflictRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingConflictRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean canScrollHorizontally = canScrollHorizontally(-1) || canScrollHorizontally(1);
        boolean canScrollVertically = canScrollVertically(-1) || canScrollVertically(1);
        if (canScrollHorizontally || canScrollVertically) {
            ViewParent parent = getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.dispatchTouchEvent(event);
    }

}

