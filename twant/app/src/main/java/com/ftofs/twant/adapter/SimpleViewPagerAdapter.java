package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.ftofs.twant.R;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * @author gzp
 */
public class SimpleViewPagerAdapter extends PagerAdapter {
    private final List<View> mViews;
    private final Context mContext;

    public SimpleViewPagerAdapter(Context context,List<View> mViews) {
        this.mViews = mViews;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mViews == null) {
            return 0;
        } else {
            return mViews.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ViewGroup testPager = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.seller_add_good_primary_widget, container, false);
        View view = mViews.get(position);
        if (view.getParent() == null) {
//            container.removeView(view);
        }else{
            ((ViewGroup) view.getParent()).removeView(view);}
        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
//        container.removeAllViews();
    }

}
