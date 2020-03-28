package com.ftofs.twant.tangram;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.SearchPostParams;
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
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.RoundedDataImageView;
import com.orhanobut.hawk.Hawk;
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

        bannerView.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                WebSliderItem webSliderItem = webSliderItemList.get(i);
                String linkType = webSliderItem.linkType;
                SLog.info("i = %d, linkType[%s]", i, linkType);

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

    public static class BannerViewHolder implements MZViewHolder<WebSliderItem> {
        private ImageView mImageView;

        public static final int GOODS_IMAGE_COUNT = 3;
        ImageView imgDesktop;
        RoundedDataImageView[] goodsImageArr = new RoundedDataImageView[GOODS_IMAGE_COUNT];
        TextView[] goodsPriceArr = new TextView[GOODS_IMAGE_COUNT];

        List<WebSliderItem> webSliderItemList;

        public BannerViewHolder(List<WebSliderItem> webSliderItemList) {
            this.webSliderItemList = webSliderItemList;
        }

        public void setGoodsImageVisibility(int visibility,int count) {
            for (int i = 0; i < count; ++i) {
                goodsImageArr[i].setVisibility(visibility);
                goodsPriceArr[i].setVisibility(visibility);
            }
        }

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.carousel_banner_item,null);
            mImageView = view.findViewById(R.id.img_banner);

            imgDesktop = view.findViewById(R.id.img_goods_desktop);
            goodsImageArr[0] = view.findViewById(R.id.goods_image1);
            goodsImageArr[1] = view.findViewById(R.id.goods_image2);
            goodsImageArr[2] = view.findViewById(R.id.goods_image3);

            goodsPriceArr[0] = view.findViewById(R.id.tv_goods_price1);
            goodsPriceArr[1] = view.findViewById(R.id.tv_goods_price2);
            goodsPriceArr[2] = view.findViewById(R.id.tv_goods_price3);


            for (int i = 0; i < GOODS_IMAGE_COUNT; i++) {
                goodsImageArr[i].setOnClickListener(v -> {
                    int commonId = (int) ((RoundedDataImageView) v).getCustomData();
                    SLog.info("commonId[%d]", commonId);
                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                });
            }
            return view;
        }

        @Override
        public void onBind(Context context, int position, WebSliderItem webSliderItem) {
            // 数据绑定
            String imageUrl = StringUtil.normalizeImageUrl(webSliderItem.image);
            Glide.with(context).load(imageUrl).centerCrop().into(mImageView);
            //SLog.info("webSliderItem.linkType，[%s]",webSliderItem.linkType);

            imgDesktop.setVisibility(View.GONE);
            setGoodsImageVisibility(View.GONE,GOODS_IMAGE_COUNT);
            if(!webSliderItem.goodsCommons.equals("[]")){
                String goodsCommons = webSliderItem.goodsCommons;
                EasyJSONArray goodsArray = (EasyJSONArray) EasyJSONArray.parse(goodsCommons);
                //SLog.info("goodsArray%d",goodsArray.length());
                for (int i=0;i<goodsArray.length();i++) {
                    EasyJSONObject goods;
                    try {
                        goods = goodsArray.getObject(i);
                        int commonId = goods.getInt("commonId");
                        float price = (float) goods.getDouble("goodsPrice0");
                        String goodsImage = StringUtil.normalizeImageUrl(goods.getSafeString("goodsImage"));
                        if (StringUtil.isEmpty(goodsImage)) {
                            goodsImageArr[i].setVisibility(View.GONE);
                        }
                        Glide.with(context).load(goodsImage).centerCrop().into(goodsImageArr[i]);
                        goodsImageArr[i].setCustomData(commonId);
                        goodsPriceArr[i].setText(StringUtil.formatPrice(context, price, 0,false));
                        goodsPriceArr[i].setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }

                    if (goodsArray.length() > 0) {
                        imgDesktop.setVisibility(View.VISIBLE);
                        setGoodsImageVisibility(View.VISIBLE,goodsArray.length());
                    }
                }

            }
        }
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
                    bannerView.setPages(webSliderItemList, new MZHolderCreator<BannerViewHolder>() {
                        @Override
                        public BannerViewHolder createViewHolder() {
                            return new BannerViewHolder(webSliderItemList);
                        }
                    });
                    bannerView.start();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
}
