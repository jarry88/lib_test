package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.log.SLog;

import java.util.List;


/**
 * 區號列表Adapter
 * @author zwm
 */
public class MobileZoneAdapter extends RecyclerView.Adapter<MobileZoneAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView checkedImage;
        TextView areaName;

        public ViewHolder(View view) {
            super(view);
            checkedImage = view.findViewById(R.id.checked_image);
            areaName = view.findViewById(R.id.area_name);
        }
    }

    Context context;
    List<MobileZone> mobileZoneList;
    int highlightedIndex;

    public MobileZoneAdapter(Context context, List<MobileZone> mobileZoneList, int highlightedIndex) {
        this.context = context;
        this.mobileZoneList = mobileZoneList;
        this.highlightedIndex = highlightedIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mobile_zone_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        MobileZone mobileZone = mobileZoneList.get(i);
        viewHolder.areaName.setText(mobileZone.areaName);
        // 高亮的顯示紅色
        if (i == highlightedIndex) {
            int textColor = context.getColor(R.color.tw_red);
            viewHolder.areaName.setTextColor(textColor);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLog.info("i[%d], highlightedIndex[%d]", i, highlightedIndex);
                if (i == highlightedIndex) {
                    return;
                }
                notifyItemChanged(highlightedIndex);
                highlightedIndex = i;
                notifyItemChanged(highlightedIndex);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mobileZoneList.size();
    }

    public int getHighlightedIndex() {
        return highlightedIndex;
    }
}
