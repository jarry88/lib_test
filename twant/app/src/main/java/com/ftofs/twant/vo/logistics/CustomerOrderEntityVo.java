package com.ftofs.twant.vo.logistics;

import java.math.BigInteger;
import java.util.List;

/**
 * @Description: 受理单
 * @Auther: yangjian
 * @Date: 2019/4/23 17:10
 */
public class CustomerOrderEntityVo {
    private String originalOrderNumber ; //客户单号:非必填
    private String consigneeAddress ; //收货人地址:非必填
    private String consigneeName ; //收货人姓名:非必填
    private String consigneePhone ; //收货人电话:非必填
    private String consignerAddress ; //发货人地址:必填
    private String consignerName ; //发货人姓名:必填
    private String consignerPhone ; //发货人电话:必填
    private String endAddress ; //目的地:非必填
    private String remarks ; //备注:非必填
    private String startAddress ; //起始地:非必填

    private List<CargoVo> cargoList; // 货物信息

    //只保存到數據庫 不屬於3TMS受理單字段
    private String consignerCompanyName; //寄件人公司名稱

    private String consigneeCompanyName; //收件人公司名稱

    private String consignerLandlinePhone; //寄件人座機號碼

    private String consigneeLandlinePhone; //收件人座機號碼

    private BigInteger ordersSn; //訂單編號

    private String logisticsType; //物流類型

    private Integer state; //物流狀態

    private String customerOrderNumber; //受理單號

    private String shipUrl; //物流公司網址

    private int logisticsId; //本地受理單id

    private String createTime; //創建時間/發貨日期

    public String getOriginalOrderNumber() {
        return originalOrderNumber;
    }

    public void setOriginalOrderNumber(String originalOrderNumber) {
        this.originalOrderNumber = originalOrderNumber;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsignerAddress() {
        return consignerAddress;
    }

    public void setConsignerAddress(String consignerAddress) {
        this.consignerAddress = consignerAddress;
    }

    public String getConsignerName() {
        return consignerName;
    }

    public void setConsignerName(String consignerName) {
        this.consignerName = consignerName;
    }

    public String getConsignerPhone() {
        return consignerPhone;
    }

    public void setConsignerPhone(String consignerPhone) {
        this.consignerPhone = consignerPhone;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public List<CargoVo> getCargoList() {
        return cargoList;
    }

    public void setCargoList(List<CargoVo> cargoList) {
        this.cargoList = cargoList;
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

    public BigInteger getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(BigInteger ordersSn) {
        this.ordersSn = ordersSn;
    }

    public String getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCustomerOrderNumber() {
        return customerOrderNumber;
    }

    public void setCustomerOrderNumber(String customerOrderNumber) {
        this.customerOrderNumber = customerOrderNumber;
    }

    public String getShipUrl() {
        return shipUrl;
    }

    public void setShipUrl(String shipUrl) {
        this.shipUrl = shipUrl;
    }

    public int getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(int logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CustomerOrderEntityVo{" +
                "originalOrderNumber='" + originalOrderNumber + '\'' +
                ", consigneeAddress='" + consigneeAddress + '\'' +
                ", consigneeName='" + consigneeName + '\'' +
                ", consigneePhone='" + consigneePhone + '\'' +
                ", consignerAddress='" + consignerAddress + '\'' +
                ", consignerName='" + consignerName + '\'' +
                ", consignerPhone='" + consignerPhone + '\'' +
                ", endAddress='" + endAddress + '\'' +
                ", remarks='" + remarks + '\'' +
                ", startAddress='" + startAddress + '\'' +
                ", cargoList=" + cargoList +
                ", consignerCompanyName='" + consignerCompanyName + '\'' +
                ", consigneeCompanyName='" + consigneeCompanyName + '\'' +
                ", consignerLandlinePhone='" + consignerLandlinePhone + '\'' +
                ", consigneeLandlinePhone='" + consigneeLandlinePhone + '\'' +
                ", ordersSn=" + ordersSn +
                ", logisticsType='" + logisticsType + '\'' +
                ", state=" + state +
                ", customerOrderNumber='" + customerOrderNumber + '\'' +
                ", shipUrl='" + shipUrl + '\'' +
                ", logisticsId=" + logisticsId +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
