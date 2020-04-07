package com.ftofs.twant.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.util.StringUtil;

import org.litepal.util.Const;

import java.util.List;

public class ShopRelativePostAdapter extends BaseMultiItemQuickAdapter<PostItem, BaseViewHolder> {
    public ShopRelativePostAdapter( @Nullable List<PostItem> data) {
        super( data);
        addItemType(Constant.ITEM_TYPE_NORMAL,R.layout.shop_relative_post_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT,R.layout.load_end_hint);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem item) {
        if (item.itemType == Constant.ITEM_TYPE_NORMAL) {
            if (!StringUtil.isEmpty(item.goodsimage)) {
                ImageView imgPostGood = helper.getView(R.id.iv_goods_img);
                imgPostGood.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.goodsimage)).centerCrop().into(imgPostGood);
            }
            ImageView imgAuthorAvatar = helper.getView(R.id.img_author_avatar);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.authorAvatar)).centerCrop().into(imgAuthorAvatar);

            helper.setText(R.id.tv_title, item.title)
                    .setText(R.id.tv_author_nickname, item.authorNickname+"·"+item.createTime)
                    .setText(R.id.tv_follow_count, String.valueOf(item.postFollow))
                    .setText(R.id.tv_like_count,String.valueOf(item.postLike))
                    .setText(R.id.tv_look_count,String.valueOf(item.postView))
                    .setText(R.id.tv_thumb_count,String.valueOf(item.postThumb));
            if (item.comeTrueState == 1) {
                helper.getView(R.id.img_true_state).setVisibility(View.VISIBLE);
            }
            if (item.isDelete == 1) {
                helper.getView(R.id.tv_unuseful).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_follow_count).setVisibility(View.GONE);
                helper.getView(R.id.tv_look_count).setVisibility(View.GONE);
                helper.getView(R.id.tv_like_count).setVisibility(View.GONE);
                helper.getView(R.id.icon_like).setVisibility(View.GONE);
                helper.getView(R.id.icon_follow).setVisibility(View.GONE);
                helper.getView(R.id.icon_view).setVisibility(View.GONE);
            }
        }




    }
}
