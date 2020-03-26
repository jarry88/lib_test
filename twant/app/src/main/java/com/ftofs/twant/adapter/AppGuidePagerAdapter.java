package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.AppGuideActivity;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * App引導頁PagerAdapter
 * @author zwm
 */
public class AppGuidePagerAdapter extends PagerAdapter {
    Context context;
    boolean showFramework;  // 是否显示引导页框架
    List<String> imageList = new ArrayList<>(); // 引导页背景图
    SimpleCallback simpleCallback;

    public AppGuidePagerAdapter(Context context, boolean showFramework, List<String> imageList, SimpleCallback simpleCallback) {
        this.context = context;
        this.showFramework = showFramework;
        this.imageList = imageList;
        this.simpleCallback = simpleCallback;
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
        View appGuide = LayoutInflater.from(context).inflate(R.layout.app_guide, container, false);

        if (showFramework) {
            appGuide.findViewById(R.id.app_guide_framework).setVisibility(View.VISIBLE);
        } else {
            appGuide.findViewById(R.id.app_guide_framework).setVisibility(View.GONE);
        }


        appGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == imageList.size() - 1) { // 如果是最後一頁，點擊就跳轉到MainActivity
                    SLog.info("如果是最後一頁，點擊就跳轉到MainActivity");
                    simpleCallback.onSimpleCall(EasyJSONObject.generate("event_type", AppGuideActivity.EVENT_TYPE_START_MAIN_ACTIVITY));
                } else { // 不是最后一页，跳過下一頁
                    SLog.info("不是最后一页，跳過下一頁");
                    simpleCallback.onSimpleCall(EasyJSONObject.generate("event_type", AppGuideActivity.EVENT_TYPE_GOTO_NEXT_PAGE));
                }

            }
        });


        ImageView iv = appGuide.findViewById(R.id.img_background);
        // 採用fitCenter() 齊高寬度適應是指背景圖高度保持與屏幕高度一致，隨著設備分辨率不同，背景圖片跟隨變化，寬度按照等比方式適應；
        Glide.with(context).load(StringUtil.normalizeImageUrl(imageList.get(position))).centerCrop().into(iv);
        container.addView(appGuide);

        return appGuide;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
