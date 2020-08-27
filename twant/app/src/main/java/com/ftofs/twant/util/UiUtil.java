package com.ftofs.twant.util;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
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
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.NewShoppingSpecialFragment;
import com.zhouwei.mzbanner.MZBannerView;

import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

public class UiUtil {

    public static void toPriceUI(TextView textView,int firstSize) {
        toPriceUI(textView,firstSize,0,1);
    }
    public static void toPriceUI(TextView leftTextView,int firstSize,int start,int end) {
        if (leftTextView == null) {
            return;
        }
        String string = leftTextView.getText().toString();
        if (StringUtil.isEmpty(string)) {
            return;
        }
        if (firstSize == 0) {
            firstSize = 11;
        }
        // SLog.info(string);
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
//        if (StringUtil.isArrayEmpty(webSliderItemList)) {
//            return;
//        }
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
                    case "shoppingZone":
                        //購物新專場
                        int zoneId = Integer.parseInt(webSliderItem.linkValue);
                        Util.startFragment(NewShoppingSpecialFragment.newInstance(zoneId));
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
    public static void moveToMiddle(RecyclerView recyclerView, int position) {
        //先从RecyclerView的LayoutManager中获取当前第一项和最后一项的Position
        int firstItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int lastItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        //中间位置
        int middle = (firstItem + lastItem)/2;
        // 取绝对值，index下标是当前的位置和中间位置的差，下标为index的view的top就是需要滑动的距离
        int index = (position - middle) >= 0 ? position - middle : -(position - middle);
        //左侧列表一共有getChildCount个Item，如果>这个值会返回null，程序崩溃，如果>getChildCount直接滑到指定位置,或者,都一样啦
        if (index >= recyclerView.getChildCount()) {
            recyclerView.scrollToPosition(position);
        } else {
            //如果当前位置在中间位置上面，往下移动，这里为了防止越界
            if(position < middle) {
                recyclerView.scrollBy(0, -recyclerView.getChildAt(index).getTop());
                // 在中间位置的下面，往上移动
            } else {
                recyclerView.scrollBy(0, recyclerView.getChildAt(index).getTop());
            }
        }
    }
    public static Drawable tvButtonBackGround(TextView textView,int colorBg) {
        ShapeDrawable drawable = new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {

            }
        });
//        drawable.add;
        return null;
    }

    public static void toConsultUI(TextView textView) {
        textView.setText("詢價");
    }
}
