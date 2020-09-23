package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CrossBorderNavItem;
import com.ftofs.twant.entity.CrossBorderNavPane;
import com.ftofs.twant.util.StringUtil;
import com.gzp.lib_common.utils.SLog;

import java.util.List;

public class CrossBorderNavAdapter extends BaseQuickAdapter<CrossBorderNavPane, BaseViewHolder> {
    Context context;
    boolean showSecondRow;
    int[] itemIdArr = new int[] {R.id.nav_1, R.id.nav_2, R.id.nav_3, R.id.nav_4, R.id.nav_5,
            R.id.nav_6, R.id.nav_7, R.id.nav_8, R.id.nav_9, R.id.nav_10, };

    int[] itemIconIdArr = new int[] {R.id.nav_icon_1, R.id.nav_icon_2, R.id.nav_icon_3, R.id.nav_icon_4, R.id.nav_icon_5,
            R.id.nav_icon_6, R.id.nav_icon_7, R.id.nav_icon_8, R.id.nav_icon_9, R.id.nav_icon_10, };

    int[] itemTextIdArr = new int[] {R.id.nav_text_1, R.id.nav_text_2, R.id.nav_text_3, R.id.nav_text_4, R.id.nav_text_5,
            R.id.nav_text_6, R.id.nav_text_7, R.id.nav_text_8, R.id.nav_text_9, R.id.nav_text_10, };

    public CrossBorderNavAdapter(Context context, int layoutResId, int navItemCount, @Nullable List<CrossBorderNavPane> data) {
        super(layoutResId, data);

        this.context = context;
        this.showSecondRow = (navItemCount > 5);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CrossBorderNavPane item) {
        helper.addOnClickListener(itemIdArr);

        // 決定是否需要顯示第2行
        for (int i = 5; i < 10; i++) {
            helper.setVisible(itemIdArr[i], showSecondRow);
        }

        for (int i = 0; i < 10; i++) {
            if (i < item.crossBorderNavItemList.size()) {
                CrossBorderNavItem navItem = item.crossBorderNavItemList.get(i);

                ImageView imgIcon = helper.getView(itemIconIdArr[i]);
                Glide.with(context).load(StringUtil.normalizeImageUrl(navItem.icon)).centerCrop().into(imgIcon);
                helper.setText(itemTextIdArr[i], navItem.navName);

                helper.setVisible(itemIdArr[i], true);
            } else {
                helper.setVisible(itemIdArr[i], false);
            }
        }
    }
}
