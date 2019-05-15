package com.ftofs.twant.vo.seller;

import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 商家菜单状态
 *
 * @author dqw
 * Created 2017/4/17 11:55
 */
public class SellerMenuStateVo {
    private String currentMainMenu;
    private String currentMenuTitle;
    private String currentSubMenu;
    private String currentPath;
    private List<String> BreadCrumbList;

    public String getCurrentMainMenu() {
        return currentMainMenu;
    }

    public void setCurrentMainMenu(String currentMainMenu) {
        this.currentMainMenu = currentMainMenu;
    }

    public String getCurrentMenuTitle() {
        return currentMenuTitle;
    }

    public void setCurrentMenuTitle(String currentMenuTitle) {
        this.currentMenuTitle = currentMenuTitle;
    }

    public String getCurrentSubMenu() {
        return currentSubMenu;
    }

    public void setCurrentSubMenu(String currentSubMenu) {
        this.currentSubMenu = currentSubMenu;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public List<String> getBreadCrumbList() {
        return BreadCrumbList;
    }

    public void setBreadCrumbList(List<String> breadCrumbList) {
        BreadCrumbList = breadCrumbList;
    }

    @Override
    public String toString() {
        return "SellerMenuStateVo{" +
                "currentMainMenu='" + currentMainMenu + '\'' +
                ", currentMenuTitle='" + currentMenuTitle + '\'' +
                ", currentSubMenu='" + currentSubMenu + '\'' +
                ", currentPath='" + currentPath + '\'' +
                ", BreadCrumbList=" + BreadCrumbList +
                '}';
    }
}

