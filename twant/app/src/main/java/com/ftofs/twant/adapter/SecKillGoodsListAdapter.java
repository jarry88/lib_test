package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.databinding.SecKillListNormalItemBinding;
import com.ftofs.twant.entity.SecKillGoodsListItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;
//如果用到layout binding的場景 ，BaseBindAdapter替換為BaseBindAdapter，BaseViewHolder 替換為相應的binding文件
public class SecKillGoodsListAdapter extends BaseBindAdapter<SecKillGoodsListItem,SecKillListNormalItemBinding> {
    Context context;


    public SecKillGoodsListAdapter(Context context, int layoutResId, @Nullable List<SecKillGoodsListItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(@NotNull BoundViewHolder<? extends SecKillListNormalItemBinding> helper, SecKillGoodsListItem item) {
        //這樣用也行
//        helper.setText(R.id.tv_goods_name, "sdf");
//        helper.getBinding().btnVersatile.setOnClickListener(null);
//        initView(helper.getBinding(),item);



        // item.goodsStorage = 223;
        // item.goodsSaleNum = 162;
        // 绘制销售进度
        float totalProgressBarWidth = context.getResources().getDimension(R.dimen.sec_kill_progress_bar_width);
        View currentProgress = helper.getView(R.id.current_progress);
        int currentProgressBarWidth = (int) (item.goodsSaleNum * totalProgressBarWidth / item.goodsStorage);
        ViewGroup.LayoutParams layoutParams = currentProgress.getLayoutParams();
        layoutParams.width = currentProgressBarWidth;
        currentProgress.setLayoutParams(layoutParams);

        int progress = 0;
        if (item.goodsStorage > 0) { // 预防除0的异常
            progress = 100 * item.goodsSaleNum / item.goodsStorage;
        }

        helper.setText(R.id.tv_goods_name, item.goodsName)
                .setText(R.id.tv_remain_goods_count, String.format("剩餘%d件", item.goodsStorage - item.goodsSaleNum))
                .setText(R.id.tv_sec_kill_price, StringUtil.formatPrice(context, item.secKillPrice, 0))
//                .setText(R.id.tv_original_price, StringUtil.formatPrice(context, item.originalPrice, 0))
                .setText(R.id.tv_current_progress_desc, progress + "%");
    }

    @Override
    public void initView(@NotNull SecKillListNormalItemBinding binding, SecKillGoodsListItem item) {
        binding.tvSecKillPrice.setText("sss");
    }
    //          這樣用也行
//        helper.binding.tvSecKillPrice.setText("33333");
//        ImageView goodsImage = helper.getView(R.id.goods_image);
//        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc)).centerCrop().into(goodsImage);
//





}
