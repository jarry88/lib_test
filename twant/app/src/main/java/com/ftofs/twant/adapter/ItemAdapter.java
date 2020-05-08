package com.ftofs.twant.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Item;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.BaseViewHolder;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.lxj.xpopup.XPopup;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> mItemList;
    private static final int IS_HEADER = 0;
    private static final int IS_LINEAR = 1;
    private Context mContext;
    private Typeface typeFace;

    static class ViewHolder extends BaseViewHolder {

        public ViewHolder(View view) {
            super(view);
        }
    }
    static class LinkageSecondaryViewHolder extends BaseViewHolder {
        public LinkageSecondaryViewHolder (View view) {
            super(view);
        }
    }
    public ItemAdapter(List<Item> itemList) {
        mItemList = itemList;
        typeFace = Typeface.DEFAULT;
    }
    public ItemAdapter(List<Item> itemList,Typeface typeface) {
        mItemList = itemList;
        typeFace = typeface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == IS_HEADER) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_shopping_zone_secondary_linear, parent, false);
            return new LinkageSecondaryViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = mItemList.get(position);
        if (item.isCategoryTitle) {
            ViewHolder viewHolder=(ViewHolder) holder;
            TextView tvTitle = viewHolder.getView(R.id.tv_id);
            LinearLayout linearLayout = viewHolder.getView(R.id.ll_container);
            tvTitle.setText(item.goods.name);
            tvTitle.setTextSize(12);
            tvTitle.setTextColor(Color.parseColor("#999999"));
        } else {
            LinkageSecondaryViewHolder secondHolder = (LinkageSecondaryViewHolder) holder;
            if (item.goods == null) {
                return;
            }
            try {
                ((TextView) secondHolder.getView(R.id.tv_goods_name)).setText(item.goods.name);
                ((TextView) secondHolder.getView(R.id.tv_goods_comment)).setText(item.goods.jingle);
                TextView tvPrice=secondHolder.getView(R.id.tv_goods_price);
                tvPrice.setText(StringUtil.formatPrice(mContext, item.goods.price, 0, true));
                tvPrice.setTypeface(typeFace);
                UiUtil.toPriceUI(tvPrice,12);

                if (item.goods.showDiscount) {
                    TextView tvOriginalPrice=secondHolder.getView(R.id.tv_goods_original_price);
                    tvOriginalPrice.setVisibility(View.VISIBLE);
                    tvOriginalPrice.setText(StringUtil.formatPrice(mContext, item.goods.getOriginal(), 0, true));
                    tvOriginalPrice.setTypeface(typeFace);
                    // 原價顯示刪除線
                    tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                secondHolder.getView(R.id.sw_price).setVisibility( View.GONE);
//                ((SlantedWidget) holder.getView(R.id.sw_price)).setDiscountInfo(mContext, item.info.getDiscount(), item.info.getOriginal());
                ImageView imageView = secondHolder.getView(R.id.img_goods_item);
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.goods.imageUrl)).placeholder(R.drawable.goods_image_bg).centerCrop().into(imageView);
                secondHolder.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                    //TODO
                    Util.startFragment(GoodsDetailFragment.newInstance(item.goods.id, 0));
                });

                secondHolder.getView(R.id.iv_goods_add).setOnClickListener(v -> {
                    if (!item.goods.hasGoodsStorage()) {
                        ToastUtil.error(mContext,"該產品已售罄，看看其他的吧");
                        return;
                    }
                    if (item.goods.goodsStatus == 0) {
                        ToastUtil.error(mContext,"商品已下架");
                        return;

                    }
                    new XPopup.Builder(mContext)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new SpecSelectPopup(mContext, Constant.ACTION_ADD_TO_CART, item.goods.id , null, null, null, 1, null, null, 0, 2, null))
                            .show();
                    //TODO
                });
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemList.get(position).isCategoryTitle) {
            return IS_HEADER;
        } else {
            return IS_LINEAR;
        }
    }
}

