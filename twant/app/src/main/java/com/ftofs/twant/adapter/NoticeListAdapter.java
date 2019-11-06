package com.ftofs.twant.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.NoticeItem;

import java.util.List;

public class NoticeListAdapter extends BaseMultiItemQuickAdapter<NoticeItem, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NoticeListAdapter(List<NoticeItem> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.notice_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeItem item) {
        if (item.getItemType() == Constant.ITEM_TYPE_NORMAL) {
            helper.setText(R.id.tv_msg_title, item.title)
                    .setText(R.id.tv_msg_time, item.createTime)
                    .setText(R.id.tv_msg_content, item.content);

            helper.setGone(R.id.icon_unread_count_indicator, !item.isRead);

            helper.addOnClickListener(R.id.btn_delete_message_item);

            ImageView imageView = helper.getView(R.id.img_msg_cover);
            if (item.tplCode.equals("memberReturnUpdate")) {
                Glide.with(mContext).load(R.drawable.icon_notice_return).centerCrop().into(imageView);
            } else if (item.tplCode.equals("storeOpen") || item.tplCode.equals("storeClose") ||
                    item.tplCode.equals("storeInfoUpdate") || item.tplCode.equals("storeGoodsCommonNew") || item.tplCode.equals("storeAnnouncement") ||
                    item.tplCode.equals("storeGoodsCommonUpdate")) {
                Glide.with(mContext).load(R.drawable.icon_notice_store).centerCrop().into(imageView);
            } else if (item.tplCode.equals("storeSalesPromotion") || item.tplCode.equals("memberDiscountCoupon")) {
                Glide.with(mContext).load(R.drawable.icon_notice_bargain).centerCrop().into(imageView);
            } else if (item.tplCode.equals("memberWantCommentLike") || item.tplCode.equals("memberStoreWantCommentReply") || item.tplCode.equals("memberGoodsWantCommentReply")) {
                Glide.with(mContext).load(R.drawable.icon_notice_interactive).centerCrop().into(imageView);
            } else if (item.tplCode.equals("memberWantPostLike") || item.tplCode.equals("memberFriendsApply") ||
                    item.tplCode.equals("memberFollowWantPost") || item.tplCode.equals("memberAgreeFriendsApply")) {
                Glide.with(mContext).load(R.drawable.icon_notice_friend).centerCrop().into(imageView);
            } else {
                Glide.with(mContext).load(item.imageUrl).centerCrop().into(imageView);
            }
        }
    }
}
