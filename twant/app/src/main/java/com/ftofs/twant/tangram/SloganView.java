package com.ftofs.twant.tangram;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

public class SloganView extends LinearLayout implements ITangramViewLifeCycle {
    Context context;

    public SloganView(Context context) {
        this(context, null);
    }

    public SloganView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SloganView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);


        View contentView = LayoutInflater.from(context).inflate(R.layout.tangram_layout_home_slogan_view, this, false);

        addView(contentView);
    }

    @Override
    public void cellInited(BaseCell cell) {

    }

    @Override
    public void postBindView(BaseCell cell) {

    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }
}

