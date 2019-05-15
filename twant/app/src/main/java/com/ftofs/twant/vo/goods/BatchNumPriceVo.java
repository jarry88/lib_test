package com.ftofs.twant.vo.goods;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 批发阶梯结果
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:04
 */
public class BatchNumPriceVo {
    private String num;
    private String price;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BatchNumPriceVo{" +
                "num='" + num + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
