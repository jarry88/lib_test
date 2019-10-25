package com.ftofs.twant.entity.footprint;


import java.util.ArrayList;
import java.util.List;


/**
 * 【瀏覽記憶】日期狀態模型
 * @author zwm
 */
public class DateStatus extends BaseStatus {
    public TotalStatus parent;
    public List<StoreStatus> storeStatusList = new ArrayList<>();

    @Override
    public void changeCheckStatus(boolean checked, int phrase) {
        super.changeCheckStatus(checked, phrase);

        if (phrase == PHRASE_CAPTURE) {
            for (StoreStatus storeStatus : storeStatusList) {
                storeStatus.changeCheckStatus(checked, PHRASE_CAPTURE);
            }
        } else if (phrase == PHRASE_TARGET) {
            // 狀態向下傳播
            for (StoreStatus storeStatus : storeStatusList) {
                storeStatus.changeCheckStatus(checked, PHRASE_CAPTURE);
            }

            // 狀態向上傳播
            parent.changeCheckStatus(checked, PHRASE_BUBBLE);
        } else if (phrase == PHRASE_BUBBLE) {
            // 如果是冒泡階段，要所有子項目都選中了，才選中自己
            boolean allChecked = true;
            for (StoreStatus storeStatus : storeStatusList) {
                if (!storeStatus.isChecked()) {
                    allChecked = false;
                }
            }

            super.changeCheckStatus(allChecked, PHRASE_BUBBLE);
            parent.changeCheckStatus(allChecked, PHRASE_BUBBLE);
        }
    }
}
