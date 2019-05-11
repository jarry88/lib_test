package com.ftofs.twant.entity;

import android.support.annotation.NonNull;

/**
 * 規格項
 * @author zwm
 */
public class SpecValue {
    public SpecValue(int specValueId, int goodsId, String specValueName, String imageSrc) {
        this.specValueId = specValueId;
        this.goodsId = goodsId;
        this.specValueName = specValueName;
        this.imageSrc = imageSrc;
    }


    public int specValueId;
    // sku id
    public int goodsId;
    public String specValueName;
    public String imageSrc;

    @NonNull
    @Override
    public String toString() {
        return String.format("specValueId[%d], goodsId[%d], specValueName[%s], imageSrc[%s]",
                specValueId, goodsId, specValueName, imageSrc);
    }
}
