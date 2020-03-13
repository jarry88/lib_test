package com.ftofs.twant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Uri;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.HackyViewPager;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * 縮放圖片，橫滑動切換下一張圖片
 * @author gzp
 */
public class ViewPagerFragment extends BaseFragment implements View.OnClickListener {
    private List<String> currGalleryImageList;
    private SamplePagerAdapter viewPagerAdapter;

    public static ViewPagerFragment newInstance(List<String> currGalleryImageList) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.currGalleryImageList = currGalleryImageList;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_view_pager,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.setOnClickListener(view,R.id.view_pager,this);
        Util.setOnClickListener(view,R.id.btn_back_round,this);
        HackyViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPagerAdapter = SamplePagerAdapter.newInstance(currGalleryImageList, getContext());
        viewPagerAdapter.setmOnItemClickListener(v ->
                pop());
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        SLog.info("v.getId(%d)",v.getId());
        int id = v.getId();
        if (id == R.id.view_pager) {
            SLog.info(currGalleryImageList.toString());
        } else if(id==R.id.btn_back_round) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }

    static class SamplePagerAdapter extends PagerAdapter {
        private List<String> currGalleryImageList;
        private Context context;

        public void setmOnItemClickListener(View.OnClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        private View.OnClickListener mOnItemClickListener;

        public static SamplePagerAdapter newInstance(List<String> currGalleryImageList, Context context) {
            SamplePagerAdapter samplePagerAdapter = new SamplePagerAdapter();
            samplePagerAdapter.currGalleryImageList = currGalleryImageList;
            samplePagerAdapter.context = context;
            return samplePagerAdapter;
        }
        @Override
        public int getCount() {
            return currGalleryImageList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());

            // 如果是本地路徑，還要添加上file://協議前綴
            String imagePath=currGalleryImageList.get(position);
            if (imagePath.startsWith("/")) {
                imagePath = "file://" + imagePath;
                Glide.with(context).load(android.net.Uri.parse(imagePath)).into(photoView);
            } else {
                imagePath=StringUtil.normalizeImageUrl(currGalleryImageList.get(position));

                Glide.with(context).load(imagePath).into(photoView);
            }
            //photoView.setImageURI(Uri.parse(StringUtil.normalizeImageUrl(currGalleryImageList.get(position))));
            //photoView.setImageResource(R.drawable.icon_wait_pay);

            // Now just add PhotoView to ViewPager and return it
            photoView.setOnClickListener(v -> {
                mOnItemClickListener.onClick(v);
            });
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
