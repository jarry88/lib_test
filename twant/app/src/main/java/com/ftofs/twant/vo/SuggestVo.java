package com.ftofs.twant.vo;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 搜索提示词Vo
 *
 * @author dqw
 * Created 2017/6/15 10:55
 */
public class SuggestVo {
    private String suggest;
    private int order;

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "MemberSuggestVo{" +
                "suggest='" + suggest + '\'' +
                ", order=" + order +
                '}';
    }
}

