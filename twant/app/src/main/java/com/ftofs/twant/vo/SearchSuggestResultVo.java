package com.ftofs.twant.vo;

import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 搜索提示词返回结果Vo
 *
 * @author dqw
 * Created 2017/6/15 10:55
 */
public class SearchSuggestResultVo {
    private int total;
    private List<SuggestVo> suggestList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SuggestVo> getSuggestList() {
        return suggestList;
    }

    public void setSuggestList(List<SuggestVo> suggestList) {
        this.suggestList = suggestList;
    }

    @Override
    public String toString() {
        return "SearchSuggestResultVo{" +
                "total=" + total +
                ", suggestList=" + suggestList +
                '}';
    }
}

