package com.ftofs.twant.entity;

import java.util.List;

public class GoodsConformItem {
    public int conformId;
    public String startTime;
    public String endTime;
    public int limitAmount;
    public int conformPrice;
    public int isFreeFreight;
    public int templateId;
    public int templatePrice;
    public List<GiftVo> giftVoList; // 【滿優惠】的贈品列表
}
