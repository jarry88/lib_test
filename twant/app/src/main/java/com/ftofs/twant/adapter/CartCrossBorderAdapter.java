package com.ftofs.twant.adapter;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CartCrossBorderItem;

import java.util.List;

/**
 * 購物車跨境商品Adapter
 * @author zwm
 */
public class CartCrossBorderAdapter extends BaseQuickAdapter<CartCrossBorderItem, BaseViewHolder> {
    public int selectedPosition = -1;
    public CartCrossBorderAdapter(int layoutResId, @Nullable List<CartCrossBorderItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CartCrossBorderItem item) {
        if (item.selected) {
            helper.setImageResource(R.id.selected_indicator, R.drawable.icon_cart_item_checked);
        } else {
            helper.setImageResource(R.id.selected_indicator, R.drawable.icon_cart_item_unchecked);
        }

        if (item.isCrossBorder) {
            helper.setText(R.id.tv_store_name, item.storeName)
                    .setText(R.id.tv_product_count, "X " + item.productCount)
                    .setGone(R.id.cross_border_product_count_container, true)
                    .setGone(R.id.tv_other_product_count, false);
        } else {
            helper.setText(R.id.tv_store_name, "其他產品")
                    .setText(R.id.tv_other_product_count, "X " + item.productCount)
                    .setGone(R.id.cross_border_product_count_container, false)
                    .setGone(R.id.tv_other_product_count, true);
        }

        // 如果是最後一項，隱藏分隔線
        View itemContainer = helper.getView(R.id.item_container);
        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();

        if (position == itemCount - 1) {
//            SLog.info("position %d,itemcount-1");
            // 最后一項，不顯示分隔線
            itemContainer.setBackground(null);
        } else {
            itemContainer.setBackgroundResource(R.drawable.border_type_d);
        }
    }
}
