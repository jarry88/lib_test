package com.ftofs.twant.entity;

import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 收貨地址數據結構
 * @author zwm
 */
public class AddrItem {
    public AddrItem(int addressId, String realName, List<Integer> areaIdList, int areaId, String areaInfo,
                    String address, String mobileAreaCode, String mobPhone, int isDefault) {
        this.addressId = addressId;
        this.realName = realName;
        this.areaIdList = areaIdList;
        this.areaId = areaId;
        this.areaInfo = areaInfo;
        this.address = address;
        this.mobileAreaCode = mobileAreaCode;
        this.mobPhone = mobPhone;
        this.isDefault = isDefault;
    }

    public EasyJSONObject toEasyJSONObject() {
        return EasyJSONObject.generate(
                "addressId", addressId,
                "realName", realName,
                "areaId1", areaIdList.get(0),
                "areaId2", areaIdList.get(1),
                "areaId3", areaIdList.get(2),
                "areaId4", areaIdList.get(3),
                "areaId", areaId,
                "areaInfo", areaInfo,
                "address", address,
                "mobPhone", mobileAreaCode + mobPhone,
                "isDefault", isDefault);
    }

    public int addressId;
    public String realName;
    public List<Integer> areaIdList;
    public int areaId;
    public String areaInfo;
    public String address;
    public String mobileAreaCode;
    public String mobPhone;
    public int isDefault;
}
