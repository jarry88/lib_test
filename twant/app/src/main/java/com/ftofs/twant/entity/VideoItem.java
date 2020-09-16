package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.lib_net.model.Goods;

import java.util.ArrayList;
import java.util.List;

public class VideoItem implements MultiItemEntity {
    public int itemType;
    public String videoId;
    public int playCount;
    public String updateTime;
    public List<Goods> goodsList; // 產品列表

    public VideoItem(int itemType) {
        this.itemType = itemType;
        goodsList = new ArrayList<>();
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
