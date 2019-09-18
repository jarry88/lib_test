package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreGoodsItem;
import com.ftofs.twant.util.StringUtil;


import java.util.List;

/**
 * 鎮店之寶Adapter
 * @author zwm
 */
public class FeaturesGoodsAdapter  extends RecyclerView.Adapter<FeaturesGoodsAdapter.ViewHolder> {
    Context context;
    private List<StoreGoodsItem> storeGoodsItemList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImage;
        TextView tvGoodsName;
        TextView tvGoodsJingle;
        TextView tvGoodsPrice;

        public ViewHolder(View view) {
            super(view);

            goodsImage = view.findViewById(R.id.goods_image);
            tvGoodsName = view.findViewById(R.id.tv_goods_name);
            tvGoodsJingle = view.findViewById(R.id.tv_goods_jingle);
            tvGoodsPrice = view.findViewById(R.id.tv_goods_price);
        }
    }

    public FeaturesGoodsAdapter(Context context, List<StoreGoodsItem> storeGoodsItemList) {
        this.context = context;
        this.storeGoodsItemList = storeGoodsItemList;
    }

    @Override
    public FeaturesGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.features_goods_item, parent, false);
        FeaturesGoodsAdapter.ViewHolder holder = new FeaturesGoodsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeaturesGoodsAdapter.ViewHolder holder, int position) {
        position = position % storeGoodsItemList.size();
        StoreGoodsItem item = storeGoodsItemList.get(position);

        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc)).centerCrop().into(holder.goodsImage);
        holder.tvGoodsName.setText(item.goodsName);
        holder.tvGoodsJingle.setText(item.jingle);
        holder.tvGoodsPrice.setText(StringUtil.formatPrice(context, item.price, 1));
    }

    @Override
    public int getItemCount() {
        // 用于無限循環
        return Integer.MAX_VALUE;
    }
}
