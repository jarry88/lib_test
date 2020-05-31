package com.ftofs.twant.domain;

import cn.snailpad.easyjson.EasyJSONObject;

public class AdminCountry {
    /**
     * 編號
     */
    private int countryId;

    /**
     * 中文簡稱
     */
    private String countryCn;

    /**
     * 英文簡稱
     */
    private String countryEn;

    /**
     * 國旗
     */
    private String nationalFlag;

    /**
     * 所屬地區
     */
    private String countryContinents;

    /**
     * 排序
     */
    private int countrySort = 0;

    public static AdminCountry parase(Object o) throws Exception{
        EasyJSONObject item = (EasyJSONObject) o;
        AdminCountry adminCountry = new AdminCountry();
        adminCountry.countryId = item.getInt("countryId");
        adminCountry.countryContinents = item.getSafeString("countryContinents");
        return adminCountry;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryCn() {
        return countryCn;
    }

    public void setCountryCn(String countryCn) {
        this.countryCn = countryCn;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getNationalFlag() {
        return nationalFlag;
    }

    public void setNationalFlag(String nationalFlag) {
        this.nationalFlag = nationalFlag;
    }

    public String getCountryContinents() {
        return countryContinents;
    }

    public void setCountryContinents(String countryContinents) {
        this.countryContinents = countryContinents;
    }

    public int getCountrySort() {
        return countrySort;
    }

    public void setCountrySort(int countrySort) {
        this.countrySort = countrySort;
    }
}
