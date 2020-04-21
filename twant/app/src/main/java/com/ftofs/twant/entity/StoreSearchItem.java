package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

import java.util.List;

/**
 * 商鋪搜索结果项
 * @author zwm
 */
public class StoreSearchItem implements MultiItemEntity {
    public int listCount;
    public CustomerServiceStaff staff =null;
    public int viewCount;
    public int followCount;
    public boolean showJobInfo;
    public String storeVideoUrl; // 店鋪形象視頻

    public StoreSearchItem() {
        this.itemType = Constant.ITEM_TYPE_LOAD_END_HINT;
    }
    public StoreSearchItem(int itemType) {
        this.itemType = itemType;
    }

    public StoreSearchItem(int storeId, int storeView, String storeAvatarUrl, String storeVideoUrl, String storeName, String className, String mainBusiness, String storeFigureImage,
                           float distance, String shopDay, int likeCount,int followCount,int viewCount, int goodsCommonCount, List<String> goodsImageList, List<JobInfoItem> jobList,CustomerServiceStaff staff) {
        this.itemType = Constant.ITEM_TYPE_NORMAL;
        this.storeId = storeId;
        this.storeView = storeView;
        this.storeAvatarUrl = storeAvatarUrl;
        this.storeVideoUrl = storeVideoUrl;
        this.storeName = storeName;
        this.className = className;
        this.mainBusiness = mainBusiness;
        this.storeFigureImage = storeFigureImage;
        this.distance = distance;
        this.shopDay = shopDay;
        this.likeCount = likeCount;
        this.goodsCommonCount = goodsCommonCount;
        this.goodsImageList = goodsImageList;
        this.jobList = jobList;
        this.followCount = followCount;
        this.viewCount = viewCount;
        this.staff = staff;
        if (jobList != null && jobList.size() > 0) {
            this.listCount = jobList.size();
        }else{
            this.likeCount = 0;
        }
    }

    public int itemType;
    public int storeId;
    public int storeView;  // 店鋪瀏覽量
    public String storeAvatarUrl;
    public String storeName;
    public String className; // 商店分類名稱，比如：超級市場、文具店等
    public String mainBusiness;
    public String storeFigureImage;
    public float distance;
    public String shopDay;
    public List<JobInfoItem> jobList;
    /**
     * 讚想數量
     */
    public int likeCount;
    public int goodsCommonCount;
    /**
     * 商店的前3個產品的照片
     */
    public List<String> goodsImageList;

    // 最后一項的動畫的顯示狀態
    public int animShowStatus;

    @Override
    public int getItemType() {
        return itemType;
    }
}



