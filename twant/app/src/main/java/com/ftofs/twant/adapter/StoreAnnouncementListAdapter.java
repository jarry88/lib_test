package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreAnnouncement;

import java.util.List;

public class StoreAnnouncementListAdapter extends BaseQuickAdapter<StoreAnnouncement, BaseViewHolder> {
    public StoreAnnouncementListAdapter(int layoutResId, @Nullable List<StoreAnnouncement> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreAnnouncement item) {
        helper.setText(R.id.tv_announcement_title, item.title);
    }
}
