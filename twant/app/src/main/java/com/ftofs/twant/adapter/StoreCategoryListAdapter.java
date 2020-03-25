package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.store.StoreLabel;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

import java.util.List;

public class StoreCategoryListAdapter extends BaseQuickAdapter<StoreLabel, BaseViewHolder> {
    Context context;
    int twBlack;
    int twBlue;
    OnSelectedListener onSelectedListener;

    int prevSelectedItemIndex = 0;  // 上一次選中的菜單item的索引,一開始默認選中【全部】
    int prevSelectedSubItemIndex = -1;  // 上一次選中的二級菜單item的索引

    public StoreCategoryListAdapter(Context context, int layoutResId, @Nullable List<StoreLabel> data, OnSelectedListener onSelectedListener) {
        super(layoutResId, data);
        this.context = context;
        this.onSelectedListener = onSelectedListener;

        twBlack = context.getColor(R.color.tw_black);
        twBlue = context.getColor(R.color.tw_blue);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreLabel item) {
        List<StoreLabel> storeLabelList = item.getStoreLabelList();
        int subItemCount = storeLabelList.size(); // 二級分類的項數
        String labelName = item.getStoreLabelName();

        TextView tvCategoryName = helper.getView(R.id.tv_category_name);
        tvCategoryName.setText(labelName);

        LinearLayout llSubCategoryList = helper.getView(R.id.ll_sub_category_list);


        int goodsCount = item.getGoodsCount();
        if (goodsCount < 1) {
            // 如果對應分類下沒有商品時， 不顯示紅點
            helper.setGone(R.id.fl_goods_category_item_count_container, false);
        } else {
            String goodsCountText;
            if (goodsCount < 999) {
                goodsCountText = String.valueOf(goodsCount);
            } else {
                goodsCountText = "...";
            }
            helper.setText(R.id.tv_goods_category_item_count, goodsCountText);
            helper.setGone(R.id.fl_goods_category_item_count_container, true);
        }

        if (item.getIsFold() == Constant.TRUE_INT) {
            helper.itemView.setBackgroundColor(Color.parseColor("#F3F3F3"));
            helper.setGone(R.id.vw_selected_indicator, false);
            tvCategoryName.setTextColor(context.getResources().getColor(R.color.tw_black, null));
            tvCategoryName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            llSubCategoryList.setVisibility(View.GONE);
        } else {
            helper.itemView.setBackgroundColor(Color.WHITE);
            helper.setGone(R.id.vw_selected_indicator, true);
            tvCategoryName.setTextColor(context.getResources().getColor(R.color.tw_medium_grey, null));
            tvCategoryName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            if (subItemCount > 0) {
                llSubCategoryList.removeAllViews();
                for (int i = 0; i < subItemCount; i++) {
                    StoreLabel storeLabel = item.getStoreLabelList().get(i);
                    TextView tvSubCategory = new TextView(context);

                    SLog.info("storeLabel.getIsFold, i[%d], isFold[%d]", i, storeLabel.getIsFold());
                    if (storeLabel.getIsFold() == 0) {
                        tvSubCategory.setTextColor(twBlue);
                    } else {
                        tvSubCategory.setTextColor(twBlack);
                    }

                    int finalI = i;
                    tvSubCategory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SLog.info("onClick");

                            storeLabel.setIsFold(Constant.FALSE_INT);

                            SLog.info("prevSelectedSubItemIndex[%d], finalI[%d]", prevSelectedSubItemIndex, finalI);
                            // 上一次選中的二級菜單取消選中
                            if (prevSelectedSubItemIndex != -1) {
                                SLog.info("prevSelectedSubItemIndex[%d]", prevSelectedSubItemIndex);
                                StoreLabel prevSelectedSubItem = item.getStoreLabelList().get(prevSelectedSubItemIndex);
                                prevSelectedSubItem.setIsFold(Constant.TRUE_INT);
                            }

                            SLog.info("prevSelectedSubItemIndex[%d], finalI[%d]", prevSelectedSubItemIndex, finalI);
                            prevSelectedSubItemIndex = finalI;
                            onSelectedListener.onSelected(PopupType.DEFAULT, helper.getAdapterPosition(), storeLabel.getStoreLabelId());
                        }
                    });

                    tvSubCategory.setPadding(0, Util.dip2px(context, 12.5f), 0, Util.dip2px(context, 12.5f));
                    tvSubCategory.setText("·" + storeLabel.getStoreLabelName());
                    llSubCategoryList.addView(tvSubCategory);
                }
                llSubCategoryList.setVisibility(View.VISIBLE);
            } else {
                llSubCategoryList.setVisibility(View.GONE);
            }
        }


        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        } else {
            layoutParams.bottomMargin = 0;
        }
    }


    /**
     * 設置上一次選中的菜單item的索引
     * @param index
     */
    public void setPrevSelectedItemIndex(int index) {
        if (prevSelectedItemIndex != -1) {
            List<StoreLabel> dataList = getData();

            List<StoreLabel> subItemList = dataList.get(prevSelectedItemIndex).getStoreLabelList();
            for (StoreLabel storeLabel : subItemList) {
                storeLabel.setIsFold(Constant.TRUE_INT);
            }
        }
        prevSelectedItemIndex = index;
        prevSelectedSubItemIndex = -1;
    }

    /**
     * 獲取上一次選中的菜單item的索引
     * @return
     */
    public int getPrevSelectedItemIndex() {
        return prevSelectedItemIndex;
    }
}
