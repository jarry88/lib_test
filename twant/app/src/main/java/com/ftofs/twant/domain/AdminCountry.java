package com.ftofs.twant.domain;

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

    public AdminCountry(){
    }

    public AdminCountry(String countryCn, String countryEn, String nationalFlag, String countryContinents, int countrySort) {
        this.countryCn = countryCn;
        this.countryEn = countryEn;
        this.nationalFlag = nationalFlag;
        this.countryContinents = countryContinents;
        this.countrySort = countrySort;
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
