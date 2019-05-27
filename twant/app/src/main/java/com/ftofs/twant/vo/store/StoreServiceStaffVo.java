package com.ftofs.twant.vo.store;

/**
 * @author liusf
 * @create 2019/1/19 12:07
 * @description 店铺客服视图类
 */
public class StoreServiceStaffVo {
    /**
     * 客服ID
     */
    private int staffId;

    /**
     * 店铺ID
     */
    private int storeId;

    /**
     * 客服名称
     */
    private String staffName;

    /**
     * 关联的会员名称
     */
    private String memberName;

    /**
     * 客服类型
     */
    private int staffType;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 頭像
     */
    private String avatar;

    /**
     * 招呼語
     */
    private String welcome = "";

    /**
     * 所在用戶組
     */
    private String groupName;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "StoreServiceStaffVo{" +
                "staffId=" + staffId +
                ", storeId=" + storeId +
                ", staffName='" + staffName + '\'' +
                ", memberName='" + memberName + '\'' +
                ", staffType=" + staffType +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", welcome='" + welcome + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
