package com.ftofs.twant.entity;

/**
 * 客服工作人員
 * @author zwm
 */
public class CustomerServiceStaff {
    public int storeId;
    public int staffId;
    public String staffName;
    public String memberName;  // 好友聊天時，用這個
    public String imName; // 跟店員聊天時，用這個(相當于臨時聊天)
    public String avatar;
    public String welcomeMessage;
    public int staffType;
    public boolean showWelcomeMessageAnimation; // 是否需要顯示歡迎語動畫
}
