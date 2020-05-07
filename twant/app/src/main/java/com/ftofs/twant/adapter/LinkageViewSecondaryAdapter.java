package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SlantedWidget;

import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author gzp
 */
public class LinkageViewSecondaryAdapter extends BaseMultiItemQuickAdapter<Goods, BaseViewHolder> {
    Context context;
    private Typeface typeFace;

    public LinkageViewSecondaryAdapter(Context context, List<Goods> linkageItems) {
        super( linkageItems);
        this.context = context;
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.adapter_shopping_zone_secondary_linear);
        typeFace =Typeface.createFromAsset(context.getAssets(), "fonts/din_alternate_bold.ttf");
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        try {
            ((TextView) helper.getView(R.id.tv_goods_name)).setText(item.name);
            ((TextView) helper.getView(R.id.tv_goods_comment)).setText(item.jingle);
            TextView tvPrice=helper.getView(R.id.tv_goods_price);
            TextView tvOriginalPrice=helper.getView(R.id.tv_goods_original_price);
            tvPrice.setText(StringUtil.formatPrice(mContext, item.price, 0, true));
            if (item.showDiscount) {
                tvOriginalPrice.setVisibility(View.VISIBLE);
                tvOriginalPrice.setText(StringUtil.formatPrice(mContext, item.getOriginal(), 0, true));
                tvPrice.setTypeface(typeFace);
                tvOriginalPrice.setTypeface(typeFace);
                // 原價顯示刪除線
                tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            helper.getView(R.id.sw_price).setVisibility(item.showDiscount ? View.VISIBLE : View.GONE);
            ((SlantedWidget) helper.getView(R.id.sw_price)).setDiscountInfo(mContext, item.getDiscount(), item.getOriginal());
            ImageView imageView = helper.getView(R.id.iv_goods_img);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.imageUrl)).centerCrop().into(imageView);
            helper.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                //TODO
                Util.startFragment(GoodsDetailFragment.newInstance(item.id, 0));
            });

            helper.getView(R.id.iv_goods_add).setOnClickListener(v -> {
                //TODO
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
}
