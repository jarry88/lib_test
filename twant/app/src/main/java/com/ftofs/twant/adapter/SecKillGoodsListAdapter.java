package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.databinding.SecKillListNormalItemBinding;
import com.ftofs.twant.entity.SecKillGoodsListItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;
//如果用到layout binding的場景 ，BaseBindAdapter替換為BaseBindAdapter，BaseViewHolder 替換為ViewDataBinding
public class SecKillGoodsListAdapter extends BaseBindAdapter<SecKillGoodsListItem, ViewDataBinding> {
    Context context;


    public SecKillGoodsListAdapter(Context context, int layoutResId, @Nullable List<SecKillGoodsListItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

//    @NonNull
//    @Override
//    public SecKillListNormalItemBinding onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return DataBindingUtil.inflate(
//                LayoutInflater.from(parent.getContext()),
//                mLayoutResId,
//                parent,
//                false
//        );
//    }

    @Override
    protected void convert(@NonNull BaseBindViewHolder<ViewDataBinding> helper, SecKillGoodsListItem item) {
//          這樣用也行
        SecKillListNormalItemBinding secKillListNormalItemBinding = (SecKillListNormalItemBinding) helper.binding;
        secKillListNormalItemBinding.tvSecKillPrice.setText("sssss");

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
//                .setText(R.id.tv_original_price, StringUtil.formatPrice(context, item.originalPrice, 0))
                .setText(R.id.tv_current_progress_desc, progress + "%");
    }


}
