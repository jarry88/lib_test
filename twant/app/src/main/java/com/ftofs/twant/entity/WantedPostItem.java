package com.ftofs.twant.entity;

/**
 * 招聘貼
 */
public class WantedPostItem extends MyFollowItem {
    public int postId;
    public String postTitle;
    public String postType;  // 兼職
    public String salaryType;  // 月薪
    public String salaryRange;
    public String currency;
    public String postArea; // 澳门 氹仔
    public String postDescription;
    public String mailbox;
    public int isFavor;
    public boolean isJobDescExpanded;  // 職位詳情是否已經展開
    public boolean isCompanyInfoExpand;//是否需要展開公司信息

}
