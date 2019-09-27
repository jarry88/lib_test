package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.order.OrderDetailGoodsItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.widget.SquareGridLayout;

import java.util.List;

public class GoodsEvaluationAdapter extends BaseQuickAdapter<OrderDetailGoodsItem, BaseViewHolder> {
    int storeId;
    String storeName;

    public GoodsEvaluationAdapter(int layoutResId, int storeId, String storeName, @Nullable List<OrderDetailGoodsItem> data) {
        super(layoutResId, data);

        this.storeId = storeId;
        this.storeName = storeName;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailGoodsItem item) {
        helper.addOnClickListener(R.id.btn_add_image);

        int position = helper.getAdapterPosition();
        if (position == 0) { // 首項才顯示店鋪名
            helper.setGone(R.id.ll_store_name_container, true);
        } else {
            helper.setGone(R.id.ll_store_name_container, false);
        }

        helper.setText(R.id.tv_store_name, storeName);
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.imageSrc)).centerCrop().into(goodsImage);

        helper.setText(R.id.tv_goods_name, item.goodsName)
                .setText(R.id.tv_goods_full_specs, item.goodsFullSpecs);

        EditText etContent = helper.getView(R.id.et_content);
        etContent.setText(item.evaluationContent == null ? "" : item.evaluationContent);
        SquareGridLayout sglImageContainer = helper.getView(R.id.sgl_image_container);
        int childCount = sglImageContainer.getChildCount();
        if (childCount > 1) { // 刪除舊的圖片
            for (int i = 0; i < childCount - 1; i++) {
                sglImageContainer.removeViewAt(0);
            }
        }


        View btnAddImage = helper.getView(R.id.btn_add_image);
        if (item.evaluationImageList != null) {
            if (item.evaluationImageList.size() >= 3) { // 最多3張圖片，如果多于3張，隱藏添加圖片按鈕
                helper.setGone(R.id.btn_add_image, false);
                btnAddImage.setVisibility(View.GONE);
            } else {
                btnAddImage.setVisibility(View.VISIBLE);
            }

            int index = 0;
            for (String absolutePath : item.evaluationImageList) {
                View imageWidget = LayoutInflater.from(mContext).inflate(R.layout.refund_image_widget, sglImageContainer, false);
                ImageView imageView = imageWidget.findViewById(R.id.refund_image);
                View btnRemoveImage = imageWidget.findViewById(R.id.btn_remove_image);
                btnRemoveImage.setTag(index);
                btnRemoveImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = (int) v.getTag();
                        item.evaluationImageList.remove(index);
                        notifyItemChanged(position);
                    }
                });

                Glide.with(mContext).load(absolutePath).centerCrop().into(imageView);
                childCount = sglImageContainer.getChildCount();
                if (childCount > 0) {
                    sglImageContainer.addView(imageWidget, childCount - 1);
                }
                index++;
            }
        } else {
            btnAddImage.setVisibility(View.VISIBLE);
        }

        int itemCount = getItemCount();
        // 最后一項，設置大一點的bottomMargin
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == itemCount - 1) {
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        } else {
            layoutParams.bottomMargin = 0;
        }
    }
}
