package com.ftofs.twant.domain.logistics;

import com.ftofs.twant.vo.logistics.CargoVo;



public class Cargo {
    private int cargoId;

    /**
     * 货物名称
     */
    private String cargoName ;

    /**
     * 物流受理單id
     */
    private int logisticsId ;

    /**
     * 货物备注
     */
    private String productModel ;

    /**
     * 件数
     */
    private String quantity ;

    /**
     * 重量
     */
    private float weight ;

    /**
     * 貨物高
     */
    private float height;

    /**
     * 貨物寬
     */
    private float width;

    /**
     * 貨物長度
     */
    private float cargoLong;

    /**
     * 創建時間
     */
    private String createTime;

    public Cargo(){}

    public Cargo(CargoVo cargoVo){
        this.cargoName = cargoVo.getName();
        this.productModel = cargoVo.getProductModel();
        this.quantity = cargoVo.getQuantity();
        this.weight = cargoVo.getWeight();
        this.cargoLong = cargoVo.getCargoLong();
        this.width = cargoVo.getWidth();
        this.height = cargoVo.getHeight();
    }

    public int getCargoId() {
        return cargoId;
    }

    public void setCargoId(int cargoId) {
        this.cargoId = cargoId;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(int logisticsId) {
        this.logisticsId = logisticsId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getCargoLong() {
        return cargoLong;
    }

    public void setCargoLong(float cargoLong) {
        this.cargoLong = cargoLong;
    }

    public void setCargoLong(int cargoLong) {
        this.cargoLong = cargoLong;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "cargoId=" + cargoId +
                ", cargoName='" + cargoName + '\'' +
                ", logisticsId=" + logisticsId +
                ", productModel='" + productModel + '\'' +
                ", quantity='" + quantity + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", width=" + width +
                ", cargoLong=" + cargoLong +
                ", createTime=" + createTime +
                '}';
    }
}
