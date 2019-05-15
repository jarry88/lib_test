package com.ftofs.twant.vo.search;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * Elastic聚合
 *
 * @author dqw
 * Created 2017/9/27 17:06
 */
public class ElasticAggVo {
    /**
     * 聚合字段值
     */
    private int key;
    /**
     * 数量
     */
    private int count;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ElasticAggVo{" +
                "key=" + key +
                ", count=" + count +
                '}';
    }
}

