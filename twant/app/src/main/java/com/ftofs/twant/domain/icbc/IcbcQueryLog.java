package com.ftofs.twant.domain.icbc;

public class IcbcQueryLog {
    /**
     * 支付單號(對應paySn)
     */
    private String ordersSn;
    /**
     * 通知信息
     */
    private String queryResult;
    /**
     * 驗證結果 1支付成功/0支付失敗
     */
    private int verifySign;

    public String getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(String ordersSn) {
        this.ordersSn = ordersSn;
    }

    public String getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
    }

    public int getVerifySign() {
        return verifySign;
    }

    public void setVerifySign(int verifySign) {
        this.verifySign = verifySign;
    }

    @Override
    public String toString() {
        return "IcbcQueryLog{" +
                "ordersSn='" + ordersSn + '\'' +
                ", queryResult='" + queryResult + '\'' +
                ", verifySign=" + verifySign +
                '}';
    }
}
