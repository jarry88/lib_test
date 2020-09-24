package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 確認訂單Sku數據項
 * @author zwm
 */
public class ConfirmOrderSkuItem {
    public int storageStatus=1;//0时显示售罄
    public int allowSend =1;//0时显示 所選地區無貨
    public int joinBigSale;//0表示不参加活动 1表示参加
    public int goodsModel;
    public int tariffEnable;

    public ConfirmOrderSkuItem(String goodsImage, int goodsId, String goodsName, String goodsFullSpecs, int buyNum, double skuPrice, List<GiftItem> giftItemList) {
        this.goodsImage = goodsImage;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsFullSpecs = goodsFullSpecs;
        this.buyNum = buyNum;
        this.skuPrice = skuPrice;
        this.giftItemList = giftItemList;
    }


    public String goodsImage;
    public int cartId;
    public int goodsId;
    public String goodsName;
    public String goodsFullSpecs;
    public int buyNum;
    public double skuPrice;
    public List<GiftItem> giftItemList;

    @NonNull
    @Override
    public String toString() {
        return String.format("goodsId [%d],goodsName [%s]",goodsId,goodsName);
    }
}
