package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.store.StoreLabel;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.kotlin.ZoneCategory;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ZoneCategoryListAdapter extends BaseQuickAdapter<ZoneCategory, BaseViewHolder> {
    Context context;
    int twBlack;
    int twBlue;
    OnSelectedListener onSelectedListener;

    int prevSelectedItemIndex = 0;  // 上一次選中的菜單item的索引,一開始默認選中【全部】
    int prevSelectedSubItemIndex = -1;  // 上一次選中的二級菜單item的索引
    private int selectedItemIndex =0; //當前選中的菜單item的索引
    private int selectedSubItemIndex =-1; //當前選中的二級菜單item的索引


    public ZoneCategoryListAdapter(Context context, int layoutResId, @Nullable List<ZoneCategory> data, OnSelectedListener onSelectedListener) {
        super(layoutResId, data);
        this.context = context;
//        this.mLayoutResId = R.layout.store_category_list_item;
        this.onSelectedListener = onSelectedListener;

        twBlack = context.getColor(R.color.tw_black);
        twBlue = context.getColor(R.color.tw_blue);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZoneCategory item) {
        List<ZoneCategory> storeLabelList = item.getNextList();
        if (storeLabelList == null) {
            storeLabelList = new ArrayList<>();
        }
        int subItemCount = storeLabelList.size(); // 二級分類的項數
        String labelName = item.getCategoryName();

        TextView tvCategoryName = helper.getView(R.id.tv_category_name);
        tvCategoryName.setTextSize(14);
        tvCategoryName.setText(labelName);
        ImageView imgFold = helper.getView(R.id.img_fold);
        LinearLayout llSubCategoryList = helper.getView(R.id.ll_sub_ategory_list);
        if (item.getNextList() == null || item.getNextList().size() == 0) {
            imgFold.setVisibility(View.INVISIBLE);
        }else {
            imgFold.setVisibility(View.VISIBLE);
        }
        if (item.getFold() == Constant.FALSE_INT) {//收起狀態
            helper.itemView.setBackgroundColor(Color.parseColor("#F3F3F3"));
            Glide.with(context).load(R.drawable.icon_down_triangle).centerCrop().into(imgFold);
            tvCategoryName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            llSubCategoryList.setVisibility(View.GONE);
        } else {
            helper.itemView.setBackgroundColor(Color.WHITE);
            tvCategoryName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            Glide.with(context).load(R.drawable.icon_up_triangle).centerCrop().into(imgFold);

//            tvCategoryName.setTextColor(context.getResources().getColor(R.color.tw_blue, null));
//            tvCategoryName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            if (subItemCount > 0) {
                llSubCategoryList.removeAllViews();
                for (int i = 0; i < subItemCount; i++) {
                    ZoneCategory storeLabel = item.getNextList().get(i);
                    TextView tvSubCategory = new TextView(context);

                    SLog.info("storeLabel.getIsFold, i[%d], isFold[%d]", i, storeLabel.getFold());
                    tvSubCategory.setMaxLines(2);
                    tvSubCategory.setLineSpacing(1,1.1f);
                    tvSubCategory.setTextSize(12);
                    tvSubCategory.setEllipsize(TextUtils.TruncateAt.END);
                    if (storeLabel.getFold() == Constant.TRUE_INT) {
                        tvSubCategory.setTextColor(twBlue);
                    } else {
                        tvSubCategory.setTextColor(twBlack);
                    }

                    int finalI = i;
                    tvSubCategory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SLog.info("onClick");
                           performNext(helper,item,finalI);
                        }
                    });

                    tvSubCategory.setPadding(0, Util.dip2px(context, 12.5f), 0, Util.dip2px(context, 12.5f));
                    tvSubCategory.setText(String.format("•%s", storeLabel.getCategoryName()));
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

    private void performNext(BaseViewHolder helper,ZoneCategory item,int finalI) {
        try {
            ZoneCategory storeLabel = item.getNextList().get(finalI);

            storeLabel.setFold(Constant.TRUE_INT);

            // 上一次選中的二級菜單取消選中
            if (prevSelectedSubItemIndex != -1) {

                SLog.info("prevSelectedSubItemIndex[%d], finalI[%d]", prevSelectedSubItemIndex, finalI);
                ZoneCategory prevSelectedSubItem = item.getNextList().get(prevSelectedSubItemIndex);
                prevSelectedSubItem.setFold(Constant.FALSE_INT);

//            SLog.info("prevSelectedSubItemIndex[%d], finalI[%d]", prevSelectedSubItemIndex, finalI);
//            prevSelectedSubItemIndex = finalI;
//            onSelectedListener.onSelected(PopupType.DEFAULT, helper.getAdapterPosition(), storeLabel);
            }

            SLog.info("prevSelectedSubItemIndex[%d], finalI[%d]", prevSelectedSubItemIndex, finalI);
            prevSelectedSubItemIndex = finalI;
            onSelectedListener.onSelected(PopupType.DEFAULT, helper.getAdapterPosition(), storeLabel);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));

        }

    }


    /**
     * 設置上一次選中的菜單item的索引
     * @param index
     * @deprecated 记录上次选中的item索引 并且清空subitem选中状态
     */
    public void setPrevSelectedItemIndex(int index) {
        if (prevSelectedItemIndex != -1) {
            List<ZoneCategory> dataList = getData();

            List<ZoneCategory> subItemList = dataList.get(prevSelectedItemIndex).getNextList();
            if (subItemList != null) {
                for (ZoneCategory storeLabel : subItemList) {
                    storeLabel.setFold(Constant.FALSE_INT);
                }
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
    } /**
     * 獲取上一次選中的菜單item的索引
     * @return
     */
    public int getPrevSelectedSubItemIndex() {
        return prevSelectedSubItemIndex;
    }
    public void setPrevSelectedSubItemIndex(int data) {
        prevSelectedSubItemIndex=data;
    }
    /**
     * 獲取當前選中的菜單item的索引
     * @return
     */
    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    /**
     * 檢查有下一subitem或者上一個
     *
     * @return
     */
    public boolean hasNextSubItem(boolean down) {
            List<ZoneCategory> dataList = getData();

            List<ZoneCategory> subItemList = dataList.get(prevSelectedItemIndex).getNextList();
            if (subItemList != null) {
                if (down) {
                    if (prevSelectedSubItemIndex == -1) {
                        return 0<subItemList.size() - 1;
                    }
                    return prevSelectedSubItemIndex < subItemList.size() - 1;
                } else {
                    SLog.info("prevSelectedSubItemIndex %d",prevSelectedSubItemIndex);
                    return prevSelectedSubItemIndex > 0;
                }
            }
        return false;
    }


    public int getSelectedItemCount() {
        return getData().get(prevSelectedItemIndex).getNextList().size();
    }
}

