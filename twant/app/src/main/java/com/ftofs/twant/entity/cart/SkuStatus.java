package com.ftofs.twant.entity.cart;



public class SkuStatus extends BaseStatus {
    public SpuStatus parent;

    @Override
    public void changeCheckStatus(boolean checked, int phrase) {
        super.changeCheckStatus(checked, phrase);

        if (phrase == PHRASE_CAPTURE) {

        } else if (phrase == PHRASE_TARGET) {
            // 狀態向上傳播
            parent.changeCheckStatus(checked, PHRASE_BUBBLE);
        }
    }
}
