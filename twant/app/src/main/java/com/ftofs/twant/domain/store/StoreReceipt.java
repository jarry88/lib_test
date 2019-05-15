package com.ftofs.twant.domain.store;

import java.io.Serializable;
import java.sql.Timestamp;

public class StoreReceipt implements Serializable{
    /**
     * 主鍵
     */
    private int id;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 公司標志圖片-彩色
     */
    private String companyLogoColorful;

    /**
     * 公司標志圖片-黑白
     */
    private String companyLogoBlackWhite;

    /**
     * 是否顯示公司標志(1:顯示 0:不顯示)
     */
    private int companyLogoDisplay;

    /**
     * 是否顯示商品編號(1:顯示 0:不顯示)
     */
    private int goodsNoDisplay;

    /**
     * 二維碼圖片
     */
    private String qrCode;

    /**
     * 是否顯示二維碼(1:顯示 0:不顯示)
     */
    private int qrCodeDisplay;

    /**
     * 備注
     */
    private String remark;

    /**
     * 狀態(1:可用 0:不可用)
     */
    private int status;

    /**
     * 類型(1:购物小票模板 0:退货小票模板 )
     */
    private int type;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp modifyTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getCompanyLogoColorful() {
        return companyLogoColorful;
    }

    public void setCompanyLogoColorful(String companyLogoColorful) {
        this.companyLogoColorful = companyLogoColorful;
    }

    public String getCompanyLogoBlackWhite() {
        return companyLogoBlackWhite;
    }

    public void setCompanyLogoBlackWhite(String companyLogoBlackWhite) {
        this.companyLogoBlackWhite = companyLogoBlackWhite;
    }

    public int getCompanyLogoDisplay() {
        return companyLogoDisplay;
    }

    public void setCompanyLogoDisplay(int companyLogoDisplay) {
        this.companyLogoDisplay = companyLogoDisplay;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getQrCodeDisplay() {
        return qrCodeDisplay;
    }

    public void setQrCodeDisplay(int qrCodeDisplay) {
        this.qrCodeDisplay = qrCodeDisplay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getGoodsNoDisplay() {
        return goodsNoDisplay;
    }

    public void setGoodsNoDisplay(int goodsNoDisplay) {
        this.goodsNoDisplay = goodsNoDisplay;
    }

    @Override
    public String toString() {
        return "StoreReceipt{" +
                "id=" + id +
                ", storeId=" + storeId +
                ", companyLogoColorful='" + companyLogoColorful + '\'' +
                ", companyLogoBlackWhite='" + companyLogoBlackWhite + '\'' +
                ", companyLogoDisplay=" + companyLogoDisplay +
                ", goodsNoDisplay=" + goodsNoDisplay +
                ", qrCode='" + qrCode + '\'' +
                ", qrCodeDisplay=" + qrCodeDisplay +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", createTime=" + createTime +
                ", modifyTIme=" + modifyTime +
                '}';
    }
}