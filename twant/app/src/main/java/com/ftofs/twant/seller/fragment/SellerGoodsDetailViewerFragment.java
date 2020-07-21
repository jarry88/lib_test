package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ftofs.twant.R;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.ImageFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.goods.GoodsMobileBodyVo;
import com.ftofs.twant.widget.DataImageView;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;


/**
 * 【賣家】商品詳情查看頁面
 * @author zwm
 */
public class SellerGoodsDetailViewerFragment extends BaseFragment implements View.OnClickListener {
    LinearLayout llGoodsDetailImageContainer;
    EasyJSONArray mobileBodyVoList;
    List<String> goodsDetailImageList = new ArrayList<>();

    public static SellerGoodsDetailViewerFragment newInstance(EasyJSONArray mobileBodyVoList) {
        Bundle args = new Bundle();

        SellerGoodsDetailViewerFragment fragment = new SellerGoodsDetailViewerFragment();
        fragment.setArguments(args);
        fragment.mobileBodyVoList = mobileBodyVoList;

        SLog.info("mobileBodyVoList[%s]", mobileBodyVoList);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_detail_viewer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        llGoodsDetailImageContainer = view.findViewById(R.id.ll_goods_detail_image_container);

        try {
            // 產品詳情圖片
            int imageIndex = 0;

            for (Object object : mobileBodyVoList) {
                EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                GoodsMobileBodyVo goodsMobileBodyVo = new GoodsMobileBodyVo();
                goodsMobileBodyVo.setType(easyJSONObject.getSafeString("type"));
                goodsMobileBodyVo.setValue(easyJSONObject.getSafeString("value"));
                goodsMobileBodyVo.setWidth(easyJSONObject.getInt("width"));
                goodsMobileBodyVo.setHeight(easyJSONObject.getInt("height"));

                if (goodsMobileBodyVo.getType().equals("image")) {
                    String imageUrl = StringUtil.normalizeImageUrl(easyJSONObject.getSafeString("value"));

                    DataImageView imageView = new DataImageView(_mActivity);
                    imageView.setCustomData(imageIndex);
                    imageView.setAdjustViewBounds(true);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SLog.info("imageUrl[%s],v[%s]", imageUrl, v instanceof DataImageView);
                            int currImageIndex = (int) ((DataImageView) v).getCustomData();
                            Util.startFragment(ImageFragment.newInstance(currImageIndex, goodsDetailImageList));
                        }
                    });
                    // 加上.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)，防止加載長圖模糊的問題
                    // 參考 Glide加载图片模糊问题   https://blog.csdn.net/sinat_26710701/article/details/89384579
                    Glide.with(llGoodsDetailImageContainer).load(imageUrl).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(imageView);
                    llGoodsDetailImageContainer.addView(imageView);

                    goodsDetailImageList.add(StringUtil.normalizeImageUrl(imageUrl));

                    imageIndex++;
                } else if (goodsMobileBodyVo.getType().equals("text")) {
                    TextView textView = new TextView(_mActivity);
                    textView.setText(goodsMobileBodyVo.getValue());
                    textView.setTextColor(getResources().getColor(R.color.tw_black, null));
                    textView.setTextSize(16);
                    textView.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = Util.dip2px(_mActivity, 20);
                    layoutParams.rightMargin = layoutParams.leftMargin;
                    layoutParams.topMargin = Util.dip2px(_mActivity, 10);
                    layoutParams.bottomMargin = layoutParams.topMargin;
                    textView.setLayoutParams(layoutParams);

                    llGoodsDetailImageContainer.addView(textView);
                }
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
