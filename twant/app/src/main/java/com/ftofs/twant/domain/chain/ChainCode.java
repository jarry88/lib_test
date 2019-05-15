package com.ftofs.twant.domain.chain;

public class ChainCode {
    /**
     * 主键、自增
     */
    private int chainCodeId;

    /**
     * 订单Id
     */
    private int ordersId;

    /**
     * 门店Id
     */
    private int chainId;

    /**
     * 提货码
     */
    private int takeCode;

    /**
     * 使用状态
     */
    private int useState;

    public int getChainCodeId() {
        return chainCodeId;
    }

    public void setChainCodeId(int chainCodeId) {
        this.chainCodeId = chainCodeId;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public int getTakeCode() {
        return takeCode;
    }

    public void setTakeCode(int takeCode) {
        this.takeCode = takeCode;
    }

    public int getUseState() {
        return useState;
    }

    public void setUseState(int useState) {
        this.useState = useState;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

}
