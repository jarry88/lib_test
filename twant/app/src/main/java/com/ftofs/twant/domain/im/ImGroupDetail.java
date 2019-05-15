package com.ftofs.twant.domain.im;

import java.util.List;

/**
 * 群組詳情
 */
public class ImGroupDetail {
    /**
     * id	String	群组 ID，群组唯一标识符，由环信服务器生成，等同于单个用户的环信 ID。
     * name	String	群组名称，根据用户输入创建，字符串类型。
     * description	String	群组描述，根据用户输入创建，字符串类型。
     * public	Boolean	群组类型：true：公开群，false：私有群。
     * membersonly	Boolean	加入群组是否需要群主或者群管理员审批。true：是，false：否。
     * allowinvites	Boolean	是否允许群成员邀请别人加入此群。 true：允许群成员邀请人加入此群，false：只有群主才可以往群里加人。
     * maxusers	Integer	群成员上限，创建群组的时候设置，可修改。
     * affiliations_count	Integer	现有成员总数。
     * affiliations	Array	现有成员列表，包含了 owner 和 member。例如：“affiliations”:[{“owner”: “13800138001”},{“member”:“v3y0kf9arx”},{“member”:“xc6xrnbzci”}]。
     * owner	String	群主的环信 ID。例如：{“owner”: “13800138001”}。
     * member	String	群成员的环信 ID。例如：{“member”:“xc6xrnbzci”}。
     * invite_need_confirm	Boolean	邀请加群，被邀请人是否需要确认。如果是true，表示邀请加群需要被邀请人确认；如果是false，表示不需要被邀请人确认，直接将被邀请人加入群。 该字段的默认值为true。
     */
    private String id;

    private String name;

    private String description;

    private boolean membersOnly;

    private boolean allowInvites;

    private int maxusers;

    private String owner;

    private long created;

    private String custom;

    private int affiliationsCount;

    private List<Affiliations> affiliations;

    private boolean isPublic;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public boolean isMembersOnly() {
        return membersOnly;
    }

    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }

    public boolean isAllowInvites() {
        return allowInvites;
    }

    public void setAllowInvites(boolean allowInvites) {
        this.allowInvites = allowInvites;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }
    public int getMaxusers() {
        return maxusers;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getOwner() {
        return owner;
    }

    public void setCreated(long created) {
        this.created = created;
    }
    public long getCreated() {
        return created;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }
    public String getCustom() {
        return custom;
    }

    public void setAffiliationsCount(int affiliationsCount) {
        this.affiliationsCount = affiliationsCount;
    }
    public int getAffiliationsCount() {
        return affiliationsCount;
    }

    public void setAffiliations(List<Affiliations> affiliations) {
        this.affiliations = affiliations;
    }
    public List<Affiliations> getAffiliations() {
        return affiliations;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean bPublic) {
        isPublic = bPublic;
    }
}