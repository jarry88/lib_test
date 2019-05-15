package com.ftofs.twant.domain.im;

import com.ftofs.twant.vo.store.StoreVo;

/**
 * IM群
 */
public class ImGroup {
    /**
     * 群Id
     */
    private String groupId;

    /**
     * 群名
     */
    private String groupName;

    /**
     * 群主(OrgName#AppName_memberName)
     */
    private String owner;

    /**
     * 成員數
     */
    private int affiliations;

    /**
     * 修改時間
     */
    private long lastModified;

    /**
     * 類型（group)
     */
    private String type;

    /**
     * 創建時間
     */
    private long created;

    /**
     * 店鋪信息
     */
    private StoreVo storeInfo;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(int affiliations) {
        this.affiliations = affiliations;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public StoreVo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreVo storeInfo) {
        this.storeInfo = storeInfo;
    }
}
