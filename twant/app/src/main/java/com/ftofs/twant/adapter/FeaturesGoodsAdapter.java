package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreGoodsItem;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.HwLoadingView;

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
        HwLoadingView loadingView;

        public ViewHolder(View view) {
            super(view);

            goodsImage = view.findViewById(R.id.goods_image);
            tvGoodsName = view.findViewById(R.id.tv_goods_name);
            tvGoodsJingle = view.findViewById(R.id.tv_goods_jingle);
            tvGoodsPrice = view.findViewById(R.id.tv_goods_price);
            loadingView = view.findViewById(R.id.loading_view);
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

        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.loadingView.setVisibility(View.GONE);  // 如果加載完成，隱藏loadingView
                return false;
            }
        }).centerCrop().into(holder.goodsImage);
        holder.tvGoodsName.setText(item.goodsName);
        holder.tvGoodsJingle.setText(item.jingle);
        holder.tvGoodsPrice.setText(StringUtil.formatPrice(context, item.price, 1,false));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.startFragment(GoodsDetailFragment.newInstance(item.commonId, 0));
            }
        });
    }

    @Override
    public int getItemCount() {
        // 用于無限循環
        return Constant.INFINITE_LOOP_VALUE;
    }
}
