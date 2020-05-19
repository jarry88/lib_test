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
        if (mViews != null) {

            return 0;
        } else {
            return mViews.size()+1;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ViewGroup testPager = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.black_layout, container, false);

        if (position == 0) {
            RecyclerView rvList = new RecyclerView(mContext);
            TestAdapter testAdapter = new TestAdapter();
            rvList.setAdapter(testAdapter);
            rvList.setLayoutManager(new LinearLayoutManager(mContext));
            rvList.setBackgroundColor(Color.parseColor("#FFFFAA"));

            testPager.addView(rvList, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            TextView textView = new TextView(mContext);
            textView.setText("Hello");
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(24);

            testPager.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }


        container.addView(testPager);
        return testPager;
//        Context context;
//        Context context;
//        TextView textView = new TextView(getContext());
//        textView.setText("ss");
//        container.addView(textView);
//        if (position == 1) {
////                    textView.setText("2w");
////                    RecyclerView recyclerView= new RecyclerView(container.getContext());
////                    recyclerView.setBackgroundColor(Color.BLUE);
////                    recyclerView.getLayoutParams().height = 500;
//            return recyclerReturnView;
//        }
//        return textView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

}
