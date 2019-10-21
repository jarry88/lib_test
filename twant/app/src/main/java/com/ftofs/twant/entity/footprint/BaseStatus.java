package com.ftofs.twant.entity.footprint;


import com.ftofs.twant.R;
import com.ftofs.twant.widget.ScaledButton;

/**
 * 【我的足跡】狀態模型的基類
 * @author zwm
 */
public class BaseStatus {
    /**
     * 狀態改變的傳播 仿照
     * DOM事件三个阶段
     * 当一个DOM事件被触发时，它不仅仅只是单纯地在本身对象上触发一次，而是会经历三个不同的阶段：
     *
     * 捕获阶段：先由文档的根节点document往事件触发对象，从外向内捕获事件对象；
     * 目标阶段：到达目标事件位置（事发地），触发事件；
     * 冒泡阶段：再从目标事件位置往文档的根节点方向回溯，从内向外冒泡事件对象。
     */
    public static final int PHRASE_CAPTURE = 1;  // 狀態傳播方向 total => store => spu => sku
    public static final int PHRASE_TARGET = 2;   // 自己處理，然后，雙向傳播
    public static final int PHRASE_BUBBLE = 3;   // 狀態傳播方向 sku => spu => store => total

    protected ScaledButton radio;
    protected boolean checked;

    /**
     * 設置是否選中的狀態
     * @param checked
     * @param phrase 狀態傳播階段
     */
    public void changeCheckStatus(boolean checked, int phrase) {
        this.checked = checked;
        if (radio != null) {
            if (checked) {
                radio.setIconResource(R.drawable.icon_checked);
            } else {
                radio.setIconResource(R.drawable.icon_cart_item_unchecked);
            }
        }
    }

    /**
     * 切換是否選中的狀態
     * @param phrase 狀態傳播階段
     */
    public void switchCheckStatus(int phrase) {
        changeCheckStatus(!isChecked(), phrase);
    }

    public ScaledButton getRadio() {
        return radio;
    }

    public void setRadio(ScaledButton radio) {
        this.radio = radio;
    }

    public boolean isChecked() {
        return checked;
    }
}
