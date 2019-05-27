package com.ftofs.twant.domain.chain;

public class Chain {
    /**
     * 门店编号
     */
    private int chainId;

    /**
     * 门店名称
     */
    private String chainName;

    /**
     * 门店类型
     */
    private int classId;

    /**
     * 门店类型名称
     */
    private String className;

    /**
     * 店主名称
     */
    private String clerkName;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 一级地区编号
     */
    private int areaId1 = 0;

    /**
     * 二级地区编号
     */
    private int areaId2 = 0;
    /**
     * 三级地区编号
     */

    private int areaId3 = 0;

    /**
     * 四级地区编号
     */
    private int areaId4 = 0;

    /**
     * 地区内容
     */
    private String areaInfo;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String chainPhone;

    /**
     * 营业时间
     */
    private String chainOpeningTime;

    /**
     * 交通线路
     */
    private String chainTrafficLine;

    /**
     * 门店背景
     */
    private String imageName1="";

    /**
     * 图片背景路径
     */
    private String imageSrc1="";

    /**
     * 门店头像
     */
    private String imageName2="";

    /**
     * 图片头像路径
     */
    private String imageSrc2="";

    /**
     * 高德经度
     */
    private Double lng = 0d;

    /**
     * 字符串形式
     */
    private String lngString;

    /**
     * 高德纬度
     */
    private Double lat = 0d;

    /**
     * 字符串形式
     */
    private String latString;

    /**
     * 好评率
     */
    private int chainGoodsRate = 100;

    /**
     * 门店评分
     */
    private int chainCriterion = 5;

    /**
     * 总评价次数
     */
    private int evaluateNum = 0;

    /**
     * 获得星星总和
     */
    private int allStarSum = 0;

    /**
     * 获得一星次数
     */
    private int oneStarNum = 0;

    /**
     * 获得二星次数
     */
    private int twoStarNum = 0;

    /**
     * 获得三星次数
     */
    private int threeStarNum = 0;

    /**
     * 获得四星次数
     */
    private int fourStatNum = 0;

    /**
     * 获得五星次数
     */
    private int fiveStarNum = 0;

    /**
     * 場地面積
     */
    private Float chainSize;

    /**
     * 門店員工數
     */
    private int employeeCount = 0;

    /**
     * 門店注冊資金
     */
    private int registeredCapital = 0;

    /**
     * 門店聯係人姓名
     */
    private String contactsName;

    /**
     * 門店聯係人電話
     */
    private String contactsPhone;

    /**
     * 電子郵箱
     */
    private String contactsEmail;

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

    public String getChainPhone() {
        return chainPhone;
    }

    public void setChainPhone(String chainPhone) {
        this.chainPhone = chainPhone;
    }

    public String getChainOpeningTime() {
        return chainOpeningTime;
    }

    public void setChainOpeningTime(String chainOpeningTime) {
        this.chainOpeningTime = chainOpeningTime;
    }

    public String getChainTrafficLine() {
        return chainTrafficLine;
    }

    public void setChainTrafficLine(String chainTrafficLine) {
        this.chainTrafficLine = chainTrafficLine;
    }

    public String getImageName1() {
        return imageName1;
    }

    public void setImageName1(String imageName1) {
        this.imageName1 = imageName1;
    }

    public String getImageSrc1() {
       return imageSrc1;
    }

    public void setImageSrc1(String imageSrc1) {
        this.imageSrc1 = imageSrc1;
    }

    public String getImageName2() {
        return imageName2;
    }

    public void setImageName2(String imageName2) {
        this.imageName2 = imageName2;
    }

    public String getImageSrc2() {
       return imageSrc2;
    }

    public void setImageSrc2(String imageSrc2) {
        this.imageSrc2 = imageSrc2;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getLngString() {
        return String.valueOf(lng);
    }

    public void setLngString(String lngString) {
        this.lngString = lngString;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getLatString() {
        return String.valueOf(lat);
    }

    public void setLatString(String latString) {
        this.latString = latString;
    }

    public int getChainGoodsRate() {
        return chainGoodsRate;
    }

    public void setChainGoodsRate(int chainGoodsRate) {
        this.chainGoodsRate = chainGoodsRate;
    }

    public int getChainCriterion() {
        return chainCriterion;
    }

    public void setChainCriterion(int chainCriterion) {
        this.chainCriterion = chainCriterion;
    }

    public int getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(int evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public int getAllStarSum() {
        return allStarSum;
    }

    public void setAllStarSum(int allStarSum) {
        this.allStarSum = allStarSum;
    }

    public int getOneStarNum() {
        return oneStarNum;
    }

    public void setOneStarNum(int oneStarNum) {
        this.oneStarNum = oneStarNum;
    }

    public int getTwoStarNum() {
        return twoStarNum;
    }

    public void setTwoStarNum(int twoStarNum) {
        this.twoStarNum = twoStarNum;
    }

    public int getThreeStarNum() {
        return threeStarNum;
    }

    public void setThreeStarNum(int threeStarNum) {
        this.threeStarNum = threeStarNum;
    }

    public int getFourStatNum() {
        return fourStatNum;
    }

    public void setFourStatNum(int fourStatNum) {
        this.fourStatNum = fourStatNum;
    }

    public int getFiveStarNum() {
        return fiveStarNum;
    }

    public void setFiveStarNum(int fiveStarNum) {
        this.fiveStarNum = fiveStarNum;
    }

    public Float getChainSize() {
        return chainSize;
    }

    public void setChainSize(Float chainSize) {
        this.chainSize = chainSize;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public int getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(int registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
    }

    @Override
    public String toString() {
        return "Chain{" +
                "chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", classId=" + classId +
                ", className='" + className + '\'' +
                ", clerkName='" + clerkName + '\'' +
                ", storeId=" + storeId +
                ", areaId1=" + areaId1 +
                ", areaId2=" + areaId2 +
                ", areaId3=" + areaId3 +
                ", areaId4=" + areaId4 +
                ", areaInfo='" + areaInfo + '\'' +
                ", address='" + address + '\'' +
                ", chainPhone='" + chainPhone + '\'' +
                ", chainOpeningTime='" + chainOpeningTime + '\'' +
                ", chainTrafficLine='" + chainTrafficLine + '\'' +
                ", imageName1='" + imageName1 + '\'' +
                ", imageSrc1='" + imageSrc1 + '\'' +
                ", imageName2='" + imageName2 + '\'' +
                ", imageSrc2='" + imageSrc2 + '\'' +
                ", lng=" + lng +
                ", lngString='" + lngString + '\'' +
                ", lat=" + lat +
                ", latString='" + latString + '\'' +
                ", chainGoodsRate=" + chainGoodsRate +
                ", chainCriterion=" + chainCriterion +
                ", evaluateNum=" + evaluateNum +
                ", allStarSum=" + allStarSum +
                ", oneStarNum=" + oneStarNum +
                ", twoStarNum=" + twoStarNum +
                ", threeStarNum=" + threeStarNum +
                ", fourStatNum=" + fourStatNum +
                ", fiveStarNum=" + fiveStarNum +
                ", chainSize=" + chainSize +
                '}';
    }
}
