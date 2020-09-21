package com.ftofs.twant.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CrossBorderHomeItem;

import java.util.List;

public class CrossBorderHomeAdapter extends BaseMultiItemQuickAdapter<CrossBorderHomeItem, BaseViewHolder> {
    Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CrossBorderHomeAdapter(Context context, List<CrossBorderHomeItem> data) {
        super(data);

        this.context = context;

        addItemType(Constant.ITEM_TYPE_HEADER, R.layout.cross_border_home_header);
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.cross_border_home_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CrossBorderHomeItem item) {
        int itemType = item.getItemType();

        if (itemType == Constant.ITEM_TYPE_HEADER) {
            helper.addOnClickListener(R.id.btn_receive_newbie_red_packet, R.id.btn_goto_shopping_zone,
                    R.id.btn_view_more_bargain, R.id.btn_view_more_group, R.id.btn_view_more_best_store);

            ImageView imgBanner = helper.getView(R.id.img_banner);
            Glide.with(context).load("https://spfs1.oss-cn-qingdao.aliyuncs.com/image/152335825006007200.jpg")
                    .centerCrop().into(imgBanner);

            ImageView imgShoppingZone = helper.getView(R.id.img_shopping_zone);

            Glide.with(context).load("https://spfs1.oss-cn-qingdao.aliyuncs.com/image/0159525979bab9a8012193a329c12d.jpg")
                    .centerCrop().into(imgShoppingZone);
        }
    }
}
