package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreConform;

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
                title += String.format("滿%d立減%d,", itemData.limitAmount, itemData.conformPrice);
            }
        }
        if (itemData.templateId > 0) { // 送券
            if (title.length() == 0) {
                title += String.format("滿%d", itemData.limitAmount);
            }
            title += "送券,";
        }
        if (itemData.giftCount > 0) {
            if (title.length() == 0) {
                title += String.format("滿%d", itemData.limitAmount);
            }
            title += "贈品,";
        }
        if (title.length() == 0) {
            title += String.format("滿%d,", itemData.limitAmount);
        }
        if (itemData.isFreeFreight == 1) {
            title += "包郵";
        }

        if (title.endsWith(",")) {
            title = title.substring(0, title.length() - 1);
        }

        String content = String.format(context.getString(R.string.text_conform_content_template), itemData.limitAmount, itemData.conformPrice);
        String validTime = context.getString(R.string.text_valid_time) + ": " + itemData.startTime + "  -  " + itemData.endTime;

        setText(itemView, R.id.tv_conform_title, title);
        // setText(itemView, R.id.tv_conform_content, content);
        setText(itemView, R.id.tv_conform_valid_time, validTime);
    }
}
