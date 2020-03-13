package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreConform;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

public class StoreConformListAdapter extends ViewGroupAdapter<StoreConform> {

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public StoreConformListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
        addClickableChildrenId(R.id.btn_participate_activity);
    }

    @Override
    public void bindView(int position, View itemView, StoreConform itemData) {
        // String title = String.format(context.getString(R.string.text_conform_title_template), itemData.limitAmount, itemData.conformPrice);
        String title = "";
        String subTitle = "";
        /*
         if (model.conformPrice > 0) {
        // 立減
        if (model.limitAmount == 0) {
            [str appendFormat:@"$%d無門檻券,",model.conformPrice];
        }else{
            [str appendFormat:@"滿%d立減%d,",model.limitAmount,model.conformPrice];
        }
    }
    if(model.templateId.intValue > 0){
        // 送券
        if (str.length == 0) {
            [str appendFormat:@"滿%d",model.limitAmount];
        }
        [str appendFormat:@"送券,"];
    }
    if(model.giftVoList.count > 0){
        if (str.length == 0) {
            [str appendFormat:@"滿%d",model.limitAmount];
        }
        // 贈品
        [str appendFormat:@"贈品,"];
    }
    if (str.length == 0) {
        [str appendFormat:@"滿%d,",model.limitAmount];
    }
    // 包郵
    if (model.isFreeFreight) {
        [str appendFormat:@"包郵,"];
    }
         */

        if (itemData.conformPrice > 0) { // 立減
            if (itemData.limitAmount == 0) {
                title += String.format("$%d無門檻券,", itemData.conformPrice);
            } else {
                title += String.format("訂單滿%d", itemData.limitAmount);
            }

            subTitle += String.format("減$%d,", itemData.conformPrice);
        }
        if (itemData.templateId > 0) { // 送券
            if (title.length() == 0) {
                title += String.format("訂單滿%d", itemData.limitAmount);
            }
            subTitle += "送券,";
        }
        if (itemData.giftCount > 0) {
            if (title.length() == 0) {
                title += String.format("訂單滿%d", itemData.limitAmount);
            }
            subTitle += "贈品,";
            LinearLayout container =  itemView.findViewById(R.id.ll_container_giveaway);
            container.setVisibility(View.VISIBLE);

            for (int i = 0; i < itemData.giftCount; i++) {
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                ImageView imgGift = new ImageView(context);
                ViewGroup.LayoutParams imgParams=new ViewGroup.LayoutParams(50,30);
                imgGift.setLayoutParams(imgParams);

                Glide.with(context).load(itemData.getGiftList().get(i).imageSrc).centerCrop().into(imgGift);
                int finalI = i;
                imgGift.setOnClickListener(v -> {
                    Util.startFragment(GoodsDetailFragment.newInstance(itemData.getGiftList().get(finalI).commonId,0));
                });
                params.setMargins(5,5,2,2);
                container.addView(imgGift,params);
                TextView tvGiftCount = new TextView(context);
                tvGiftCount.setText(String.format("x%d",itemData.giftCount));
                params.setMarginEnd(8);
                tvGiftCount.setGravity(Gravity.CENTER_VERTICAL);
                container.addView(tvGiftCount,params);
            }
        }
        if (title.length() == 0) {
            title += String.format("訂單滿%d", itemData.limitAmount);
        }
        if (itemData.isFreeFreight == 1) {
            subTitle += "包郵";
        }

        if (subTitle.endsWith(",")) {
            subTitle = subTitle.substring(0, subTitle.length() - 1);
        }

        String content = itemData.contentCartRule;
        String validTime = context.getString(R.string.text_valid_time) + ": " + itemData.startTime.substring(0, 10) + "  -  " + itemData.endTime.substring(0, 10);

        setText(itemView, R.id.tv_conform_title, title);

        TextView tvSubTitle = itemView.findViewById(R.id.tv_conform_sub_title);
        if (StringUtil.isEmpty(subTitle)) {
            tvSubTitle.setVisibility(View.GONE);
        } else {
            tvSubTitle.setText("(" + subTitle + ")");
        }
        setText(itemView, R.id.tv_conform_content, content);
        setText(itemView, R.id.tv_conform_valid_time, validTime);
    }
}
