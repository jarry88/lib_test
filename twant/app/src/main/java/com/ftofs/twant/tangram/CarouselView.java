package com.ftofs.twant.tangram;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.Goods;
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
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.BannerViewHolder;
import com.ftofs.twant.widget.RoundedDataImageView;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class CarouselView extends LinearLayout implements ITangramViewLifeCycle {
    Context context;
    MZBannerView bannerView;
    List<WebSliderItem> webSliderItemList = new ArrayList<>();


    public CarouselView(Context context) {
        this(context, null);
    }

    public CarouselView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        View contentView = LayoutInflater.from(context).inflate(R.layout.tangram_layout_home_carousel_view, this, false);
        bannerView = contentView.findViewById(R.id.banner_view);

        UiUtil.addBannerPageClick(bannerView,webSliderItemList);

        loadCarousel();


        addView(contentView);
    }

    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
    }

    @Override
    public void postBindView(BaseCell cell) {

    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }

    /**
     * 加載輪播圖片
     */
    private void loadCarousel() {
        SLog.info("___loadCarousel");
        Api.getUI(Api.PATH_HOME_CAROUSEL, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(getContext(), e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                try {
                    if (ToastUtil.checkError(context, responseObj)) {
                        return;
                    }

                    EasyJSONArray itemList = responseObj.getSafeArray("datas.webSliderItem");
                    SLog.info(itemList.toString());
                    webSliderItemList.clear();
                    for (Object object : itemList) {
                        EasyJSONObject itemObj = (EasyJSONObject) object;

                        String goodsCommonsStr;
                        Object goodsCommons = itemObj.get("goodsCommons");
                        if (Util.isJsonNull(goodsCommons)) {
                            goodsCommonsStr = "[]";
                        } else if ("null".equals(goodsCommons.toString())) {
                            goodsCommonsStr = "[]";
                        } else {
                            goodsCommonsStr = goodsCommons.toString();
                        }

                        WebSliderItem webSliderItem = new WebSliderItem(
                                itemObj.getSafeString("image"),
                                itemObj.getSafeString("linkType"),
                                itemObj.getSafeString("linkValue"),
                                itemObj.getSafeString("goodsIds"),
                                goodsCommonsStr);
                        webSliderItemList.add(webSliderItem);
                    }

                    // 设置数据
                    bannerView.setPages(webSliderItemList, (MZHolderCreator<BannerViewHolder>) () -> new BannerViewHolder(webSliderItemList));
                    LinearLayout linearLayout =bannerView.getIndicatorContainer();
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();;
                    params.setMarginStart(Util.dip2px(getContext(),10));
                    linearLayout.setLayoutParams(params);
                    bannerView.setIndicatorAlign(MZBannerView.IndicatorAlign.LEFT);
                    int heightPadding = Util.getScreenDimension(getContext()).first * 9 / 16 - Util.dip2px(getContext(), 12);
                    bannerView.setIndicatorPadding(0,heightPadding,0,0);

                    bannerView.start();
                    bannerView.setDelayedTime(2500);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
}
