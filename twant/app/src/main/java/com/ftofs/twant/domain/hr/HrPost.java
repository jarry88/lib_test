package com.ftofs.twant.domain.hr;

public class HrPost {
    /**
     * 招聘文章id
     */
    private int postId;

    /**
     * 標題（職位名稱）
     */
    private String postTitle;

    /**
     * 文章類型
     */
    private int postType;

    /**
     * 地區id
     */
    private int postAreaId;

    /**
     * 工作地點
     */
    private String workLocation;

    /**
     * 薪資類型
     */
    private int salaryType;

    /**
     * 薪資範圍
     */
    private int salaryRange;

    /**
     * 薪資單位
     */
    private int currency;

    /**
     * 最低薪資
     */
    private int customizeLow;

    /**
     * 最高薪資
     */
    private int customizeHigh;

    /**
     * 職位描述
     */
    private String postDescription;

    /**
     * 郵箱
     */
    private String mailbox;

    /**
     * 審核狀態 0審核中 1審核通過 2審核不通過 3保存草稿
     */
    private int isPublish;

    /**
     * 上線狀態太 1上線中 0下線中 2管理员下线
     */
    private int isOnline;

    /**
     * 是否刪除 1已刪除  0未刪除
     */
    private int isDelete;

    /**
     * 發佈人
     */
    private String createBy;

    /**
     * 發佈時間
     */
    private String createTime;

    /**
     * 店鋪id
     */
    private int storeId;

    /**
     * 排序
     */
    private int sort;

    /**
     * 關注數
     */
    private int postFavor;

    /**
     * 审核失败原因
     */
    private String approveReason;

    /**
     * 發佈人暱稱
     */
    private String nickName;

    /**
     * 店铺名称
     */
    private String storeName;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public int getPostAreaId() {
        return postAreaId;
    }

    public void setPostAreaId(int postAreaId) {
        this.postAreaId = postAreaId;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public int getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(int salaryType) {
        this.salaryType = salaryType;
    }

    public int getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(int salaryRange) {
        this.salaryRange = salaryRange;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getCustomizeLow() {
        return customizeLow;
    }

    public void setCustomizeLow(int customizeLow) {
        this.customizeLow = customizeLow;
    }

    public int getCustomizeHigh() {
        return customizeHigh;
    }

    public void setCustomizeHigh(int customizeHigh) {
        this.customizeHigh = customizeHigh;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public int getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(int isPublish) {
        this.isPublish = isPublish;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getPostFavor() {
        return postFavor;
    }

    public void setPostFavor(int postFavor) {
        this.postFavor = postFavor;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getApproveReason() {
        return approveReason;
    }

    public void setApproveReason(String approveReason) {
        this.approveReason = approveReason;
    }

    @Override
    public String toString() {
        return "HrPost{" +
                "postId=" + postId +
                ", postTitle='" + postTitle + '\'' +
                ", postType=" + postType +
                ", postAreaId=" + postAreaId +
                ", workLocation='" + workLocation + '\'' +
                ", salaryType=" + salaryType +
                ", salaryRange=" + salaryRange +
                ", currency=" + currency +
                ", customizeLow=" + customizeLow +
                ", customizeHigh=" + customizeHigh +
                ", postDescription='" + postDescription + '\'' +
                ", mailbox='" + mailbox + '\'' +
                ", isPublish=" + isPublish +
                ", isOnline=" + isOnline +
                ", isDelete=" + isDelete +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", storeId=" + storeId +
                ", sort=" + sort +
                ", postFavor=" + postFavor +
                ", approveReason='" + approveReason + '\'' +
                ", nickName='" + nickName + '\'' +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
