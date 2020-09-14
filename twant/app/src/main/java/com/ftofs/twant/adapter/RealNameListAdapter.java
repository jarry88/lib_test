package com.ftofs.twant.adapter;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.RealNameListItem;

import java.util.List;

/**
 * 實名認證列表Adapter
 * @author zwm
 */
public class RealNameListAdapter extends BaseQuickAdapter<RealNameListItem, BaseViewHolder> {
    public RealNameListAdapter(int layoutResId, @Nullable List<RealNameListItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RealNameListItem item) {
        helper.addOnClickListener(R.id.btn_edit_real_name_info, R.id.btn_delete_real_name_info)
                .setText(R.id.tv_name, item.name)
                .setText(R.id.tv_id, item.idNum);

        View rlContainer = helper.getView(R.id.rl_container);

        // 如果是最後一項，隱藏分隔線
        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == itemCount - 1) {
//            SLog.info("position %d,itemcount-1");
            // 最后一項，不顯示分隔線
            rlContainer.setBackground(null);
        } else {
            rlContainer.setBackgroundResource(R.drawable.border_type_d);
        }
    }
}
