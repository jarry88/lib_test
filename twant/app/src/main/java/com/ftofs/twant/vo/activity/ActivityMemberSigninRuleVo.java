package com.ftofs.twant.vo.activity;

/**
 * copyright  Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 会员签到规则vo
 * 
 * @author cj
 * Created 2017-5-26 上午 10:21
 */
public class ActivityMemberSigninRuleVo {

    /**
     * 连签天数
     */
    private int ruleDays ;
    /**
     * 签到点数
     */
    private int rulePoints ;

    public int getRuleDays() {
        return ruleDays;
    }

    public void setRuleDays(int ruleDays) {
        this.ruleDays = ruleDays;
    }

    public int getRulePoints() {
        return rulePoints;
    }

    public void setRulePoints(int rulePoints) {
        this.rulePoints = rulePoints;
    }

    @Override
    public String toString() {
        return "ActivityMemberSigninRuleVo{" +
                "ruleDays=" + ruleDays +
                ", rulePoints=" + rulePoints +
                '}';
    }
}
