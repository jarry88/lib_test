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
        if (TangramCellType.CAROUSEL_CELL.equals(cell.stringType)) {
            Object data = cell.optParam("data");
            if (data != null) {
                WebSliderItem webSliderItem = (WebSliderItem) data;
                SLog.info("%s", webSliderItem.toString());

                String linkType = webSliderItem.linkType;

                switch (linkType) {
                    case "none":
                        // 无操作
                        break;
                    case "url":
                        // 外部鏈接
                        Util.startFragment(ExplorerFragment.newInstance(webSliderItem.linkValue, true));
                        break;
                    case "keyword":
                        // 关键字
                        String keyword = webSliderItem.linkValue;
                        Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                                EasyJSONObject.generate("keyword", keyword).toString()));
                        break;
                    case "goods":
                        // 產品
                        int commonId = Integer.parseInt(webSliderItem.linkValue);
                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                        break;
                    case "store":
                        // 店铺
                        int storeId = Integer.parseInt(webSliderItem.linkValue);
                        Util.startFragment(ShopMainFragment.newInstance(storeId));
                        break;
                    case "category":
                        // 產品搜索结果页(分类)
                        String cat = webSliderItem.linkValue;
                        Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                                EasyJSONObject.generate("cat", cat).toString()));
                        break;
                    case "brandList":
                        // 品牌列表
                        break;
                    case "voucherCenter":
                        // 领券中心
                        break;
                    case "activityUrl":
                        Util.startFragment(H5GameFragment.newInstance(webSliderItem.linkValue, true));
                        break;
                    case "postId":
                        int postId = Integer.parseInt(webSliderItem.linkValue);
                        Util.startFragment(PostDetailFragment.newInstance(postId));
                        break;
                    case "shopping":
                        Util.startFragment(ShoppingSessionFragment.newInstance());
                        break;
                    case "wantPost":
                        MainFragment mainFragment = MainFragment.getInstance();
                        if (mainFragment == null) {
                            ToastUtil.error(TwantApplication.getInstance(), "MainFragment為空");
                            return;
                        }
                        mainFragment.showHideFragment(MainFragment.CIRCLE_FRAGMENT);
                        break;
                    default:
                        break;
                }
            }
        } else if (TangramCellType.STORE_ITEM_CELL.equals(cell.stringType)) {
            Object data = cell.optParam("data");
            if (data != null) {
                StoreItem storeItem = (StoreItem) data;
                Util.startFragment(ShopMainFragment.newInstance(storeItem.storeId));
            }
        }
    }
}
