package com.ftofs.twant.domain.icbc;

public class IcbcNotifyLog {
    /**
     * 支付單號(對應paySn)
     */
    private String ordersSn;
    /**
     * 通知信息
     */
    private String notifyResult;

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

    public String getNotifyResult() {
        return notifyResult;
    }

    public void setNotifyResult(String notifyResult) {
        this.notifyResult = notifyResult;
    }

    public int getVerifySign() {
        return verifySign;
    }

    public void setVerifySign(int verifySign) {
        this.verifySign = verifySign;
    }

    @Override
    public String toString() {
        return "IcbcNotifyLog{" +
                "ordersSn='" + ordersSn + '\'' +
                ", notifyResult='" + notifyResult + '\'' +
                ", verifySign=" + verifySign +
                '}';
    }
}
