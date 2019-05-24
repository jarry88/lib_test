package com.ftofs.twant.vo.hr;

import com.ftofs.twant.domain.hr.HrPost;
import com.ftofs.twant.vo.store.StoreVo;



/**
 * @Description: 招聘視圖對象
 * @Auther: yangjian
 * @Date: 2019/3/27 19:54
 */
public class HrPostVo {
    /**
     * 招聘文章id
     */
    private int postId;
    /**
     * 標題（職位名稱）
     */
    private String postTitle;
    /**
     * 職位類型
     */
    private String postType;
    /**
     * 一級地區
     */
    private String postAreaParent;
    /**
     * 二級地區
     */
    private String postArea;
    /**
     * 工作地點
     */
    private String workLocation;
    /**
     * 薪資類型
     */
    private String salaryType;
    /**
     * 薪資範圍
     */
    private String salaryRange;
    /**
     * 薪資單位
     */
    private String currency;
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
     * 審核狀態
     */
    private int isPublish;
    /**
     * 上線狀態太
     */
    private int isOnline;
    /**
     * 是否刪除
     */
    private int isDelete;
    /**
     * 發佈人
     */
    private String createBy;
    /**
     * 暱稱
     */
    private String nickName;
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
     * 是否關注該職位
     */
    private int isFavor;
    /**
     * 审核失败原因
     */
    private String approveReason;

    /**
     * 店鋪信息
     */
    private StoreVo storeVo;

    public HrPostVo(HrPost hr) {
        this.postId = hr.getPostId();
        this.postTitle = hr.getPostTitle();
        this.workLocation = hr.getWorkLocation();
        this.customizeLow = hr.getCustomizeLow();
        this.customizeHigh = hr.getCustomizeHigh();
        this.postDescription = hr.getPostDescription();
        this.mailbox = hr.getMailbox();
        this.isPublish = hr.getIsPublish();
        this.isOnline = hr.getIsOnline();
        this.isDelete = hr.getIsDelete();
        this.createBy = hr.getCreateBy();
        this.nickName = hr.getNickName();
        this.createTime = hr.getCreateTime();
        this.postFavor = hr.getPostFavor();
        this.storeId = hr.getStoreId();
        this.approveReason = hr.getApproveReason();
    }

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

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostArea() {
        return postArea;
    }

    public void setPostArea(String postArea) {
        this.postArea = postArea;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public int getIsFavor() {
        return isFavor;
    }

    public void setIsFavor(int isFavor) {
        this.isFavor = isFavor;
    }

    public String getPostAreaParent() {
        return postAreaParent;
    }

    public void setPostAreaParent(String postAreaParent) {
        this.postAreaParent = postAreaParent;
    }

    public String getApproveReason() {
        return approveReason;
    }

    public void setApproveReason(String approveReason) {
        this.approveReason = approveReason;
    }

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }

    @Override
    public String toString() {
        return "HrPostVo{" +
                "postId=" + postId +
                ", postTitle='" + postTitle + '\'' +
                ", postType='" + postType + '\'' +
                ", postAreaParent='" + postAreaParent + '\'' +
                ", postArea='" + postArea + '\'' +
                ", workLocation='" + workLocation + '\'' +
                ", salaryType='" + salaryType + '\'' +
                ", salaryRange='" + salaryRange + '\'' +
                ", currency='" + currency + '\'' +
                ", customizeLow=" + customizeLow +
                ", customizeHigh=" + customizeHigh +
                ", postDescription='" + postDescription + '\'' +
                ", mailbox='" + mailbox + '\'' +
                ", isPublish=" + isPublish +
                ", isOnline=" + isOnline +
                ", isDelete=" + isDelete +
                ", createBy='" + createBy + '\'' +
                ", nickName='" + nickName + '\'' +
                ", createTime=" + createTime +
                ", storeId=" + storeId +
                ", sort=" + sort +
                ", postFavor=" + postFavor +
                ", isFavor=" + isFavor +
                ", approveReason='" + approveReason + '\'' +
                '}';
    }
}
