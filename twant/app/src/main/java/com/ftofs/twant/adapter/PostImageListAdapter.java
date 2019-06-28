package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;

import java.util.List;

public class PostImageListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PostImageListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView postImage = helper.getView(R.id.post_image);

        SLog.info("ITEM[%s]", item);
        Glide.with(mContext).load(item).centerCrop().into(postImage);
    }
}
