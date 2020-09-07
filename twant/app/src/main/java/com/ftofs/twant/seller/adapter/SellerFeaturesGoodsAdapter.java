package com.ftofs.twant.seller.adapter;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.SellerGoodsItem;
import com.ftofs.twant.seller.fragment.SellerGoodsListFragment;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class SellerFeaturesGoodsAdapter extends BaseQuickAdapter<Goods,BaseViewHolder> {
    Context context;
    int status;

    int twBlue;
    int twRed;
    public SellerFeaturesGoodsAdapter(Context context, int status, int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);

        this.context = context;
        this.status = status;

        twBlue = context.getColor(R.color.tw_blue);
        twRed = context.getColor(R.color.tw_red);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageUrl)).centerCrop().into(goodsImage);
        String priceRange;
        if (item.goodsModal == Constant.GOODS_TYPE_CONSULT) {
            priceRange = "詢價";
        } else {
             priceRange = String.format("%s MOP - %s MOP", StringUtil.formatFloat(item.price), StringUtil.formatFloat(item.getOriginal()));

        }

        helper.addOnClickListener(R.id.btn_view_all_sku, R.id.btn_more, R.id.ll_swipe_content, R.id.btn_switch_status);

        helper.setText(R.id.tv_goods_name, item.name)
                .setText(R.id.tv_spu_id, "產品SPU: " + item.id)
                .setText(R.id.tv_virtual_indicator, item.goodsModal==5?"咨詢":item.isVirtual == Constant.TRUE_INT ? "虛擬" : "零售")
                .setGone(R.id.tv_cross_border_indicator, item.tariffEnable == Constant.TRUE_INT)
                .setText(R.id.tv_price_range, priceRange)
                .setText(R.id.tv_total_stock, "總庫存: " + item.getGoodsStorage())
//                .setText(R.id.tv_total_sale, "銷量: " + item.goodsSaleNum)
                .setImageResource(R.id.icon_switch_status, status == SellerGoodsListFragment.TAB_GOODS_IN_SALE ? R.drawable.ic_file_download_red_24dp : R.drawable.ic_file_upload_blue_24dp)
                .setText(R.id.tv_switch_status_desc, status == SellerGoodsListFragment.TAB_GOODS_IN_SALE ? "下架" : "上架")
                .setTextColor(R.id.tv_switch_status_desc, status == SellerGoodsListFragment.TAB_GOODS_IN_SALE ? twRed : twBlue);
    }

}
