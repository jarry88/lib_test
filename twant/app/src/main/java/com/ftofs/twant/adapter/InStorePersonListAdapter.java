package com.ftofs.twant.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.InStorePersonItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class InStorePersonListAdapter extends BaseMultiItemQuickAdapter<InStorePersonItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public InStorePersonListAdapter(List<InStorePersonItem> data) {
        super(data);

        addItemType(InStorePersonItem.TYPE_ITEM, R.layout.in_store_person_item);
        addItemType(InStorePersonItem.TYPE_LABEL, R.layout.in_store_item_label);
        addItemType(InStorePersonItem.TYPE_EMPTY_HINT, R.layout.in_store_item_empty_hint);
    }

    @Override
    protected void convert(BaseViewHolder helper, InStorePersonItem item) {
        int itemViewType = helper.getItemViewType();

        if (itemViewType == InStorePersonItem.TYPE_LABEL) {
            helper.setText(R.id.tv_category_label, item.nickname);
        } else if (itemViewType == InStorePersonItem.TYPE_ITEM) {
            ImageView imgAvatar = helper.getView(R.id.img_avatar);
            if (StringUtil.useDefaultAvatar(item.avatarUrl)) {
                Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(imgAvatar);
            } else {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatarUrl)).centerCrop().into(imgAvatar);
            }

            helper.setText(R.id.tv_nickname, item.nickname);
        } else if (itemViewType == InStorePersonItem.TYPE_EMPTY_HINT) {
            helper.setText(R.id.tv_hint, item.nickname);
        }
    }
}
