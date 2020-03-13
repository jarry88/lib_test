package com.ftofs.twant.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;

import java.util.List;


/**
 * 列表選擇彈出框Adapter
 * @author zwm
 */
public class ListPopupAdapter extends RecyclerView.Adapter<ListPopupAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlListsPopupItemWrapper;
        ImageView imgIcon;
        ImageView checkedImage;
        TextView tvText;

        public ViewHolder(View view) {
            super(view);
            rlListsPopupItemWrapper = view.findViewById(R.id.rl_list_popup_item_wrapper);
            imgIcon = view.findViewById(R.id.img_icon);
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
    int twBlack;

    boolean hasSeparator;
    boolean showUncheckedIndicator;  // 是否顯示未選中的提示圖標

    /**
     * Ctor
     * @param context
     * @param type 數據類型
     * @param itemList
     * @param index 初始選中哪個index
     * @param hasSeparator 是否顯示分隔線
     */
    public ListPopupAdapter(Context context, PopupType type,
                            OnSelectedListener onSelectedListener, List<ListPopupItem> itemList, int index, boolean hasSeparator, boolean showUncheckedIndicator) {
        this.context = context;
        this.type = type;
        this.onSelectedListener = onSelectedListener;
        this.itemList = itemList;
        this.index = index;
        this.hasSeparator = hasSeparator;
        this.showUncheckedIndicator = showUncheckedIndicator;

        highlightedTextColor = context.getColor(R.color.tw_blue);
        twBlack = context.getColor(R.color.tw_black);
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

        if (i == index) {  // 選中的高亮顯示
            viewHolder.tvText.setTextColor(highlightedTextColor);
            viewHolder.checkedImage.setVisibility(View.VISIBLE);
            viewHolder.checkedImage.setImageResource(R.drawable.icon_checked);

            if (item.selectedIconResId != 0) {
                viewHolder.imgIcon.setImageResource(item.selectedIconResId);
            }
            viewHolder.imgIcon.setVisibility(item.selectedIconResId != 0 ? View.VISIBLE : View.GONE);
        } else {
            viewHolder.tvText.setTextColor(twBlack);
            if (showUncheckedIndicator) {
                viewHolder.checkedImage.setVisibility(View.VISIBLE);
                viewHolder.checkedImage.setImageResource(R.drawable.icon_cart_item_unchecked);
            }

            if (item.unselectedIconResId != 0) {
                viewHolder.imgIcon.setImageResource(item.unselectedIconResId);
            }
            viewHolder.imgIcon.setVisibility(item.unselectedIconResId != 0 ? View.VISIBLE : View.GONE);
        }

        if (hasSeparator) {
            viewHolder.rlListsPopupItemWrapper.setBackgroundResource(R.drawable.border_type_d);
        } else {
            viewHolder.rlListsPopupItemWrapper.setBackground(null);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = i;
                onSelectedListener.onSelected(type, i, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
