package com.ftofs.twant.domain.store;

import java.io.Serializable;

public class StoreLabelGoods implements Serializable {
    /**
     * 商品SPU编号
     */
    private int commonId;

    /**
     * 店铺标签编号
     */
    private int storeLabelId;

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getStoreLabelId() {
        return storeLabelId;
    }

    public void setStoreLabelId(int storeLabelId) {
        this.storeLabelId = storeLabelId;
    }

    @Override
    public String toString() {
        return "StoreLabelGoods{" +
                "commonId=" + commonId +
                ", storeLabelId=" + storeLabelId +
                '}';
    }
}
