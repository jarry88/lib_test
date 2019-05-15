package com.ftofs.twant.domain.im;

public class ImMemberStatus {
    /**
     * 會員狀態
     */
    private String memberNameStatus;
    /**
     * 離線消息個數
     * @return
     */
    private int offlineMsgCount;

    public String getMemberNameStatus() {
        return memberNameStatus;
    }

    public void setMemberNameStatus(String memberNameStatus) {
        this.memberNameStatus = memberNameStatus;
    }

    public int getOfflineMsgCount() {
        return offlineMsgCount;
    }

    public void setOfflineMsgCount(int offlineMsgCount) {
        this.offlineMsgCount = offlineMsgCount;
    }
}
