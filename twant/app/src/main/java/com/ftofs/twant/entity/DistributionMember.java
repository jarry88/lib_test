package com.ftofs.twant.entity;

import androidx.navigation.PopUpToBuilder;

public class DistributionMember {
    /*
    "memberId": 872,
			"memberName": "u_087116027519",
			"nickName": "城友_aVtSHs",
			"mobileAreaCode": "00853",
			"mobile": "61316364",
			"parentMemberId": 15,
			"topMemberId": 0,
			"avatar": "",
			"parentMemberName": null,
			"parentNickName": null,
			"commissionTotalAmount": 0,
			"deep": 1
     */

    public DistributionMember() {
    }

    public String nickName;
    public String avatar;
    public double commissionTotalAmount;
    public int deep;
    public String parentNickName;
}
