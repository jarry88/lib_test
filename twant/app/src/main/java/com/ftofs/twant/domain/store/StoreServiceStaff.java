package com.ftofs.twant.domain.store;

public class StoreServiceStaff {
    private int staffId;

    /**
     * 商店ID
     */
    private int storeId;

    /**
     * 客服名稱
     */
    private String staffName;

    /**
     * 關聯的城友名稱
     */
    private String memberName;

    /**
     * 客服類型 StoreServiceStaffType
     */
    private int staffType;

    /**
     * 招呼語
     */
    private String welcome;

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getStaffType() {
        return staffType;
    }

    public void setStaffType(int staffType) {
        this.staffType = staffType;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }
}
