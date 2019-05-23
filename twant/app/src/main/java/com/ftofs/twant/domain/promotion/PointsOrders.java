package com.ftofs.twant.domain.promotion;

import java.io.Serializable;


public class PointsOrders implements Serializable {
    /**
     * 积分订单编号
     */
    private int pointsOrdersId;

    /**
     * 积分订单外部编号
     */
    private long pointsOrdersSn;

    /**
     * 订单状态
     * 0-已取消 10-新订单 20-已发货 30-已收货
     */
    private int pointsOrdersState = 10;

    /**
     * 订单状态文字
     */
    private String pointsOrdersStateText;

    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 一级地区ID
     */
    private int receiverAreaId1;

    /**
     * 二级地区ID
     */
    private int receiverAreaId2;

    /**
     * 三级地区Id
     */
    private int receiverAreaId3;

    /**
     * 四级地区
     */
    private int receiverAreaId4;

    /**
     * 省市县(区)内容
     */
    private String receiverAreaInfo = "";

    /**
     * 收货人地址
     */
    private String receiverAddress = "";

    /**
     * 收货人电话
     */
    private String receiverPhone = "";

    /**
     * 收货人姓名
     */
    private String receiverName = "";

    /**
     * 买家留言
     */
    private String receiverMessage = "";

    /**
     * 下单时间
     */
    private String createTime;

    /**
     * 发货时间
     */
    private String sendTime;

    /**
     * 订单完成时间
     */
    private String finishTime;

    /**
     * 关闭时间
     */
    private String cancelTime;

    /**
     * 发货单号
     */
    private String shipSn = "";

    /**
     * 快递公司
     */
    private String shipName = "";

    /**
     * 快递公司编码
     */
    private String shipCode = "";

    /**
     * 发货备注
     */
    private String shipNote = "";

    /**
     * 所需积分
     */
    private int expendPoints = 0;

    /**
     * 总消耗积分
     */
    private int totalPoints = 0;

    /**
     * 商品SPU编号
     */
    private int commonId;

    /**
     * 商品Id
     */
    private int goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 完整规格
     * 例“颜色：红色，尺码：L”
     */
    private String goodsFullSpecs;

    /**
     * 兑换数量
     */
    private int buyNum = 0;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 图片路径
     */
    private String imageSrc;

    /**
     * 计量单位
     */
    private String unitName;

    public int getPointsOrdersId() {
        return pointsOrdersId;
    }

    public void setPointsOrdersId(int pointsOrdersId) {
        this.pointsOrdersId = pointsOrdersId;
    }

    public long getPointsOrdersSn() {
        return pointsOrdersSn;
    }

    public void setPointsOrdersSn(long pointsOrdersSn) {
        this.pointsOrdersSn = pointsOrdersSn;
    }

    public int getPointsOrdersState() {
        return pointsOrdersState;
    }

    public void setPointsOrdersState(int pointsOrdersState) {
        this.pointsOrdersState = pointsOrdersState;
    }

    public String getPointsOrdersStateText() {
        return pointsOrdersStateText;
    }

    public void setPointsOrdersStateText(String pointsOrdersStateText) {
        this.pointsOrdersStateText = pointsOrdersStateText;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getReceiverAreaId1() {
        return receiverAreaId1;
    }

    public void setReceiverAreaId1(int receiverAreaId1) {
        this.receiverAreaId1 = receiverAreaId1;
    }

    public int getReceiverAreaId2() {
        return receiverAreaId2;
    }

    public void setReceiverAreaId2(int receiverAreaId2) {
        this.receiverAreaId2 = receiverAreaId2;
    }

    public int getReceiverAreaId3() {
        return receiverAreaId3;
    }

    public void setReceiverAreaId3(int receiverAreaId3) {
        this.receiverAreaId3 = receiverAreaId3;
    }

    public int getReceiverAreaId4() {
        return receiverAreaId4;
    }

    public void setReceiverAreaId4(int receiverAreaId4) {
        this.receiverAreaId4 = receiverAreaId4;
    }

    public String getReceiverAreaInfo() {
        return receiverAreaInfo;
    }

    public void setReceiverAreaInfo(String receiverAreaInfo) {
        this.receiverAreaInfo = receiverAreaInfo;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMessage() {
        return receiverMessage;
    }

    public void setReceiverMessage(String receiverMessage) {
        this.receiverMessage = receiverMessage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getShipSn() {
        return shipSn;
    }

    public void setShipSn(String shipSn) {
        this.shipSn = shipSn;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipNote() {
        return shipNote;
    }

    public void setShipNote(String shipNote) {
        this.shipNote = shipNote;
    }

    public int getExpendPoints() {
        return expendPoints;
    }

    public void setExpendPoints(int expendPoints) {
        this.expendPoints = expendPoints;
    }

    public int getTotalPoints() {
        return expendPoints * buyNum;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getImageSrc() {
        return goodsImage;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return "PointsOrders{" +
                "pointsOrdersId=" + pointsOrdersId +
                ", pointsOrdersSn=" + pointsOrdersSn +
                ", pointsOrdersState=" + pointsOrdersState +
                ", pointsOrdersStateText='" + pointsOrdersStateText + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", receiverAreaId1=" + receiverAreaId1 +
                ", receiverAreaId2=" + receiverAreaId2 +
                ", receiverAreaId3=" + receiverAreaId3 +
                ", receiverAreaId4=" + receiverAreaId4 +
                ", receiverAreaInfo='" + receiverAreaInfo + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverMessage='" + receiverMessage + '\'' +
                ", createTime=" + createTime +
                ", sendTime=" + sendTime +
                ", finishTime=" + finishTime +
                ", cancelTime=" + cancelTime +
                ", shipSn='" + shipSn + '\'' +
                ", shipName='" + shipName + '\'' +
                ", shipCode='" + shipCode + '\'' +
                ", shipNote='" + shipNote + '\'' +
                ", expendPoints=" + expendPoints +
                ", totalPoints=" + totalPoints +
                ", commonId=" + commonId +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", buyNum=" + buyNum +
                ", goodsImage='" + goodsImage + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}
