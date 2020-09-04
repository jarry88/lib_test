package com.ftofs.twant.entity;

import java.util.ArrayList;
import java.util.List;

public class SecKillGoodsListItem {
    public int seckillCommonId;
    public int seckillGoodsId;
    public int commonId;
    public int goodsId;
    public String goodsName;
    public String imageSrc;
    public int scheduleState;
    public String scheduleStateText;
    public double secKillPrice;
    public double originalPrice;
    public int goodsSaleNum;
    public int goodsStorage;
    public int salesPercentage;
    public List<String> membersAvatars = new ArrayList<>();
}
