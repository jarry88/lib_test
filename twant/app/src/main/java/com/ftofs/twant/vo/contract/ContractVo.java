package com.ftofs.twant.vo.contract;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 消费保障
 *
 * @author sjz
 * Created 2017/4/17 11:50
 */
public class ContractVo {
    private int ctId;
    private int ctStoreid;
    private String ctStorename;
    private int ctItemid;
    private int ctAuditstate;
    private String ctAuditstateStr;
    private int ctJoinstate;
    private String ctJoinstateStr;
    private String ctCost;//保证金余额
    private int ctClosestate;
    private String ctClosestateStr;
    private int ctQuitstate;
    private String ctQuitstateStr;
    private String ctState;
    private String ctItemname;
    private String cause;
    
    private String desc;
    private String icon;
    private String ctiCost;//所需保证金
    private String iconUrl;
    
    private String ctDescurl;

    public int getCtId() {
        return ctId;
    }

    public void setCtId(int ctId) {
        this.ctId = ctId;
    }

    public int getCtStoreid() {
        return ctStoreid;
    }

    public void setCtStoreid(int ctStoreid) {
        this.ctStoreid = ctStoreid;
    }

    public String getCtStorename() {
        return ctStorename;
    }

    public void setCtStorename(String ctStorename) {
        this.ctStorename = ctStorename;
    }

    public int getCtItemid() {
        return ctItemid;
    }

    public void setCtItemid(int ctItemid) {
        this.ctItemid = ctItemid;
    }

    public int getCtAuditstate() {
        return ctAuditstate;
    }

    public void setCtAuditstate(int ctAuditstate) {
        this.ctAuditstate = ctAuditstate;
    }

    public String getCtAuditstateStr() {
        return ctAuditstateStr;
    }

    public void setCtAuditstateStr(String ctAuditstateStr) {
        this.ctAuditstateStr = ctAuditstateStr;
    }

    public int getCtJoinstate() {
        return ctJoinstate;
    }

    public void setCtJoinstate(int ctJoinstate) {
        this.ctJoinstate = ctJoinstate;
    }

    public String getCtJoinstateStr() {
        return ctJoinstateStr;
    }

    public void setCtJoinstateStr(String ctJoinstateStr) {
        this.ctJoinstateStr = ctJoinstateStr;
    }

    public String getCtCost() {
        return ctCost;
    }

    public void setCtCost(String ctCost) {
        this.ctCost = ctCost;
    }

    public int getCtClosestate() {
        return ctClosestate;
    }

    public void setCtClosestate(int ctClosestate) {
        this.ctClosestate = ctClosestate;
    }

    public String getCtClosestateStr() {
        return ctClosestateStr;
    }

    public void setCtClosestateStr(String ctClosestateStr) {
        this.ctClosestateStr = ctClosestateStr;
    }

    public int getCtQuitstate() {
        return ctQuitstate;
    }

    public void setCtQuitstate(int ctQuitstate) {
        this.ctQuitstate = ctQuitstate;
    }

    public String getCtQuitstateStr() {
        return ctQuitstateStr;
    }

    public void setCtQuitstateStr(String ctQuitstateStr) {
        this.ctQuitstateStr = ctQuitstateStr;
    }

    public String getCtState() {
        return ctState;
    }

    public void setCtState(String ctState) {
        this.ctState = ctState;
    }

    public String getCtItemname() {
        return ctItemname;
    }

    public void setCtItemname(String ctItemname) {
        this.ctItemname = ctItemname;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCtiCost() {
        return ctiCost;
    }

    public void setCtiCost(String ctiCost) {
        this.ctiCost = ctiCost;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCtDescurl() {
        return ctDescurl;
    }

    public void setCtDescurl(String ctDescurl) {
        this.ctDescurl = ctDescurl;
    }

    @Override
    public String toString() {
        return "ContractVo{" +
                "ctId=" + ctId +
                ", ctStoreid=" + ctStoreid +
                ", ctStorename='" + ctStorename + '\'' +
                ", ctItemid=" + ctItemid +
                ", ctAuditstate=" + ctAuditstate +
                ", ctAuditstateStr='" + ctAuditstateStr + '\'' +
                ", ctJoinstate=" + ctJoinstate +
                ", ctJoinstateStr='" + ctJoinstateStr + '\'' +
                ", ctCost='" + ctCost + '\'' +
                ", ctClosestate=" + ctClosestate +
                ", ctClosestateStr='" + ctClosestateStr + '\'' +
                ", ctQuitstate=" + ctQuitstate +
                ", ctQuitstateStr='" + ctQuitstateStr + '\'' +
                ", ctState='" + ctState + '\'' +
                ", ctItemname='" + ctItemname + '\'' +
                ", cause='" + cause + '\'' +
                ", desc='" + desc + '\'' +
                ", icon='" + icon + '\'' +
                ", ctiCost='" + ctiCost + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", ctDescurl='" + ctDescurl + '\'' +
                '}';
    }
}
