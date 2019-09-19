package com.ftofs.twant.entity;

/**
 * 經緯度定位數據
 * @author zwm
 */
public class Location {
    public double longitude;
    public double latitude;
    /**
     * 定位出此經緯度的時間
     */
    public long timestamp;

    public Location(double longitude, double latitude, long timestamp) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
    }

    public Location() {
    }
}


