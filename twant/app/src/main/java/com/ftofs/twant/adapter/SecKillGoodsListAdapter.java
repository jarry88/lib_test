package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.SecKillGoodsListItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class SecKillGoodsListAdapter extends BaseQuickAdapter<SecKillGoodsListItem, BaseViewHolder> {
    Context context;


    public SecKillGoodsListAdapter(Context context, int layoutResId, @Nullable List<SecKillGoodsListItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SecKillGoodsListItem item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc)).centerCrop().into(goodsImage);

        item.goodsStorage = 223;
        item.goodsSaleNum = 162;

        // 绘制销售进度
        float totalProgressBarWidth = context.getResources().getDimension(R.dimen.sec_kill_progress_bar_width);
        View currentProgress = helper.getView(R.id.current_progress);
        int currentProgressBarWidth = (int) (item.goodsSaleNum * totalProgressBarWidth / item.goodsStorage);
        ViewGroup.LayoutParams layoutParams = currentProgress.getLayoutParams();
        layoutParams.width = currentProgressBarWidth;
        currentProgress.setLayoutParams(layoutParams);

        int progress = 100 * item.goodsSaleNum / item.goodsStorage;
        helper.setText(R.id.tv_goods_name, item.goodsName)
            .setText(R.id.tv_remain_goods_count, String.format("剩餘%d件", item.goodsStorage - item.goodsSaleNum))
            .setText(R.id.tv_sec_kill_price, StringUtil.formatPrice(context, item.secKillPrice, 0))
            .setText(R.id.tv_original_price, StringUtil.formatPrice(context, item.originalPrice, 0))
            .setText(R.id.tv_current_progress_desc, progress + "%");
    }
}
