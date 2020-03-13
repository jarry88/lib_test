package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class GoodsGalleryAdapter extends RecyclerView.Adapter<GoodsGalleryAdapter.ViewHolder> {
    Context context;
    List<String> itemList;
    private View.OnClickListener onItemClickListener;
    //'注入接口'
    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItem = itemView.findViewById(R.id.img_item);
        }
    }

    public GoodsGalleryAdapter(Context context, List<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goods_gallery_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (itemList.size() > 0) {
            position = position % itemList.size();

            String url = itemList.get(position);
            if ("placeholder".equals(url)) {
                // 如果沒有圖片，加一張默認的空櫥窗占位圖
                Glide.with(context).load(R.drawable.store_figure_default).centerCrop().into(viewHolder.imgItem);
            } else {
                Glide.with(context).load(StringUtil.normalizeImageUrl(url)).centerCrop().into(viewHolder.imgItem);
            }
            // item click
            viewHolder.itemView.setOnClickListener(onItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        if (itemList.size() > 1) {
            // 用于無限循環
            return Integer.MAX_VALUE;
        } else {
            return 1;
        }
    }

    public void setNewData(List<String> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }
}
