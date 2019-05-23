package com.ftofs.twant.entity;

/**
 * 手机号类
 * @author zwm
 */
public class Mobile {
    public Mobile(int areaId, String areaCode, String mobile) {
        this.areaId = areaId;
        this.areaCode = areaCode;
        this.mobile = mobile;
    }

    public Mobile() {

    }

    /**
     * 地区Id
     */
    public int areaId;
    /**
     * 区号
     */
    public String areaCode;
    public String mobile;
}
