package com.ftofs.twant.tangram;

import android.view.View;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.constant.TangramCellType;
import com.ftofs.twant.entity.StoreItem;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.fragment.ExplorerFragment;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.PostDetailFragment;
import com.ftofs.twant.fragment.SearchResultFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.fragment.ShoppingSessionFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.SimpleClickSupport;

import cn.snailpad.easyjson.EasyJSONObject;

public class TangramClickSupport extends SimpleClickSupport {
    public TangramClickSupport() {
        setOptimizedMode(true);
    }

    @Override
    public void defaultClick(View targetView, BaseCell cell, int eventType) {
        SLog.info("stringType[%s], pos[%d], position[%d]",
                cell.stringType, cell.pos, cell.position);
        if (TangramCellType.STORE_ITEM_CELL.equals(cell.stringType)) {
            Object data = cell.optParam("data");
            if (data != null) {
                StoreItem storeItem = (StoreItem) data;
                Util.startFragment(ShopMainFragment.newInstance(storeItem.storeId));
            }
        }
    }
}
