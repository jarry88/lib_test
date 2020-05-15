package com.ftofs.twant.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class SimpleViewPagerAdapter extends PagerAdapter {
    private final List<View> mViews;

    public SimpleViewPagerAdapter(List<View> mViews) {
        this.mViews = mViews;
    }

    @Override
    public int getCount() {
        if (mViews != null) {

            return 0;
        } else {
            return mViews.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

}
