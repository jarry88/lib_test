package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import com.ftofs.twant.interfaces.ViewSizeChangedListener;
import com.ftofs.twant.log.SLog;

public class SizeChangedRecyclerView extends RecyclerView {
    ViewSizeChangedListener viewSizeChangedListener;

    public SizeChangedRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public SizeChangedRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SizeChangedRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        SLog.info("w[%d], h[%d], oldw[%d], oldh[%d]", w, h, oldw, oldh);
        if (viewSizeChangedListener != null) {
            viewSizeChangedListener.onViewSizeChanged(this, w, h, oldw, oldh);
        }
    }


    public void setViewSizeChangedListener(ViewSizeChangedListener listener) {
        viewSizeChangedListener = listener;
    }
}
