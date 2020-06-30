package com.ftofs.twant.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 【賣家】商品數據結構
 */
public class SellerGoodsItem implements Parcelable {
    public int commonId;
    public String goodsName;
    public String imageName;
    public String goodsSpecNames;
    public double batchPrice0;
    public double appPriceMin;
    public int goodsStorage;
    public int goodsSaleNum;
    public int isVirtual;
    public int appUsable;
    public int goodsState;
    public int tariffEnable;
    public int isCommend;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(commonId);
        dest.writeString(goodsName);
        dest.writeString(imageName);
        dest.writeString(goodsSpecNames);
        dest.writeDouble(batchPrice0);
        dest.writeDouble(appPriceMin);
        dest.writeInt(isCommend);
        dest.writeInt(isVirtual);
        dest.writeInt(goodsState);
        dest.writeInt(tariffEnable);
    }
}
