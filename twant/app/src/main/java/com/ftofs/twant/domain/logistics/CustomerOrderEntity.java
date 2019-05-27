package com.ftofs.twant.domain.logistics;

import java.math.BigInteger;

public class CustomerOrderEntity {
    private int logisticsId;

    /**
     * 受理單號
     */
    private String customerOrderNumber;

    /**
     * 訂單編號
     */
    private BigInteger ordersSn;

    /**
     * 快遞實體類
     */
    private String entity;

    /**
     * 物流類型
     */
    private String logisticsType;

    /**
     * 創建時間
     */
    private String createTime;

    /**
     * 寄件人公司名稱
     */
    private String consignerCompanyName;

    /**
     * 收件人公司名稱
     */
    private String consigneeCompanyName;

    /**
     * 寄件人座機號碼
     */
    private String consignerLandlinePhone;

    /**
     * 收件人座機號碼
     */
    private String consigneeLandlinePhone;

    /**
     * 物流狀態
     */
    private Integer state;

    /**
     * 目的地
     */
    private String endAddress ;

    /**
     * 起始地
     */
    private String startAddress ;

    /**
     * 快遞客戶單號
     */
    private String originalOrderNumber ;

    /**
     * 物流完成時間
     */
    private String updated ;

    /**
     * 訂單創建時間
     */
    private String orderCreateTime;

    public int getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(int logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getCustomerOrderNumber() {
        return customerOrderNumber;
    }

    public void setCustomerOrderNumber(String customerOrderNumber) {
        this.customerOrderNumber = customerOrderNumber;
    }

    public BigInteger getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(BigInteger ordersSn) {
        this.ordersSn = ordersSn;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getConsignerCompanyName() {
        return consignerCompanyName;
    }

    public void setConsignerCompanyName(String consignerCompanyName) {
        this.consignerCompanyName = consignerCompanyName;
    }

    public String getConsigneeCompanyName() {
        return consigneeCompanyName;
    }

    public void setConsigneeCompanyName(String consigneeCompanyName) {
        this.consigneeCompanyName = consigneeCompanyName;
    }

    public String getConsignerLandlinePhone() {
        return consignerLandlinePhone;
    }

    public void setConsignerLandlinePhone(String consignerLandlinePhone) {
        this.consignerLandlinePhone = consignerLandlinePhone;
    }

    public String getConsigneeLandlinePhone() {
        return consigneeLandlinePhone;
    }

    public void setConsigneeLandlinePhone(String consigneeLandlinePhone) {
        this.consigneeLandlinePhone = consigneeLandlinePhone;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getOriginalOrderNumber() {
        return originalOrderNumber;
    }

    public void setOriginalOrderNumber(String originalOrderNumber) {
        this.originalOrderNumber = originalOrderNumber;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    @Override
    public String toString() {
        return "CustomerOrderEntity{" +
                "logisticsId=" + logisticsId +
                ", customerOrderNumber='" + customerOrderNumber + '\'' +
                ", ordersSn=" + ordersSn +
                ", entity='" + entity + '\'' +
                ", logisticsType='" + logisticsType + '\'' +
                ", createTime=" + createTime +
                ", consignerCompanyName='" + consignerCompanyName + '\'' +
                ", consigneeCompanyName='" + consigneeCompanyName + '\'' +
                ", consignerLandlinePhone='" + consignerLandlinePhone + '\'' +
                ", consigneeLandlinePhone='" + consigneeLandlinePhone + '\'' +
                ", state=" + state +
                ", endAddress='" + endAddress + '\'' +
                ", startAddress='" + startAddress + '\'' +
                ", originalOrderNumber='" + originalOrderNumber + '\'' +
                ", updated='" + updated + '\'' +
                ", orderCreateTime='" + orderCreateTime + '\'' +
                '}';
    }
}
