package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

/**
 * 規格項
 * @author zwm
 */
public class SpecValue {
    public SpecValue(int specValueId, String specValueName, String imageSrc) {
        this.specValueId = specValueId;
        this.specValueName = specValueName;
        this.imageSrc = imageSrc;
    }


    public int specValueId;
    public String specValueName;
    public String imageSrc;

    @NonNull
    @Override
    public String toString() {
        return String.format("specValueId[%d], specValueName[%s], imageSrc[%s]",
                specValueId, specValueName, imageSrc);
    }
}
