package com.ftofs.twant.vo.activity;

import java.util.List;
/**
 * copyright  Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 会员签到vo
 * 
 * @author cj
 * Created 2017-5-26 上午 10:21
 */
public class ActivityMemberSigninVo {

    /**
     * 签到标题
     */
    private String title ;
    /**
     * 签到说明
     */
    private String content ;

    /**
     * 签到规则
     */
    private List<ActivityMemberSigninRuleVo> ruleList ;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ActivityMemberSigninRuleVo> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<ActivityMemberSigninRuleVo> ruleList) {
        this.ruleList = ruleList;
    }

    @Override
    public String toString() {
        return "ActivityMemberSigninVo{" +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", ruleList=" + ruleList +
                '}';
    }
}
