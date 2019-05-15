package com.ftofs.twant.vo.promotion;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 搜索的优惠券活动结果实体
 * 
 * @author zxy
 * Created 2017/9/26 16:33
 */
public class SearchCouponActivityResultVo {
    /**
     * 优惠券活动总数量
     */
    private int total;
    /**
     * 优惠券活动列表（平台券和店铺券）
     */
    private List<SearchCouponActivityVo> resultList = new ArrayList<>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SearchCouponActivityVo> getResultList() {
        return resultList;
    }

    public void setResultList(List<SearchCouponActivityVo> resultList) {
        this.resultList = resultList;
    }

    @Override
    public String toString() {
        return "SearchCouponActivityResultVo{" +
                "total=" + total +
                ", resultList=" + resultList +
                '}';
    }
}
