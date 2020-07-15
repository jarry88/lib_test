package com.ftofs.twant.entity;

public class MyBargainListItem {
    public int commonId;
    public int goodsId;
    public int bargainId;
    public int openId;
    public String imageSrc;
    public String goodsName;
    public String goodsFullSpecs;
    public String startTime;
    public String endTime;
    public double openPrice; // 當前價
    public double bargainPrice;  // 已砍掉的價錢
    public int bargainTimes; // 幫砍次數
    public double bottomPrice;
}
