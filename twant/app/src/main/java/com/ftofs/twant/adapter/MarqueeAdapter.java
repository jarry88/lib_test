package com.ftofs.twant.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.MarqueeItem;

import java.util.List;


public class MarqueeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<MarqueeItem> marqueeItemList;
    public int currPosition;

    public MarqueeAdapter(Context context, List<MarqueeItem> marqueeItemList) {
        this.context = context;
        this.marqueeItemList = marqueeItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Constant.ITEM_TYPE_MARQUEE_TEXT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marquee_layout_text, parent, false);
            return new TextViewHolder(view);
        } if (viewType == Constant.ITEM_TYPE_MARQUEE_SLOGAN) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marquee_layout_slogan, parent, false);
            return new SloganViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int realPosition = getRealPosition(position);
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).tvMarqueeText.setText(marqueeItemList.get(realPosition).text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int realPosition = getRealPosition(position);
        return marqueeItemList.get(realPosition).itemType;
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    private static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tvMarqueeText;

        public TextViewHolder(View view) {
            super(view);
            tvMarqueeText = view.findViewById(R.id.tv_marquee_text);
        }
    }

    private static class SloganViewHolder extends RecyclerView.ViewHolder {
        public SloganViewHolder(View view) {
            super(view);
        }
    }

    private int getRealPosition(int position) {
        if (marqueeItemList.size() > 0) {
            return position % marqueeItemList.size();
        }
        return 0;
    }
}

