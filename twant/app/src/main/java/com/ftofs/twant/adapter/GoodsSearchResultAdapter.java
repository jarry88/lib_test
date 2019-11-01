package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 商品搜索結果Adapter
 * @author zwm
 */
public class GoodsSearchResultAdapter extends BaseMultiItemQuickAdapter<GoodsSearchItem, BaseViewHolder> {
    Context context;
    String currencyTypeSign;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GoodsSearchResultAdapter(Context context, List<GoodsSearchItem> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_DOUBLE_ELEVEN_BANNER, R.layout.double_eleven_header);
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.goods_search_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.publish_my_want);

        this.context = context;
        currencyTypeSign = context.getResources().getString(R.string.currency_type_sign);
    }


    @Override
    protected void convert(BaseViewHolder helper, GoodsSearchItem item) {
        int itemType = item.getItemType();
        if (itemType == Constant.ITEM_TYPE_NORMAL) {
            ImageView goodsImage = helper.getView(R.id.goods_image);
            Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc)).centerCrop().into(goodsImage);

            ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);
            if (StringUtil.isEmpty(item.storeAvatarUrl)) {
                Glide.with(context).load(R.drawable.grey_default_avatar).centerCrop().into(imgStoreAvatar);
            } else {
                Glide.with(context).load(StringUtil.normalizeImageUrl(item.storeAvatarUrl)).centerCrop().into(imgStoreAvatar);
            }


            if (!StringUtil.isEmpty(item.nationalFlag)) {
                ImageView imgGoodsNationalFlag = helper.getView(R.id.img_goods_national_flag);
                Glide.with(context).load(StringUtil.normalizeImageUrl(item.nationalFlag)).centerCrop().into(imgGoodsNationalFlag);
                imgGoodsNationalFlag.setVisibility(View.VISIBLE);
            }

            helper.setText(R.id.tv_store_name, item.storeName);
            helper.setText(R.id.tv_goods_name, item.goodsName);
            helper.setText(R.id.tv_goods_jingle, item.jingle);
            helper.setText(R.id.tv_goods_price, StringUtil.formatPrice(context, item.price, 1));

            helper.setGone(R.id.tv_freight_free, item.isFreightFree)
                    .setGone(R.id.tv_gift, item.hasGift)
                    .setGone(R.id.tv_discount, item.hasDiscount);

            helper.addOnClickListener(R.id.btn_goto_store);
        } else if (itemType == Constant.ITEM_TYPE_DOUBLE_ELEVEN_BANNER) {
            helper.addOnClickListener(R.id.btn_play_game)
                    .addOnClickListener(R.id.btn_back);
        } else {

        }
    }
}
