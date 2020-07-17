package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.BargainItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class BargainListAdapter extends BaseMultiItemQuickAdapter<BargainItem, BaseViewHolder> {
    Context context;

    public static final int MAX_AVATAR_COUNT = 3;

    // 三个头像控件的Id
    int[] avatarIdArr = new int[] {R.id.img_avatar_1, R.id.img_avatar_2, R.id.img_avatar_3};

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BargainListAdapter(Context context, List<BargainItem> data) {
        super(data);

        this.context = context;

        addItemType(Constant.ITEM_TYPE_BANNER, R.layout.bargain_list_banner_item);
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.bargain_list_normal_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, BargainItem item) {
        int itemType = item.itemType;
        if (itemType == Constant.ITEM_TYPE_BANNER) {  // 頂部的Banner
            ImageView imgBanner = helper.getView(R.id.img_banner);
            Glide.with(context).load(item.bannerUrl).centerCrop().into(imgBanner);
        } else if (itemType == Constant.ITEM_TYPE_NORMAL) {
            ImageView goodsImage = helper.getView(R.id.goods_image);
            Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc)).centerCrop().into(goodsImage);

            helper.setText(R.id.tv_goods_name, item.goodsName)
                    .setText(R.id.tv_goods_jingle, item.jingle);
            // 砍價榜
            if (item.bargainOpenList.size() < 1) {
                // 如果砍價榜沒內容，則不顯示
                helper.setVisible(R.id.ll_bargain_rank_list, false);
            } else {
                helper.setVisible(R.id.ll_bargain_rank_list, true);


                int count = item.bargainOpenList.size();
                // 显示前3个
                if (count > MAX_AVATAR_COUNT) {
                    count = MAX_AVATAR_COUNT;
                }

                int i = 0;
                for (; i < count; i++) {
                    String avatarUrl = item.bargainOpenList.get(i);
                    ImageView imgAvatar = helper.getView(avatarIdArr[i]);
                    Glide.with(context).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAvatar);
                    imgAvatar.setVisibility(View.VISIBLE);
                }

                // 如果不足3個，隱藏後面的ImageView
                for (; i < MAX_AVATAR_COUNT; i++) {
                    ImageView imgAvatar = helper.getView(avatarIdArr[i]);
                    imgAvatar.setVisibility(View.GONE);
                }
            }

            helper.setText(R.id.tv_bottom_price, StringUtil.formatPrice(context, item.bottomPrice, 0));
        }
    }
}
