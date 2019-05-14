package com.ftofs.twant.entity.cart;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class SpuStatus extends BaseStatus {
    public StoreStatus parent;
    public List<SkuStatus> skuStatusList = new ArrayList<>();

    @Override
    public void changeCheckStatus(boolean checked, int phrase) {
        super.changeCheckStatus(checked, phrase);

        if (phrase == PHRASE_CAPTURE) {
            for (SkuStatus skuStatus : skuStatusList) {
                skuStatus.changeCheckStatus(checked, PHRASE_CAPTURE);
            }
        } else if (phrase == PHRASE_TARGET) {
            // 狀態向下傳播
            for (SkuStatus skuStatus : skuStatusList) {
                skuStatus.changeCheckStatus(checked, PHRASE_CAPTURE);
            }

            // 狀態向上傳播
            parent.changeCheckStatus(checked, PHRASE_BUBBLE);
        } else if (phrase == PHRASE_BUBBLE) {
            // 如果是冒泡階段，要所有子項目都選中了，才選中自己
            boolean allChecked = true;
            for (SkuStatus skuStatus : skuStatusList) {
                if (!skuStatus.isChecked()) {
                    allChecked = false;
                }
            }

            super.changeCheckStatus(allChecked, PHRASE_BUBBLE);
            parent.changeCheckStatus(allChecked, PHRASE_BUBBLE);
        }
    }
}
