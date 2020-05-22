package com.ftofs.twant.seller.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.SellerGoodsItem;
import com.ftofs.twant.seller.fragment.SellerGoodsListFragment;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 【商家】商品列表
 * @author zwm
 */
public class SellerGoodsAdapter extends BaseQuickAdapter<SellerGoodsItem, BaseViewHolder> {
    Context context;
    int status;

    int twBlue;
    int twRed;
    public SellerGoodsAdapter(Context context, int status, int layoutResId, @Nullable List<SellerGoodsItem> data) {
        super(layoutResId, data);

        this.context = context;
        this.status = status;

        twBlue = context.getColor(R.color.tw_blue);
        twRed = context.getColor(R.color.tw_red);
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerGoodsItem item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageName)).centerCrop().into(goodsImage);
        String priceRange = String.format("%s MOP - %s MOP", StringUtil.formatFloat(item.appPriceMin), StringUtil.formatFloat(item.batchPrice0));

        helper.addOnClickListener(R.id.btn_view_all_sku, R.id.btn_more, R.id.ll_swipe_content, R.id.btn_switch_status);

        helper.setText(R.id.tv_goods_name, item.goodsName)
            .setText(R.id.tv_spu_id, "產品SPU: " + item.commonId)
            .setText(R.id.tv_virtual_indicator, item.isVirtual == Constant.TRUE_INT ? "虛擬" : "零售")
            .setGone(R.id.tv_cross_border_indicator, item.tariffEnable == Constant.TRUE_INT)
            .setText(R.id.tv_price_range, priceRange)
            .setText(R.id.tv_total_stock, "總庫存: " + item.goodsStorage)
            .setText(R.id.tv_total_sale, "銷量: " + item.goodsSaleNum)
            .setImageResource(R.id.icon_switch_status, status == SellerGoodsListFragment.TAB_GOODS_IN_SALE ? R.drawable.ic_file_download_red_24dp : R.drawable.ic_file_upload_blue_24dp)
            .setText(R.id.tv_switch_status_desc, status == SellerGoodsListFragment.TAB_GOODS_IN_SALE ? "下架" : "上架")
            .setTextColor(R.id.tv_switch_status_desc, status == SellerGoodsListFragment.TAB_GOODS_IN_SALE ? twRed : twBlue);
    }
}




