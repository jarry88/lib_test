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
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;

import java.util.List;


/**
 * 列表選擇彈出框Adapter
 * @author zwm
 */
public class ListPopupAdapter extends RecyclerView.Adapter<ListPopupAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView checkedImage;
        TextView tvText;

        public ViewHolder(View view) {
            super(view);
            checkedImage = view.findViewById(R.id.checked_image);
            tvText = view.findViewById(R.id.tv_text);
        }
    }

    Context context;
    PopupType type;

    OnSelectedListener onSelectedListener;
    List<ListPopupItem> itemList;
    int index; // 選中的index

    // 選中高亮文本的顏色
    int highlightedTextColor;

    /**
     * Ctor
     * @param context
     * @param type 數據類型
     * @param itemList
     * @param index 初始選中哪個index
     */
    public ListPopupAdapter(Context context, PopupType type,
                            OnSelectedListener onSelectedListener, List<ListPopupItem> itemList, int index) {
        this.context = context;
        this.type = type;
        this.onSelectedListener = onSelectedListener;
        this.itemList = itemList;
        this.index = index;

        highlightedTextColor = context.getColor(R.color.tw_red);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_popup_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        ListPopupItem item = itemList.get(i);
        viewHolder.tvText.setText(item.title);
        // 選中的高亮顯示
        if (i == index) {
            viewHolder.tvText.setTextColor(highlightedTextColor);
            viewHolder.checkedImage.setVisibility(View.VISIBLE);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedListener.onSelected(type, i, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
