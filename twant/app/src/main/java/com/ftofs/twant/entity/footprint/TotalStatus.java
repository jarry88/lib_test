package com.ftofs.twant.entity.footprint;


import java.util.ArrayList;
import java.util.List;

/**
 * 【我的足跡】总的狀態模型
 * @author zwm
 */
public class TotalStatus extends BaseStatus {
    public List<DateStatus> dateStatusList = new ArrayList<>();

    @Override
    public void changeCheckStatus(boolean checked, int phrase) {
        super.changeCheckStatus(checked, phrase);

        if (phrase == PHRASE_TARGET) {
            for (DateStatus dateStatus : dateStatusList) {
                dateStatus.changeCheckStatus(checked, PHRASE_CAPTURE);
            }
        } else if (phrase == PHRASE_BUBBLE) {
            // 如果是冒泡階段，要所有子項目都選中了，才選中自己
            boolean allChecked = true;
            for (DateStatus dateStatus : dateStatusList) {
                if (!dateStatus.isChecked()) {
                    allChecked = false;
                }
            }

            super.changeCheckStatus(allChecked, PHRASE_BUBBLE);
        }
    }
}
