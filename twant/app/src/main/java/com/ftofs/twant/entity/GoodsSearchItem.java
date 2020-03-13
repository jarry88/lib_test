package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

/**
 * 產品搜索结果项
 * @author zwm
 */
public class GoodsSearchItem implements MultiItemEntity {
    public GoodsSearchItem(String imageSrc, String storeAvatarUrl, int storeId, String storeName, int commonId,
                           String goodsName, String jingle, float price, String nationalFlag) {
        this.itemType = Constant.ITEM_TYPE_NORMAL;
        this.imageSrc = imageSrc;
        this.storeAvatarUrl = storeAvatarUrl;
        this.storeId = storeId;
        this.storeName = storeName;
        this.commonId = commonId;
        this.goodsName = goodsName;
        this.jingle = jingle;
        this.price = price;
        this.nationalFlag = nationalFlag;
    }

    public GoodsSearchItem(int itemType) {
        this.itemType = itemType;
    }

    public int itemType;
    public boolean check;
    public String imageSrc;
    public String storeAvatarUrl;
    public int storeId;
    public String storeName;
    public int commonId;
    public String goodsName;
    public String jingle;
    public float price;
    public String nationalFlag;
    public boolean isFreightFree; // 是否包郵
    public boolean hasGift;       // 是否有贈品
    public boolean hasDiscount;   // 是否有折扣

    public float extendPrice0; // 折扣數
    public float batchPrice0;  // 原價
    public boolean showDiscountLabel;  // 是否顯示折扣標簽


    @Override
    public int getItemType() {
        return itemType;
    }
}
