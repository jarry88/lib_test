package com.ftofs.twant.entity;

public class TimeInfo {
    public int day;
    public int hour;
    public int minute;
    public int second;


    @Override
    public String toString() {
        return String.format("%d %02d:%02d:%02d", day, hour, minute, second);
    }
}
