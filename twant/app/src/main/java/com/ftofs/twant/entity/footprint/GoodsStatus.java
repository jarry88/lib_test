package com.ftofs.twant.entity.footprint;


/**
 * 【瀏覽記憶】產品狀態模型
 * @author zwm
 */
public class GoodsStatus extends BaseStatus {
    public StoreStatus parent;
    public int footprintId;

    public GoodsStatus(int footprintId) {
        this.footprintId = footprintId;
    }

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
