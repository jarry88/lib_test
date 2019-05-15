package com.ftofs.twant.vo;

import com.ftofs.twant.domain.Area;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 区域以及地区
 * 
 * @author shopnc.feng
 * Created 2017/4/13 13:56
 */
public class AreaRegionAndAreaVo {
    private String region = "";
    private List<Area> areaList = new ArrayList<>();

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    @Override
    public String toString() {
        return "AreaRegionAndAreaVo{" +
                "region='" + region + '\'' +
                ", areaList=" + areaList +
                '}';
    }
}
