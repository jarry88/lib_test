package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 商品搜索結果Adapter
 * @author zwm
 */
public class GoodsSearchResultAdapter extends BaseQuickAdapter<GoodsSearchItem, BaseViewHolder> {
    Context context;
    String currencyTypeSign;

    public GoodsSearchResultAdapter(Context context, int layoutResId, @Nullable List<GoodsSearchItem> data) {
        super(layoutResId, data);

        this.context = context;
        currencyTypeSign = context.getResources().getString(R.string.currency_type_sign);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsSearchItem item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc)).centerCrop().into(goodsImage);

        ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.storeAvatarUrl)).centerCrop().into(imgStoreAvatar);

        if (!StringUtil.isEmpty(item.nationalFlag)) {
            ImageView imgGoodsNationalFlag = helper.getView(R.id.img_goods_national_flag);
            Glide.with(context).load(StringUtil.normalizeImageUrl(item.nationalFlag)).centerCrop().into(imgGoodsNationalFlag);
            imgGoodsNationalFlag.setVisibility(View.VISIBLE);
        }

        helper.setText(R.id.tv_store_name, item.storeName);
        helper.setText(R.id.tv_goods_name, item.goodsName);
        helper.setText(R.id.tv_goods_jingle, item.jingle);
        helper.setText(R.id.tv_goods_price, StringUtil.formatPrice(context, item.price, 1));

        helper.addOnClickListener(R.id.btn_goto_store);
    }
}
