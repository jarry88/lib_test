package com.ftofs.twant.tangram;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

public class CarouselView extends LinearLayout implements ITangramViewLifeCycle {
    Context context;
    TextView tvTest;

    public CarouselView(Context context) {
        this(context, null);
    }

    public CarouselView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.tangram_layout_home_carousel_view, this, false);
        tvTest = contentView.findViewById(R.id.tv_test);

        addView(contentView);
    }

    @Override
    public void cellInited(BaseCell cell) {

    }

    @Override
    public void postBindView(BaseCell cell) {
        int pos = cell.pos;

        if (pos == 0) {
            tvTest.setBackgroundColor(Color.RED);
        } else if (pos == 1) {
            tvTest.setBackgroundColor(Color.GREEN);
        } else if (pos == 2) {
            tvTest.setBackgroundColor(Color.BLUE);
        }
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }
}
