package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

import java.util.List;

/**
 * 商鋪搜索结果项
 * @author zwm
 */
public class StoreSearchItem implements MultiItemEntity {
    public StoreSearchItem() {
        this.itemType = Constant.ITEM_TYPE_LOAD_END_HINT;
    }

    public StoreSearchItem(int storeId, String storeAvatarUrl, String storeName, String className, String mainBusiness, String storeFigureImage,
                           float distance, String shopDay, int likeCount, int goodsCommonCount, List<String> goodsImageList) {
        this.itemType = Constant.ITEM_TYPE_NORMAL;
        this.storeId = storeId;
        this.storeAvatarUrl = storeAvatarUrl;
        this.storeName = storeName;
        this.className = className;
        this.mainBusiness = mainBusiness;
        this.storeFigureImage = storeFigureImage;
        this.distance = distance;
        this.shopDay = shopDay;
        this.likeCount = likeCount;
        this.goodsCommonCount = goodsCommonCount;
        this.goodsImageList = goodsImageList;
    }

    public int itemType;
    public int storeId;
    public String storeAvatarUrl;
    public String storeName;
    public String className; // 店鋪分類名稱，比如：超級市場、文具店等
    public String mainBusiness;
    public String storeFigureImage;
    public float distance;
    public String shopDay;
    /**
     * 點讚數量
     */
    public int likeCount;
    public int goodsCommonCount;
    /**
     * 店鋪的前3個商品的照片
     */
    public List<String> goodsImageList;

    @Override
    public int getItemType() {
        return itemType;
    }
}



