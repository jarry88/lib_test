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
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;

import java.util.List;


/**
 * 店鋪商品Adapter
 * @author zwm
 */
public class ShopGoodsAdapter extends RecyclerView.Adapter<ShopGoodsAdapter.ViewHolder> {
    private Context context;
    private List<Goods> goodsList;
    String currencyTypeSign;
    OnSelectedListener onSelectedListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImage;
        TextView tvGoodsName;
        TextView tvJingle;
        TextView tvPrice;

        public ViewHolder(View view) {
            super(view);

            goodsImage = view.findViewById(R.id.img_goods);
            tvGoodsName = view.findViewById(R.id.tv_goods_name);
            tvJingle = view.findViewById(R.id.tv_goods_jingle);
            tvPrice = view.findViewById(R.id.tv_goods_price);
        }
    }

    public ShopGoodsAdapter(Context context, List<Goods> goodsList, OnSelectedListener onSelectedListener) {
        this.context = context;
        this.goodsList = goodsList;
        this.onSelectedListener = onSelectedListener;

        currencyTypeSign = context.getResources().getString(R.string.currency_type_sign);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_goods_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SLog.info("position[%d]", position);
        final Goods goods = goodsList.get(position);
        Glide.with(context).load(goods.imageUrl).into(holder.goodsImage);
        holder.tvGoodsName.setText(goods.name);
        holder.tvJingle.setText(goods.jingle);
        holder.tvGoodsName.setText(goods.name);
        holder.tvPrice.setText(currencyTypeSign + String.valueOf(goods.price));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedListener.onSelected(OnSelectedListener.TYPE_DEFAULT, goods.id, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (goodsList != null) {
            count = goodsList.size();
        }
        // SLog.info("count[%d]", count);
        return count;
    }
}
