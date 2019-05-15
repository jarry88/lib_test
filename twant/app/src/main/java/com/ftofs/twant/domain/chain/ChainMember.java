package com.ftofs.twant.domain.chain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ChainMember implements Serializable,Cloneable {
    /**
     * 自增编码
     */
    private int id;

    /**
     * 门店ID
     */
    private int chainId = 0;

    /**
     * 门店名称
     */
    private String chainName = "";

    /**
     * 会员ID
     */
    private int memberId = 0;

    /**
     * 添加时间
     */
    private Timestamp addTime;

    /**
     * 会员标签ID，例如,1,2,4,6,
     */
    private String tagId = "";

    /**
     * 标签名称列表
     */
    private List<String> tagNameList = new ArrayList<>();

    /**
     * 会员标签ID列表
     */
    private List<Integer> tagIdList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public List<String> getTagNameList() {
        return tagNameList;
    }

    public void setTagNameList(List<String> tagNameList) {
        this.tagNameList = tagNameList;
    }

    public List<Integer> getTagIdList() {
        return tagIdList;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ChainMember{" +
                "id=" + id +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", memberId=" + memberId +
                ", addTime=" + addTime +
                ", tagId='" + tagId + '\'' +
                ", tagNameList=" + tagNameList +
                ", tagIdList=" + tagIdList +
                '}';
    }
}
