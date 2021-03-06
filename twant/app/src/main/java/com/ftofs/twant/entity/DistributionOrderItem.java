package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class DistributionOrderItem implements MultiItemEntity {
    /*
    marketingOrdersState	string	true	訂單狀態 0已取消 2進行中 3已完成 4退貨退款中 5退貨退款完成 6結算完成
    marketingOrdersId	number	true	推介訂單ID
    ordersSn	number	true	原訂單編號
    goodsName	string	true	商品名稱
    goodsImage	string	true	商品圖片
    createTime	string	true	訂單創建時間
    goodsPayAmount	string	true	商品實付金額
    commissionType	number	true	佣金類型 1比例 2固定佣金
    commissionLevel1	string	true	一級佣金比例
    commissionLevel2	string	true	二級佣金比例
    commissionAmount	string	true	一級佣金金額
    commissionAmount2	string	true	二級佣金金額
    predictCommission	string	true	預計佣金
    deep	string	true	層級(一級/二級分佣)
     */

    public int itemType;

    public DistributionOrderItem() {
    }

    public DistributionOrderItem(int itemType) {
        this.itemType = itemType;
    }

    public long ordersSn;
    public int marketingOrdersState;
    public String goodsName;
    public String goodsImage;
    public String createTime;
    public double goodsPayAmount;
    public int commissionType;
    public double commissionLevel1;
    public double commissionLevel2;
    public double commissionAmount;
    public double commissionAmount2;
    public double predictCommission;
    public int deep;

    @Override
    public int getItemType() {
        return itemType;
    }

    public String getMarketingOrdersStateDesc() {
        /*
        0已取消 2進行中 3已完成 4退貨退款中 5退貨退款完成 6結算完成
         */
        switch (marketingOrdersState) {
            case 0:
                return "已取消";
            case 2:
                return "進行中";
            case 3:
                return "已完成";
            case 4:
                return "退貨退款中";
            case 5:
                return "退貨退款完成";
            case 6:
                return "結算完成";
            default:
                return "";
        }
    }
}
