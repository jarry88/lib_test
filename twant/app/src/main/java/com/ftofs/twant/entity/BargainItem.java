package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

import java.util.ArrayList;
import java.util.List;

public class BargainItem implements MultiItemEntity {
    public int itemType;
    public String bannerUrl;

    public int commonId;
    public int goodsId;
    public int bargainId;
    public long startTime;
    public long endTime;
    public int bargainState;
    public String imageSrc;
    public String goodsName;
    public String jingle;
    public List<String> bargainOpenList = new ArrayList<>();
    public double bottomPrice;

    public BargainItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
