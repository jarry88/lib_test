package com.ftofs.twant.vo.goods;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 规格
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:12
 */
public class SpecJsonVo {
    private int specId=0;
    private String specName="";
    private List<SpecValueListVo> specValueList=new ArrayList<>();

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public List<SpecValueListVo> getSpecValueList() {
        return specValueList;
    }

    public void setSpecValueList(List<SpecValueListVo> specValueList) {
        this.specValueList = specValueList;
    }

    @Override
    public String toString() {
        return "SpecJsonVo{" +
                "specId='" + specId + '\'' +
                ", specName='" + specName + '\'' +
                ", specValueList=" + specValueList +
                '}';
    }
}
