package com.ftofs.twant.vo.store;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 店铺客服Vo
 *
 * @author dqw
 * Created 2017/4/17 11:56
 */
public class ServiceVo {
    /**
     * 客服名称
     */
    private String name;
    /**
     * 客服工具</br>
     * 1-QQ 2-旺旺
     */
    private int type;
    /**
     * 客服账号
     * Modify By yangjian 2018/12/5 14:21
     */
    private String num;
    /**
     * 客服会员名称
     * Modify By yangjian 2018/12/5 14:21
     */
    private String memberName;
    /**
     * 客服头像
     *  Modify By yangjian 2018/12/5 14:21
     */
    private String avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "ServiceVo{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", num='" + num + '\'' +
                ", memberName='" + memberName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}

