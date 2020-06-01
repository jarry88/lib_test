package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.seller.entity.SellerSpecItem;

import java.util.List;

/**
 * SKU圖片列表
 * @author zwm
 */
public class SellerSkuImageListFragment extends BaseFragment {
    LinearLayout llContainer;
    List<SellerSpecItem> sellerSpecItemList;

    RecyclerView[] recyclerViews;

    public static SellerSkuImageListFragment newInstance(List<SellerSpecItem> sellerSpecItemList) {
        Bundle args = new Bundle();

        SellerSkuImageListFragment fragment = new SellerSkuImageListFragment();
        fragment.setArguments(args);
        fragment.sellerSpecItemList = sellerSpecItemList;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_sku_image_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llContainer = view.findViewById(R.id.ll_container);
        recyclerViews = new RecyclerView[sellerSpecItemList.size()];
        for (int i = 0; i < sellerSpecItemList.size(); i++) {
            SellerSpecItem sellerSpecItem = sellerSpecItemList.get(i);

            View itemView = LayoutInflater.from(_mActivity)
                    .inflate(R.layout.seller_sku_image_list_item, llContainer, false);
            TextView tvSpecValue = itemView.findViewById(R.id.tv_spec_value);
            tvSpecValue.setText(sellerSpecItem.name);
            itemView.findViewById(R.id.btn_add_image).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageSelector.builder()
                            .useCamera(true) // 设置是否使用拍照
                            .setSingle(false)  //设置是否单选
                            .setMaxSelectCount(0) // 图片的最大选择数量，小于等于0时，不限数量。
                            .start(SellerSkuImageListFragment.this, RequestCode.SELECT_MULTI_IMAGE.ordinal()); // 打开相册
                }
            });
        }
    }
}
