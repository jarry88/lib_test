package com.ftofs.twant.domain.store;

public class StoreSocialAccount {
    /**
     * 编号
     */
    private int id;

    /**
     * 社交工具id
     */
    private int socialId;

    /**
     * 店鋪id
     */
    private int storeId;

    /**
     * 店鋪社交賬號
     */
    private String account;

    /**
     * 账号主页地址
     */
    private String accountAddress;

    /**
     * 图片地址
     */
    private String accountImageAddress;

    /**
     * 添加時間
     */
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSocialId() {
        return socialId;
    }

    public void setSocialId(int socialId) {
        this.socialId = socialId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public String getAccountImageAddress() {
        return accountImageAddress;
    }

    public void setAccountImageAddress(String accountImageAddress) {
        this.accountImageAddress = accountImageAddress;
    }

    @Override
    public String toString() {
        return "StoreSocialAccount{" +
                "id=" + id +
                ", socialId=" + socialId +
                ", storeId=" + storeId +
                ", account='" + account + '\'' +
                ", accountAddress='" + accountAddress + '\'' +
                ", accountImageAddress='" + accountImageAddress + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
