package com.ftofs.twant.vo;

import com.ftofs.twant.domain.Address;
import com.ftofs.twant.domain.AdminMobileArea;

/**
 * @author liusf
 * @create 2019/2/2 14:32
 * @description 收货地址视图类
 * @params
 * @return
 */
public class AddressVo {
    /**
     * 收货地址编号<br>
     * 主键、自增
     */
    private Integer addressId;
    /**
     * 会员ID
     */
    private int memberId;
    /**
     * 联系人
     */
    private String realName;
    /**
     * 一级地区ID
     */
    private int areaId1;
    /**
     * 二级地区ID
     */
    private int areaId2;
    /**
     * 三级地区Id
     */
    private int areaId3;
    /**
     * 四级地区
     */
    private int areaId4;
    /**
     * 最末级地区
     */
    private int areaId;
    /**
     * 省市县(区)内容
     */
    private String areaInfo;
    /**
     * 街道地址
     */
    private String address;
    /**
     * 手机
     */
    private String mobPhone = "";
    /**
     * 區號
     */
    private String mobileAreaCode = "";
    /**
     * 固话
     */
    private String telPhone = "";
    /**
     * 是否作为默认收货地址<br>
     * 0-否 1-是
     */
    private int isDefault;

    /**
     * 区号信息
     */
    private int mobileAreaId;

    public AddressVo(Address address) {
        this.addressId = address.getAddressId();
        this.memberId = address.getMemberId();
        this.realName = address.getRealName();
        this.areaId1 = address.getAreaId1();
        this.areaId2 = address.getAreaId2();
        this.areaId3 = address.getAreaId3();
        this.areaId4 = address.getAreaId4();
        this.areaId = address.getAreaId();
        this.areaInfo = address.getAreaInfo();
        this.address = address.getAddress();
        this.mobPhone = address.getMobPhone();
        this.mobileAreaCode = address.getMobileAreaCode();
        this.telPhone = address.getTelPhone();
        this.isDefault = address.getIsDefault();
    }

    public AddressVo(Address address, AdminMobileArea adminMobileArea) {
        this.addressId = address.getAddressId();
        this.memberId = address.getMemberId();
        this.realName = address.getRealName();
        this.areaId1 = address.getAreaId1();
        this.areaId2 = address.getAreaId2();
        this.areaId3 = address.getAreaId3();
        this.areaId4 = address.getAreaId4();
        this.areaId = address.getAreaId();
        this.areaInfo = address.getAreaInfo();
        this.address = address.getAddress();
        this.mobPhone = address.getMobPhone();
        this.mobileAreaCode = address.getMobileAreaCode();
        this.telPhone = address.getTelPhone();
        this.isDefault = address.getIsDefault();
        this.mobileAreaId = adminMobileArea.getAreaId();
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getAreaId1() {
        return areaId1;
    }

    public void setAreaId1(int areaId1) {
        this.areaId1 = areaId1;
    }

    public int getAreaId2() {
        return areaId2;
    }

    public void setAreaId2(int areaId2) {
        this.areaId2 = areaId2;
    }

    public int getAreaId3() {
        return areaId3;
    }

    public void setAreaId3(int areaId3) {
        this.areaId3 = areaId3;
    }

    public int getAreaId4() {
        return areaId4;
    }

    public void setAreaId4(int areaId4) {
        this.areaId4 = areaId4;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobPhone() {
        return mobPhone;
    }

    public void setMobPhone(String mobPhone) {
        this.mobPhone = mobPhone;
    }

    public String getMobileAreaCode() {
        return mobileAreaCode;
    }

    public void setMobileAreaCode(String mobileAreaCode) {
        this.mobileAreaCode = mobileAreaCode;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getMobileAreaId() {
        return mobileAreaId;
    }

    public void setMobileAreaId(int mobileAreaId) {
        this.mobileAreaId = mobileAreaId;
    }
}
