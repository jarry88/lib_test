package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreAnnouncement;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.Util;

import java.util.List;

public class StoreAnnouncementListAdapter extends BaseQuickAdapter<StoreAnnouncement, BaseViewHolder> {
    public StoreAnnouncementListAdapter(int layoutResId, @Nullable List<StoreAnnouncement> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreAnnouncement item) {
        helper.setText(R.id.tv_announcement_title, item.title);
        helper.setText(R.id.tv_announcement_time, Time.fromMillisUnixtime(item.createTime, "Y-m-d H:i:s"));
    }
}
