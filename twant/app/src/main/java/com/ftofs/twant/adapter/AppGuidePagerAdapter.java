package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * App引導頁PagerAdapter
 * @author zwm
 */
public class AppGuidePagerAdapter extends PagerAdapter {
    Context context;
    List<String> imageList;
    OnSelectedListener onSelectedListener;

    public AppGuidePagerAdapter(Context context, List<String> imageList, OnSelectedListener onSelectedListener) {
        this.context = context;
        this.imageList = imageList;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    public int getCount() {
        return imageList.size(); // 返回数据的个数
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o; // 过滤和缓存的作用
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (position == imageList.size() - 1) {
            View lastAppGuide = LayoutInflater.from(context).inflate(R.layout.last_app_guide, container, false);
            ImageView iv = lastAppGuide.findViewById(R.id.img_background);
            Glide.with(context).load(StringUtil.normalizeImageUrl(imageList.get(position))).centerCrop().into(iv);
            lastAppGuide.findViewById(R.id.btn_start_main_activity).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelectedListener != null) {
                        onSelectedListener.onSelected(null, 0, null);
                    }
                }
            });
            container.addView(lastAppGuide);

            return lastAppGuide;
        } else {
            ImageView iv = new ImageView(context);
            Glide.with(context).load(StringUtil.normalizeImageUrl(imageList.get(position))).centerCrop().into(iv);
            container.addView(iv);
            return iv;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
