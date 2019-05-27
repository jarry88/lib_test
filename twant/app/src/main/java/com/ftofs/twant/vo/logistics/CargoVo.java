package com.ftofs.twant.vo.logistics;

/**
 * @Description: 货物信息
 * @Auther: yangjian
 * @Date: 2019/4/23 17:25
 */
public class CargoVo {
    private String name ; //货物名称
    private String productModel ; //货物备注
    private String quantity ; //件数
    private String status ; //货物状态
    private String value ; //货值
    private float volume ; //体积
    private float weight ; //重量

    //只保存到數據庫 不屬於3TMS受理單字段
    private float height; //高

    private float width; //寬

    private float cargoLong; //長度

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public float getVolume() {
        return cargoLong * width * height;
    }

    public void setVolume(float volume) {
        this.volume = volume;
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

    @Override
    public String toString() {
        return "CargoVo{" +
                "name='" + name + '\'' +
                ", productModel='" + productModel + '\'' +
                ", quantity='" + quantity + '\'' +
                ", status='" + status + '\'' +
                ", value='" + value + '\'' +
                ", volume=" + volume +
                ", weight=" + weight +
                ", height=" + height +
                ", width=" + width +
                ", cargoLong=" + cargoLong +
                '}';
    }
}
