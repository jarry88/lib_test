package com.ftofs.twant.entity;

/**
 * 商店地圖信息
 * @author zwm
 */
public class StoreMapInfo {

    public StoreMapInfo(double storeLongitude, double storeLatitude, double storeDistance,
                        double myLongitude, double myLatitude,
                        String storeName, String storeAddress, String storePhone,String busInfo) {
        this.storeLongitude = storeLongitude;
        this.storeLatitude = storeLatitude;
        this.storeDistance = storeDistance;
        this.myLongitude = myLongitude;
        this.myLatitude = myLatitude;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storePhone = storePhone;
        this.busInfo = busInfo;
    }

    public StoreMapInfo(double storeLongitude, double storeLatitude,
                        String storeName, String storeAddress, String storePhone) {
        this.storeLongitude = storeLongitude;
        this.storeLatitude = storeLatitude;
        this.storeDistance = 0;
        this.myLongitude = 0;
        this.myLatitude = 0;
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
    public String busInfo;


    /**
     * 商店電話
     */
    public String storePhone;


}
