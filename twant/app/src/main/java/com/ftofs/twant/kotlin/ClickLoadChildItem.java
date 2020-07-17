package com.ftofs.twant.kotlin;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.item.TreeItem;
import com.ftofs.twant.R;

public class ClickLoadChildItem extends TreeItem<ZoneCategory> {
    public static int index;

    @Override
    public int getLayoutId() {
        return R.layout.layout_item;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_id, data.getCategoryName());
        viewHolder.setOnClickListener(R.id.tv_id, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "childclick");
                ((ClickLoadGroupItem) getParentItem()).mOnItemClickLister.onItemClick(null,index);
//                getParentItem().onClick(viewHolder);
            }
        });
    }
}

