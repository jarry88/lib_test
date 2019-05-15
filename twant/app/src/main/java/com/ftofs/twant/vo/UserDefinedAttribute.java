package com.ftofs.twant.vo;

/**
 * @author liusf
 * @create 2018/10/27 19:00
 * @description 重名屬性區分
 */
public class UserDefinedAttribute {
    private int chainClassId ;
    private int storeJoininClassId;

    public int getChainClassId() {
        return chainClassId;
    }

    public void setChainClassId(int chainClassId) {
        this.chainClassId = chainClassId;
    }

    public int getStoreJoininClassId() {
        return storeJoininClassId;
    }

    public void setStoreJoininClassId(int storeJoininClassId) {
        this.storeJoininClassId = storeJoininClassId;
    }
}
