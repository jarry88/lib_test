package com.ftofs.twant.entity.cart;



public class SkuStatus extends BaseStatus {
    public SpuStatus parent;

    private int goodsId; // Sku Id
    private int count;  // 數量
    private float price;  // 價錢

    @Override
    public void changeCheckStatus(boolean checked, int phrase) {
        super.changeCheckStatus(checked, phrase);

        if (phrase == PHRASE_CAPTURE) {

        } else if (phrase == PHRASE_TARGET) {
            // 狀態向上傳播
            parent.changeCheckStatus(checked, PHRASE_BUBBLE);
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
}
