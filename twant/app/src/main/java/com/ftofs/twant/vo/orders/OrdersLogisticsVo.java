package com.ftofs.twant.vo.orders;


import com.ftofs.twant.vo.logistics.CargoVo;

/**
 * @Description: 訂單發貨參數實體類
 * @Auther: yangjian
 * @Date: 2019/4/26 17:31
 */
public class OrdersLogisticsVo {

    /**
     * 一级地区
     */
    private Integer areaId1 = 0;
    /**
     * 二级地区
     */
    private Integer areaId2 = 0;
    /**
     * 三级地区
     */
    private Integer areaId3 = 0;
    /**
     * 四级地区
     */
    private Integer areaId4 = 0;
    /**
     * 省市县内容
     */
    private String areaInfo;
    /**
     * 訂單id
     */
    private Integer ordersId;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货人电话
     */
    private String receiverPhone;
    /**
     * 收货人地址
     */
    private String receiverAddress;
    /**
     * 快递公司编码
     */
    private String shipCode;
    /**
     * 快递单号
     */
    private String shipSn;
    /**
     * 发货备注
     */
    private String shipNote;
    /**
     * 貨物信息
     */
    private CargoVo cargoVo;
    /**
     * 包裹描述
     */
    private String cargoName;

    public Integer getAreaId1() {
        return areaId1;
    }

    public void setAreaId1(Integer areaId1) {
        this.areaId1 = areaId1;
    }

    public Integer getAreaId2() {
        return areaId2;
    }

    public void setAreaId2(Integer areaId2) {
        this.areaId2 = areaId2;
    }

    public Integer getAreaId3() {
        return areaId3;
    }

    public void setAreaId3(Integer areaId3) {
        this.areaId3 = areaId3;
    }

    public Integer getAreaId4() {
        return areaId4;
    }

    public void setAreaId4(Integer areaId4) {
        this.areaId4 = areaId4;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public Integer getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(Integer ordersId) {
        this.ordersId = ordersId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipSn() {
        return shipSn;
    }

    public void setShipSn(String shipSn) {
        this.shipSn = shipSn;
    }

    public String getShipNote() {
        return shipNote;
    }

    public void setShipNote(String shipNote) {
        this.shipNote = shipNote;
    }

    public CargoVo getCargoVo() {
        return cargoVo;
    }

    public void setCargoVo(CargoVo cargoVo) {
        this.cargoVo = cargoVo;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    @Override
    public String toString() {
        return "OrdersLogisticsVo{" +
                "areaId1=" + areaId1 +
                ", areaId2=" + areaId2 +
                ", areaId3=" + areaId3 +
                ", areaId4=" + areaId4 +
                ", areaInfo='" + areaInfo + '\'' +
                ", ordersId=" + ordersId +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", shipCode='" + shipCode + '\'' +
                ", shipSn='" + shipSn + '\'' +
                ", shipNote='" + shipNote + '\'' +
                ", cargoVo=" + cargoVo +
                ", cargoName='" + cargoName + '\'' +
                '}';
    }
}
