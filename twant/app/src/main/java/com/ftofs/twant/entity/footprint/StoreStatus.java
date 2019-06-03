package com.ftofs.twant.entity.footprint;



import java.util.ArrayList;
import java.util.List;

/**
 * 【我的足跡】店鋪狀態模型
 * @author zwm
 */
public class StoreStatus extends BaseStatus {
    public DateStatus parent;
    public List<GoodsStatus> goodsStatusList = new ArrayList<>();


    @Override
    public void changeCheckStatus(boolean checked, int phrase) {
        super.changeCheckStatus(checked, phrase);

        if (phrase == PHRASE_CAPTURE) {
            for (GoodsStatus goodsStatus : goodsStatusList) {
                goodsStatus.changeCheckStatus(checked, PHRASE_CAPTURE);
            }
        } else if (phrase == PHRASE_TARGET) {
            // 狀態向下傳播
            for (GoodsStatus goodsStatus : goodsStatusList) {
                goodsStatus.changeCheckStatus(checked, PHRASE_CAPTURE);
            }

            // 狀態向上傳播
            parent.changeCheckStatus(checked, PHRASE_BUBBLE);
        } else if (phrase == PHRASE_BUBBLE) {
            // 如果是冒泡階段，要所有子項目都選中了，才選中自己
            boolean allChecked = true;
            for (GoodsStatus goodsStatus : goodsStatusList) {
                if (!goodsStatus.isChecked()) {
                    allChecked = false;
                }
            }

            super.changeCheckStatus(allChecked, PHRASE_BUBBLE);
            parent.changeCheckStatus(allChecked, PHRASE_BUBBLE);
        }
    }
}
