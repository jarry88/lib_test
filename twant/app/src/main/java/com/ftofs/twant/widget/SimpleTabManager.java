package com.ftofs.twant.widget;

import android.view.View;

import com.ftofs.twant.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * SimpleTabButton組的管理類
 * 注意：實現 View.OnClickListener::onClick(View) 方法時，須在里面先調用 onSelect(View) 方法
 * @author zwm
 */
public abstract class SimpleTabManager implements View.OnClickListener {
    /**
     * 當前選中的Tab的索引，默認從0開始
     */
    int selectedIndex;
    private List<SimpleTabButton> simpleTabButtonList;

    /**
     * 構造方法
     * @param selectedIndex 默認選中第幾個，從0開始
     */
    public SimpleTabManager(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        simpleTabButtonList = new ArrayList<>();
    }

    /**
     * 是否重復點擊
     * @param view
     * @return
     */
    public boolean onSelect(View view) {
        SimpleTabButton button = (SimpleTabButton) view;
        int count = simpleTabButtonList.size();
        for (int i = 0; i < count; ++i) {
            SimpleTabButton simpleTabButton = simpleTabButtonList.get(i);
            if (simpleTabButton == button) {
                if (i == selectedIndex) {
                    return true;
                }
                simpleTabButtonList.get(selectedIndex).setStatus(Constant.STATUS_UNSELECTED);
                button.setStatus(Constant.STATUS_SELECTED);

                selectedIndex = i;
                return false;
            }
        }

        // 理論上來不到這里
        return true;
    }

    public void add(View view) {
        SimpleTabButton simpleTabButton = (SimpleTabButton) view;

        if (simpleTabButtonList.size() == selectedIndex) {
            simpleTabButton.setStatus(Constant.STATUS_SELECTED);
        }
        simpleTabButton.setOnClickListener(this);
        simpleTabButtonList.add(simpleTabButton);
    }
}
