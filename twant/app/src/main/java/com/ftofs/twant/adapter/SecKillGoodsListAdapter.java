package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
    public void initView(@NotNull SecKillListNormalItemBinding binding, SecKillGoodsListItem item) {

    }

    @Override
    protected void convert(@NotNull BoundViewHolder<? extends SecKillListNormalItemBinding> helper, SecKillGoodsListItem item) {
        //這樣用也行
//        helper.setText(R.id.tv_goods_name, "sdf");
//        helper.getBinding().btnVersatile.setOnClickListener(null);
//        initView(helper.getBinding(),item);

        super.convert(helper,item);

        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc, "?x-oss-process=image/resize,w_200")).centerCrop().into(goodsImage);
        // item.goodsStorage = 223;
        // item.goodsSaleNum = 162;
        // 绘制销售进度
        float totalProgressBarWidth = context.getResources().getDimension(R.dimen.sec_kill_progress_bar_width);
        View currentProgress = helper.getView(R.id.current_progress);
        int currentProgressBarWidth = (int) (item.salesPercentage * totalProgressBarWidth / 100);
        ViewGroup.LayoutParams layoutParams = currentProgress.getLayoutParams();
        layoutParams.width = currentProgressBarWidth;
        currentProgress.setLayoutParams(layoutParams);


        SLog.info("item.secKillPrice[%s]", item.secKillPrice);
        helper.setText(R.id.tv_goods_name, item.goodsName)
                .setText(R.id.tv_remain_goods_count, String.format("剩餘%d件", item.goodsStorage))
                .setText(R.id.tv_sec_kill_price, StringUtil.formatPrice(context, item.secKillPrice, 0))
                .setText(R.id.tv_original_price, StringUtil.formatPrice(context, item.originalPrice, 0))
                .setText(R.id.tv_current_progress_desc, item.salesPercentage + "%");

        // 處理秒殺榜數據
        LinearLayout llSecKillRankList = helper.getView(R.id.ll_sec_kill_rank_list);
        int size = item.membersAvatars.size();
        if (size == 0) { // 如果秒殺榜為空，則隱藏
            llSecKillRankList.setVisibility(View.INVISIBLE);
        } else {
            llSecKillRankList.setVisibility(View.VISIBLE);

            ImageView imgAvatar1 = helper.getView(R.id.img_avatar_1);
            ImageView imgAvatar2 = helper.getView(R.id.img_avatar_2);
            ImageView imgAvatar3 = helper.getView(R.id.img_avatar_3);

            Glide.with(context).load(StringUtil.normalizeImageUrl(item.membersAvatars.get(0), "?x-oss-process=image/resize,w_64"))
                    .centerCrop().into(imgAvatar1);
            if (size > 1) {
                Glide.with(context).load(StringUtil.normalizeImageUrl(item.membersAvatars.get(1), "?x-oss-process=image/resize,w_64"))
                        .centerCrop().into(imgAvatar2);
                imgAvatar2.setVisibility(View.VISIBLE);
            } else {
                imgAvatar2.setVisibility(View.GONE);
            }

            if (size > 2) {
                Glide.with(context).load(StringUtil.normalizeImageUrl(item.membersAvatars.get(2), "?x-oss-process=image/resize,w_64"))
                        .centerCrop().into(imgAvatar3);
                imgAvatar3.setVisibility(View.VISIBLE);
            } else {
                imgAvatar3.setVisibility(View.GONE);
            }
        }
    }





    //          這樣用也行
//        helper.binding.tvSecKillPrice.setText("33333");
//        ImageView goodsImage = helper.getView(R.id.goods_image);
//        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc)).centerCrop().into(goodsImage);
//





}
