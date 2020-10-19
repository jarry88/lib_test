package com.ftofs.twant.entity;

public class DistributionWithdrawRecord {
    public static final int STATE_TO_BE_AUDIT = 0;
    public static final int STATE_PASS = 1;
    public static final int STATE_NOT_PASS = 2;
    public static final int STATE_PAID = 3;

    /*
    "billSn": 2020101401,
	"memberId": 15,
	"marketingCommissionAmount": 15.00,
	"createTime": "2020-10-14 16:45:28",
	"adminTime": null,
	"payTime": null,
	"billState": 0,
     */
    public DistributionWithdrawRecord() {
    }

    public boolean expanded; // 是否展开详情
    public long billSn;
    public double marketingCommissionAmount;
    public int billState; // 結算狀態 0運營待審核 1運營審核通過 2運營審核不通過 3財務已付款
    public String createTime; // 提現發起時間
    public String adminTime;
    public String payTime;
}
