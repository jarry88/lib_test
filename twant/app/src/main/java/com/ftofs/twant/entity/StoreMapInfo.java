package com.ftofs.twant.entity;

/**
 * 店鋪地圖信息
 * @author zwm
 */
public class StoreMapInfo {
    public StoreMapInfo(double storeLongitude, double storeLatitude, double storeDistance,
                        double myLongitude, double myLatitude,
                        String storeName, String storeAddress, String storePhone) {
        this.storeLongitude = storeLongitude;
        this.storeLatitude = storeLatitude;
        this.storeDistance = storeDistance;
        this.myLongitude = myLongitude;
        this.myLatitude = myLatitude;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storePhone = storePhone;
    }

    public double storeLongitude;
    public double storeLatitude;
    public double storeDistance;
    public double myLongitude;
    public double myLatitude;

    public String storeName;
    public String storeAddress;

    /**
     * 店鋪電話
     */
    public String storePhone;
}
