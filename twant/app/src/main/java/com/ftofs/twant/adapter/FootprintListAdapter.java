package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Footprint;
import com.ftofs.twant.entity.footprint.BaseStatus;
import com.ftofs.twant.entity.footprint.DateStatus;
import com.ftofs.twant.entity.footprint.GoodsStatus;
import com.ftofs.twant.entity.footprint.StoreStatus;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.widget.ScaledButton;

import java.util.List;
import java.util.Map;

/**
 * 【瀏覽記憶】列表Adapter
 * @author zwm
 */
public class FootprintListAdapter extends BaseQuickAdapter<Footprint, BaseViewHolder> {
    Context context;
    Map<String, BaseStatus> positionTypeStatusMap;

    /**
     * 編輯模式還是查看模式
     */
    int mode = Constant.MODE_VIEW;
    public FootprintListAdapter(Context context, int layoutResId, @Nullable List<Footprint> data,
                                Map<String, BaseStatus> positionTypeStatusMap) {
        super(layoutResId, data);

        this.context = context;
        this.positionTypeStatusMap = positionTypeStatusMap;
    }

    @Override
    protected void convert(BaseViewHolder helper, Footprint item) {
        int position = helper.getAdapterPosition();
        // SLog.info("position[%d]", position);
        List<Footprint> footprintList = getData();
        if (position > 0) {
            // 查看前一項，日期或店鋪是否相同
            Footprint prevFootprint = footprintList.get(position - 1);

            boolean isDifferentDate = false;
            // 如果日期與前一項相同，隱藏日期
            if (prevFootprint.date.equals(item.date)) {
                helper.setGone(R.id.ll_date_container, false);
            } else {
                helper.setGone(R.id.ll_date_container, true);
                isDifferentDate = true;
            }

            // 如果與前一項不同一天或與前一項不同一家店鋪，則顯示店鋪名信息
            if (prevFootprint.storeId != item.storeId || isDifferentDate) {
                helper.setGone(R.id.ll_store_name_container, true);
            } else {
                helper.setGone(R.id.ll_store_name_container, false);
            }
        } else {
            // 如果是開始那一項，需要手動設置為VISIBLE，預防之前重用時設置為GONE
            helper.setGone(R.id.ll_date_container, true);
            helper.setGone(R.id.ll_store_name_container, true);
        }

        if (mode == Constant.MODE_EDIT) {
            // 檢查三層級別是否需要顯示
            String key = position + "|" + Footprint.SELECT_STATUS_DATE;
            DateStatus dateStatus = (DateStatus) positionTypeStatusMap.get(key);
            ScaledButton btnSelectDate = helper.getView(R.id.btn_select_date);
            if (dateStatus == null) {
                btnSelectDate.setVisibility(View.GONE);
            } else {
                dateStatus.setRadio(btnSelectDate);
                btnSelectDate.setVisibility(View.VISIBLE);

                if (dateStatus.isChecked()) {
                    btnSelectDate.setIconResource(R.drawable.icon_checked);
                } else {
                    btnSelectDate.setIconResource(R.drawable.icon_cart_item_unchecked);
                }
            }

            key = position + "|" + Footprint.SELECT_STATUS_STORE;
            StoreStatus storeStatus = (StoreStatus) positionTypeStatusMap.get(key);
            ScaledButton btnSelectStore = helper.getView(R.id.btn_select_store);
            if (storeStatus == null) {
                btnSelectStore.setVisibility(View.GONE);
            } else {
                storeStatus.setRadio(btnSelectStore);
                btnSelectStore.setVisibility(View.VISIBLE);

                if (storeStatus.isChecked()) {
                    btnSelectStore.setIconResource(R.drawable.icon_checked);
                } else {
                    btnSelectStore.setIconResource(R.drawable.icon_cart_item_unchecked);
                }
            }

            key = position + "|" + Footprint.SELECT_STATUS_GOODS;
            GoodsStatus goodsStatus = (GoodsStatus) positionTypeStatusMap.get(key);
            ScaledButton btnSelectGoods = helper.getView(R.id.btn_select_goods);
            if (goodsStatus == null) {
                btnSelectGoods.setVisibility(View.GONE);
            } else {
                goodsStatus.setRadio(btnSelectGoods);
                btnSelectGoods.setVisibility(View.VISIBLE);

                if (goodsStatus.isChecked()) {
                    btnSelectGoods.setIconResource(R.drawable.icon_checked);
                } else {
                    btnSelectGoods.setIconResource(R.drawable.icon_cart_item_unchecked);
                }
            }
        } else {
            // 查看模式,隱藏全部Radio Button
            helper.setGone(R.id.btn_select_date, false)
                    .setGone(R.id.btn_select_store, false)
                    .setGone(R.id.btn_select_goods, false);
        }

        helper.setText(R.id.tv_date, item.date)
                .setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_goods_name, item.goodsName)
                .setText(R.id.tv_goods_jingle, item.jingle)
                .setText(R.id.tv_goods_price, StringUtil.formatPrice(context, item.price, 1));

        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(item.imageSrc).centerCrop().into(goodsImage);

        helper.addOnClickListener(R.id.btn_select_date, R.id.btn_select_store, R.id.btn_select_goods,
                R.id.ll_store_name_container, R.id.ll_goods_container);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
