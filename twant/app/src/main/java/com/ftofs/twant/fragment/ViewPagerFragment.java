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
import com.ftofs.twant.domain.goods.GoodsImage;
import com.ftofs.twant.entity.GoodsInfo;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.HackyViewPager;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 縮放圖片，橫滑動切換下一張圖片
 * @author gzp
 */
public class ViewPagerFragment extends BaseFragment implements View.OnClickListener {
    public int start;
    private List<GoodsImage> currGalleryImageList;
    private SamplePagerAdapter viewPagerAdapter;
    private HackyViewPager viewPager;

    public static ViewPagerFragment newInstance(List<GoodsImage> currGalleryImageList) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        if (currGalleryImageList == null) {
            currGalleryImageList = new ArrayList<>();
        }
        fragment.currGalleryImageList = currGalleryImageList;
        return fragment;
    }

    public static ViewPagerFragment newInstance(List<String> imageList,boolean s) {
        List<GoodsImage> list = new ArrayList<>();
        for (String src : imageList) {
            GoodsImage goodsImage = new GoodsImage();
            goodsImage.setImageSrc(src);
            list.add(goodsImage);
        }
        return newInstance(list);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_view_pager,container,false);
        return view;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (start >=currGalleryImageList.size()) {
            start = 0;
        }
        viewPager.setCurrentItem(start);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.setOnClickListener(view,R.id.view_pager,this);
        Util.setOnClickListener(view,R.id.btn_back_round,this);
        viewPager = view.findViewById(R.id.view_pager);
        viewPagerAdapter = SamplePagerAdapter.newInstance(currGalleryImageList, getContext());

        viewPagerAdapter.setmOnItemClickListener(v ->
                pop());
        viewPager.setAdapter(viewPagerAdapter);

    }

    public void setStart(int start) {
        SLog.info("start%d",start);
        this.start = start;
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

    public void updateMap(Map<Integer, GoodsInfo> goodsInfoMap){

    // 更新圖片的顯示
        currGalleryImageList.clear();
        for (Map.Entry<Integer, GoodsInfo> entry : goodsInfoMap.entrySet()) {
            if (entry.getValue() != null) {
                currGalleryImageList.add(entry.getValue().toGoodsImage());
            }
        }
        SLog.info("imagelistSize %d",currGalleryImageList.size());
    }

    public void setStartPage(int goodsId) {
        int i=0 ,j= 0;
        for (GoodsImage goodsImage : currGalleryImageList) {
            if (goodsImage.getImageId() == goodsId) {
                j = i;
                    break;
            }
            i++;
        }
        start = j;
    }

    static class SamplePagerAdapter extends PagerAdapter {
        private List<GoodsImage> currGalleryImageList;
        private Context context;

        public void setmOnItemClickListener(View.OnClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        private View.OnClickListener mOnItemClickListener;

        public static SamplePagerAdapter newInstance(List<GoodsImage> currGalleryImageList, Context context) {
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
            String imagePath=currGalleryImageList.get(position).getImageSrc();
            SLog.info("imagepath %s",imagePath);
            if (imagePath.startsWith("/")) {
                imagePath = "file://" + imagePath;
                Glide.with(context).load(android.net.Uri.parse(imagePath)).into(photoView);
            } else {
                imagePath=StringUtil.normalizeImageUrl(currGalleryImageList.get(position).getImageSrc());

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
