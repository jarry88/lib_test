package com.ftofs.twant.seller.entity;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * @author gzp
 */
public class SellerOrderRefundItem {
    /**
     * refundSn : 416145966902114
     * refundSnText : 416145966902114
     * ordersId : 6238
     * ordersSn : 5820000000631600
     * ordersSnText : 5820000000631600
     * memberName : u_001315344758
     * nickName : 鎯宠鍩庣悊鎯�
     * refundType : 1
     * returnType : 1
     * goodsState : 1
     * addTime : 2020-04-16 14:59:06
     * refundMemberCancel : 0
     * returnMemberAutoCancel : 0
     * goodsCount : 1
     * sellerState : 2
     * adminState : 1
     * refundState : 2
     * showSellerHandle : 0
     */

    private long refundSn;
    private String refundSnText;
    private int ordersId;
    private long ordersSn;
    private String ordersSnText;
    private String memberName;
    private String nickName;
    private int refundType;
    private int returnType;
    private int goodsState;
    private String addTime;
    private int refundMemberCancel;
    private int returnMemberAutoCancel;
    private int goodsCount;
    private int sellerState;
    private int adminState;
    private int refundState;
    private int showSellerHandle;//顯示商家處理按鈕 0查看 1處理 2收貨
    public String sellerStateText="-";
    public String adminStateText="-";
    public String refundStateText="-";
    public String showSellerHandleText="-";
    private double refundAmount;
    private int refundId;

    public static SellerOrderRefundItem parse(EasyJSONObject jsonObject) throws Exception {
        SellerOrderRefundItem item = new SellerOrderRefundItem();
        item.setAddTime(jsonObject.getSafeString("addTime"));
        item.setRefundSn(jsonObject.getLong("refundSn"));
        item.setRefundSnText(jsonObject.getSafeString("refundSnText"));
//        item.setRefundSnText(jsonObject.getSafeString("refundSnStr"));
        item.setOrdersSn(jsonObject.getLong("ordersSn"));
        item.setOrdersSnText(jsonObject.getSafeString("ordersSnText"));
//        item.setOrdersSnText(jsonObject.getSafeString("ordersSnStr"));
        item.setReturnType(jsonObject.getInt("returnType"));
        item.setRefundType(jsonObject.getInt("refundType"));
        item.setOrdersId(jsonObject.getInt("ordersId"));
        item.setGoodsState(jsonObject.getInt("goodsState"));
        item.setGoodsCount(jsonObject.getInt("goodsCount"));
        item.setSellerState(jsonObject.getInt("sellerState"));
        item.setAdminState(jsonObject.getInt("adminState"));
        item.setRefundState(jsonObject.getInt("refundState"));
        item.setRefundMemberCancel(jsonObject.getInt("refundMemberCancel"));
        item.setReturnMemberAutoCancel(jsonObject.getInt("returnMemberAutoCancel"));
        item.setMemberName(jsonObject.getSafeString("memberName"));
        item.setNickName(jsonObject.getSafeString("nickName"));
        if (jsonObject.exists("refundAmount")) {
            item.setRefundAmount(jsonObject.getDouble("refundAmount"));
        }
        if (jsonObject.exists("refundId")) {
            item.refundId = jsonObject.getInt("refundId");
        }

        item.stateToText();
        return item;
    }

    private void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getRefundId() {
        return refundId;
    }
    private void stateToText() {
        if (sellerState == 1) {
            sellerStateText = "待審核";
        } else if (sellerState == 2) {
            sellerStateText = "同意";
        } else if (sellerState == 3) {
            sellerStateText = "不同意";
        }

        if (adminState == 1) {
            adminStateText = "待審核";
        } else if (adminState == 2) {
            adminStateText = "同意";
        } else if (adminState == 3) {
            adminStateText = "不同意";
        }

        if (refundState == 1) {
            refundStateText = "处理中";
        } else if (refundState == 2) {
            refundStateText = "待管理员处理";
        } else if (refundState == 3) {
            refundStateText = "已完成";
        } else if (refundState == 4) {
            refundStateText = "會員取消";
        }

        if (showSellerHandle == 1) {
            showSellerHandleText = "處理";
        } else if (showSellerHandle == 0) {
            showSellerHandleText = "查看";
        } else if (showSellerHandle == 2) {
            showSellerHandleText = "收貨";
        }
    }

    public long getRefundSn() {
        return refundSn;
    }

    public void setRefundSn(long refundSn) {
        this.refundSn = refundSn;
    }

    public String getRefundSnText() {
        return refundSnText;
    }

    public void setRefundSnText(String refundSnText) {
        this.refundSnText = refundSnText;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public long getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(long ordersSn) {
        this.ordersSn = ordersSn;
    }

    public String getOrdersSnText() {
        return ordersSnText;
    }

    public void setOrdersSnText(String ordersSnText) {
        this.ordersSnText = ordersSnText;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getRefundType() {
        return refundType;
    }

    public void setRefundType(int refundType) {
        this.refundType = refundType;
    }

    public int getReturnType() {
        return returnType;
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public int getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(int goodsState) {
        this.goodsState = goodsState;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getRefundMemberCancel() {
        return refundMemberCancel;
    }

    public void setRefundMemberCancel(int refundMemberCancel) {
        this.refundMemberCancel = refundMemberCancel;
    }

    public int getReturnMemberAutoCancel() {
        return returnMemberAutoCancel;
    }

    public void setReturnMemberAutoCancel(int returnMemberAutoCancel) {
        this.returnMemberAutoCancel = returnMemberAutoCancel;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public int getSellerState() {
        return sellerState;
    }

    public void setSellerState(int sellerState) {
        this.sellerState = sellerState;
    }

    public int getAdminState() {
        return adminState;
    }

    public void setAdminState(int adminState) {
        this.adminState = adminState;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public int getShowSellerHandle() {
        return showSellerHandle;
    }

    public void setShowSellerHandle(int showSellerHandle) {
        this.showSellerHandle = showSellerHandle;
    }

    public double getRefundAmount() {
        return refundAmount;
    }
}
