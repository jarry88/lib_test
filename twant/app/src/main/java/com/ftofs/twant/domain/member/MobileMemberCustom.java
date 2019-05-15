package com.ftofs.twant.domain.member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MobileMemberCustom implements Serializable {
    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 内容
     */
    private String content;

    /**
     * 内容list
     */
    private List<Integer> contentList = new ArrayList<Integer>();

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Integer> getContentList() {
        return contentList;
    }

    public void setContentList(List<Integer> contentList) {
        this.contentList = contentList;
    }

    @Override
    public String toString() {
        return "MemberCustom{" +
                "memberId=" + memberId +
                ", content='" + content + '\'' +
                '}';
    }
}
