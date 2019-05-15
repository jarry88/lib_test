package com.ftofs.twant.domain.goods;

public class BrandApply {
    /**
     * 品牌编号
     */
    private int brandId;

    /**
     * 注册号
     */
    private String registerNumber;

    /**
     * 注册证、受理书1
     */
    private String image1;

    /**
     * 注册证、受理书2
     */
    private String image2;

    /**
     * 所有者
     */
    private String owner;

    /**
     * 审核失败原因
     */
    private String stateRemark;

    /**
     * 注册证、受理书1图片路径
     */
    private String image1Src;

    /**
     * 注册证、受理书2图片路径
     */
    private String image2Src;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStateRemark() {
        return stateRemark;
    }

    public void setStateRemark(String stateRemark) {
        this.stateRemark = stateRemark;
    }

    public String getImage1Src() {
       return image1;
    }

    public String getImage2Src() {
        return image2;
    }

    @Override
    public String toString() {
        return "BrandApply{" +
                "brandId=" + brandId +
                ", registerNumber='" + registerNumber + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", owner='" + owner + '\'' +
                ", stateRemark='" + stateRemark + '\'' +
                ", image1Src='" + image1Src + '\'' +
                ", image2Src='" + image2Src + '\'' +
                '}';
    }
}
