package com.ftofs.lib_net.model;

import android.os.Parcel;
import android.os.Parcelable;


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
    public int goodsModal;
    public int goodsState;
    public int tariffEnable;
    public int isCommend;//是否為鎮店之寶 默認0 2-鎮店之寶
    public SellerGoodsItem(){}
    protected SellerGoodsItem(Parcel in) {
        commonId = in.readInt();
        goodsName = in.readString();
        imageName = in.readString();
        goodsSpecNames = in.readString();
        batchPrice0 = in.readDouble();
        appPriceMin = in.readDouble();
        goodsStorage = in.readInt();
        goodsSaleNum = in.readInt();
        isVirtual = in.readInt();
        appUsable = in.readInt();
        goodsModal = in.readInt();
        goodsState = in.readInt();
        tariffEnable = in.readInt();
        isCommend = in.readInt();
    }

    public static final Creator<SellerGoodsItem> CREATOR = new Creator<SellerGoodsItem>() {
        @Override
        public SellerGoodsItem createFromParcel(Parcel in) {
            return new SellerGoodsItem(in);
        }

        @Override
        public SellerGoodsItem[] newArray(int size) {
            return new SellerGoodsItem[size];
        }
    };

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
