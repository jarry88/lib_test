package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreFriendsItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;


/**
 * 城友列表Adapter
 * @author zwm
 */
public class StoreFriendsAdapter extends BaseQuickAdapter<StoreFriendsItem, BaseViewHolder> {

    public StoreFriendsAdapter(int layoutResId, @Nullable List<StoreFriendsItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreFriendsItem item) {
        ImageView imageView = helper.getView(R.id.img_avatar);

        if (StringUtil.useDefaultAvatar(item.avatar)) {
            Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(imageView);
        } else {
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatar)).centerCrop().into(imageView);
        }
    }
}
