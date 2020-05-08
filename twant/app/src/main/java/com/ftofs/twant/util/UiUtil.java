package com.ftofs.twant.util;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.fragment.ExplorerFragment;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.PostDetailFragment;
import com.ftofs.twant.fragment.SearchResultFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.fragment.ShoppingSessionFragment;
import com.ftofs.twant.fragment.ShoppingSpecialFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.view.BannerViewHolder;
import com.zhouwei.mzbanner.MZBannerView;

import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

public class UiUtil {

    public static void toPriceUI(TextView leftTextView,int firstSize) {
        toPriceUI(leftTextView,firstSize,0,1);
    }
    public static void toPriceUI(TextView leftTextView,int firstSize,int start,int end) {
        if (leftTextView == null) {
            return;
        }
        if (firstSize == 0) {
            firstSize = 11;
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(leftTextView.getText().toString());
//        //设置字体颜色为红色
//        ForegroundColorSpan good_red = new ForegroundColorSpan(getResources().getColor(R.color.goods_red));
//        //设置字体颜色为灰色
//        ForegroundColorSpan good_gray = new ForegroundColorSpan(getResources().getColor(R.color.col909090));
//        //改变第0-3个字体颜色
//        spannable.setSpan(good_red, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //改变第4-之后所有的字体颜色（这里一定要注意范围，否则会造成越界）
//        spannable.setSpan(good_gray, 4, test.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //改变第0个字体大小
        spannable.setSpan(new AbsoluteSizeSpan(firstSize,true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        leftTextView.setText(spannable);
    }

    public static void addBannerPageClick(MZBannerView bannerView, List<WebSliderItem> webSliderItemList) {
        if (StringUtil.isArrayEmpty(webSliderItemList)) {
            return;
        }
        bannerView.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                WebSliderItem webSliderItem = webSliderItemList.get(i);
                String linkType = webSliderItem.linkType;
                SLog.info("i = %d, linkType[%s]", i, linkType);
                if (StringUtil.isEmpty(linkType)) {
                    return;
                }

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
                        int commonId = Integer.valueOf(webSliderItem.linkValue);
                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                        break;
                    case "store":
                        // 店铺
                        int storeId = Integer.valueOf(webSliderItem.linkValue);
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
                        int postId = Integer.valueOf(webSliderItem.linkValue);
                        Util.startFragment(PostDetailFragment.newInstance(postId));
                        break;
                    case "shopping":
                        Util.startFragment(ShoppingSessionFragment.newInstance());
                        break;
                    case "shoppingZone":
                        //購物新專場
                        int zoneId = Integer.valueOf(webSliderItem.linkValue);
                        Util.startFragment(ShoppingSpecialFragment.newInstance(zoneId));
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
        });
    }
}
