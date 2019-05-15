package com.ftofs.twant.domain.chain;

import java.io.Serializable;

public class ChainMemberTag implements Serializable,Cloneable {
    /**
     * 标签ID
     */
    private int tagId;

    /**
     * 门店ID
     */
    private int chainId = 0;

    /**
     * 门店名称
     */
    private String chainName = "";

    /**
     * 标签名称
     */
    private String tagName = "";

    /**
     * 标签排序
     */
    private int tagSort = 0;

    /**
     * 标签下会员数
     */
    private int memberCount = 0;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagSort() {
        return tagSort;
    }

    public void setTagSort(int tagSort) {
        this.tagSort = tagSort;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ChainMemberTag{" +
                "tagId=" + tagId +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", tagName='" + tagName + '\'' +
                ", tagSort=" + tagSort +
                ", memberCount=" + memberCount +
                '}';
    }
}
