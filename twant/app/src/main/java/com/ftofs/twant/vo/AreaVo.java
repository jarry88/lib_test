package com.ftofs.twant.vo;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 地区Vo
 *
 * @author hbj
 * Created 2017/4/13 16:14
 */
public class AreaVo {
    /**
     * 地区编号</br>
     * 主键、自增
     */
    private int areaId;
    /**
     * 地区名称
     */
    private String areaName;
    /**
     * 上级地区ID
     */
    private int areaParentId;
    /**
     * 深度<br/>
     * 一级地区深度为1，二级深度为2，依次类推。。。
     */
    private int areaDeep;
    /**
     * 所在大区</br>
     * 如华北、华中等
     */
    private String areaRegion;
    /**
     * 父级地区名称
     */
    private String areaParentName;

    /**
     * 地区编号
     */
    private String areaCode ;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getAreaParentId() {
        return areaParentId;
    }

    public void setAreaParentId(int areaParentId) {
        this.areaParentId = areaParentId;
    }

    public int getAreaDeep() {
        return areaDeep;
    }

    public void setAreaDeep(int areaDeep) {
        this.areaDeep = areaDeep;
    }

    public String getAreaRegion() {
        return areaRegion;
    }

    public void setAreaRegion(String areaRegion) {
        this.areaRegion = areaRegion;
    }

    public String getAreaParentName() {
        return areaParentName;
    }

    public void setAreaParentName(String areaParentName) {
        this.areaParentName = areaParentName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return "AreaVo{" +
                "areaId=" + areaId +
                ", areaName='" + areaName + '\'' +
                ", areaParentId=" + areaParentId +
                ", areaDeep=" + areaDeep +
                ", areaRegion='" + areaRegion + '\'' +
                ", areaParentName='" + areaParentName + '\'' +
                ", areaCode=" + areaCode +
                '}';
    }
}
