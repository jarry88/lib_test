package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.twant.entity.CrossBorderActivityGoods;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.entity.CrossBorderHomeItem;
import com.ftofs.twant.entity.CrossBorderNavItem;
import com.ftofs.twant.entity.CrossBorderNavPane;
import com.ftofs.twant.entity.CrossBorderShoppingZoneItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.FloorItem;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.entity.Store;
import com.ftofs.twant.entity.TimeInfo;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.tangram.NewShoppingSpecialFragment;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.UmengAnalytics;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.CountDownTimerViewHolder;
import com.ftofs.twant.widget.CrossBorderDrawView;
import com.ftofs.twant.widget.FloorContainer;
import com.ftofs.twant.widget.GridLayout;
import com.ftofs.twant.widget.SlantedWidget;
import com.gzp.lib_common.utils.SLog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.hawk.Hawk;
import com.rd.PageIndicatorView;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.HashMap;
import java.util.List;

public class CrossBorderHomeAdapter extends BaseMultiItemQuickAdapter<CrossBorderHomeItem, CountDownTimerViewHolder> {
    Context context;
    public String currentThemeColor;
    String homeDefaultColorStr;

    // 參考：
    // Android 用 RecyclerView 实现倒计时列表功能  https://juejin.im/entry/58d36be044d9040069239acd
    private SparseArray<CountDownTimer> countDownMap;  // 用于退出activity,避免countdown，造成资源浪费。

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CrossBorderHomeAdapter(Context context, List<CrossBorderHomeItem> data, String homeDefaultColorStr) {
        super(data);

        this.context = context;
        this.countDownMap = new SparseArray<>();
        this.homeDefaultColorStr = homeDefaultColorStr;

        addItemType(Constant.ITEM_TYPE_BANNER, R.layout.cross_border_home_banner);
        addItemType(Constant.ITEM_TYPE_HEADER, R.layout.cross_border_home_header);
        addItemType(Constant.ITEM_TYPE_SEC_KILL, R.layout.cross_border_sec_kill);
        addItemType(Constant.ITEM_TYPE_BARGAIN, R.layout.cross_border_bargain);
        addItemType(Constant.ITEM_TYPE_GROUP, R.layout.cross_border_group);
        addItemType(Constant.ITEM_TYPE_FLOOR, R.layout.cross_border_floor_item);
        addItemType(Constant.ITEM_TYPE_BEST_STORE, R.layout.cross_border_best_store);
        addItemType(Constant.ITEM_TYPE_RECOMMEND_TITLE, R.layout.cross_border_recommend_title);
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.cross_border_home_item); // 商品分頁加載Item
        addItemType(Constant.ITEM_TYPE_FOOTER, R.layout.cross_border_home_footer);
    }

    @Override
    protected void convert(@NonNull CountDownTimerViewHolder helper, CrossBorderHomeItem item) {
        int itemType = item.getItemType();

        if (itemType == Constant.ITEM_TYPE_BANNER) {
            ImageView drawView = helper.getView(R.id.vw_bottom_bg);
            drawView.setBackgroundColor(Color.parseColor(homeDefaultColorStr));
            Banner<CrossBorderBannerItem, BannerImageAdapter<CrossBorderBannerItem>> banner = helper.getView(R.id.banner_view);
            banner.setAdapter(new BannerImageAdapter<CrossBorderBannerItem>(item.bannerItemList) {
                @Override
                public void onBindView(BannerImageHolder holder, CrossBorderBannerItem data, int position, int size) {
                    //图片加载自己实现
                    Glide.with(holder.itemView).load(StringUtil.normalizeImageUrl(data.image)).centerCrop().into(holder.imageView);
                }
            })
                    // .addBannerLifecycleObserver(this)//添加生命周期观察者
                    .setIndicator(new CircleIndicator(context));
            banner.addOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    SLog.info("currPosition[%d]", position);
                    CrossBorderBannerItem bannerItem = item.bannerItemList.get(position);

                    boolean canChangeBackgroundColor = Hawk.get(SPField.FIELD_CAN_CHANGE_BACKGROUND_COLOR);
                    if (canChangeBackgroundColor) {
                        currentThemeColor = bannerItem.backgroundColorApp;
                        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_CROSS_BORDER_HOME_THEME_COLOR, currentThemeColor);
                        SLog.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        drawView.setBackgroundColor(Color.parseColor(currentThemeColor));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(Object data, int position) {
                    SLog.info("OnBannerClick[%d]", position);
                    CrossBorderBannerItem bannerItem = item.bannerItemList.get(position);
                    Util.handleClickLink(bannerItem.linkTypeApp, bannerItem.linkValueApp, true);
                }
            });
        } else if (itemType == Constant.ITEM_TYPE_HEADER) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            // 導航區
            RecyclerView rvNavList = helper.getView(R.id.rv_nav_list);
            rvNavList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderNavAdapter navAdapter = new CrossBorderNavAdapter(context, R.layout.cross_border_nav_pane, item.navItemCount, item.navPaneList);
            navAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    int id = view.getId();
                    CrossBorderNavPane navPane = item.navPaneList.get(position);

                    int[] itemIdArr = new int[] {R.id.nav_1, R.id.nav_2, R.id.nav_3, R.id.nav_4, R.id.nav_5,
                            R.id.nav_6, R.id.nav_7, R.id.nav_8, R.id.nav_9, R.id.nav_10, };

                    for (int i = 0; i < itemIdArr.length; i++) {
                        int itemId = itemIdArr[i];
                        if (id == itemId) {
                            CrossBorderNavItem navItem = navPane.crossBorderNavItemList.get(i);
                            SLog.info("navItem[%s]", navItem);
                            Util.handleClickLink(navItem.linkTypeApp, navItem.linkValueApp, true);

                            HashMap<String, Object> analyticsDataMap = new HashMap<>();
                            analyticsDataMap.put("categoryId", navItem.navId);
                            UmengAnalytics.onEventObject(UmengAnalyticsActionName.TARIFF_BUY_NAV, analyticsDataMap);
                            break;
                        }
                    }
                }
            });
            rvNavList.setAdapter(navAdapter);
            rvNavList.setOnFlingListener(null); // 參考：https://stackoverflow.com/questions/44043501/an-instance-of-onflinglistener-already-set-in-recyclerview
            // 使RecyclerView像ViewPager一样的效果，一次只能滑一页，而且居中显示
            // https://www.jianshu.com/p/e54db232df62
            (new PagerSnapHelper()).attachToRecyclerView(rvNavList);

            if (item.navPaneList == null) {
                return;
            }

            int navPaneCount = item.navPaneList.size();
            PageIndicatorView pageIndicatorView = helper.getView(R.id.pageIndicatorView);
            if (navPaneCount < 1) {
                pageIndicatorView.setVisibility(View.GONE);
            } else {
                pageIndicatorView.setRadius(3);
                pageIndicatorView.setCount(navPaneCount);
                rvNavList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            int position = getCurrPosition(rvNavList);
                            pageIndicatorView.setSelection(position);
                        }
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });
            }

            // 購物專場(最多顯示4個)
            int shoppingZoneCount = item.shoppingZoneList.size();
            SLog.info("shoppingZoneCount[%d]", shoppingZoneCount);
            LinearLayout llShoppingZoneContainer = helper.getView(R.id.ll_shopping_zone_container);
            if (shoppingZoneCount > 0) {
                llShoppingZoneContainer.setVisibility(View.VISIBLE);
            } else { // 如果沒有，則隱藏購物專場
                llShoppingZoneContainer.setVisibility(View.GONE);
            }

            int resId = 0;
            if (shoppingZoneCount == 1) {
                resId = R.layout.cross_border_shopping_zone_1;
            } else if (shoppingZoneCount == 2) {
                resId = R.layout.cross_border_shopping_zone_2;
            } else if (shoppingZoneCount == 3) {
                resId = R.layout.cross_border_shopping_zone_3;
            } else {
                resId = R.layout.cross_border_shopping_zone_4;
            }
            View zoneContainer = layoutInflater.inflate(resId, llShoppingZoneContainer, false);

            // shoppingZoneCount = 1;
            if (shoppingZoneCount >= 1) {
                CrossBorderShoppingZoneItem shoppingZoneItem1 = item.shoppingZoneList.get(0);
                ImageView zone1 = zoneContainer.findViewById(R.id.img_shopping_zone_1);
                zone1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.startFragment(NewShoppingSpecialFragment.newInstance(shoppingZoneItem1.zoneId));
                        handleClickShoppingZone(shoppingZoneItem1.zoneId);
                    }
                });
                Glide.with(context).load(StringUtil.normalizeImageUrl(shoppingZoneItem1.appLogo)).centerCrop().into(zone1);

                if (shoppingZoneCount >= 2) {
                    CrossBorderShoppingZoneItem shoppingZoneItem2 = item.shoppingZoneList.get(1);
                    ImageView zone2 = zoneContainer.findViewById(R.id.img_shopping_zone_2);
                    zone2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.startFragment(NewShoppingSpecialFragment.newInstance(shoppingZoneItem2.zoneId));
                            handleClickShoppingZone(shoppingZoneItem2.zoneId);
                        }
                    });
                    Glide.with(context).load(StringUtil.normalizeImageUrl(shoppingZoneItem2.appLogo)).centerCrop().into(zone2);
                    if (shoppingZoneCount >= 3) {
                        CrossBorderShoppingZoneItem shoppingZoneItem3 = item.shoppingZoneList.get(2);
                        ImageView zone3 = zoneContainer.findViewById(R.id.img_shopping_zone_3);
                        zone3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Util.startFragment(NewShoppingSpecialFragment.newInstance(shoppingZoneItem3.zoneId));
                                handleClickShoppingZone(shoppingZoneItem3.zoneId);
                            }
                        });
                        Glide.with(context).load(StringUtil.normalizeImageUrl(shoppingZoneItem3.appLogo)).centerCrop().into(zone3);

                        if (shoppingZoneCount >= 4) {
                            CrossBorderShoppingZoneItem shoppingZoneItem4 = item.shoppingZoneList.get(3);
                            ImageView zone4 = zoneContainer.findViewById(R.id.img_shopping_zone_4);
                            zone3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Util.startFragment(NewShoppingSpecialFragment.newInstance(shoppingZoneItem4.zoneId));
                                    handleClickShoppingZone(shoppingZoneItem4.zoneId);
                                }
                            });
                            Glide.with(context).load(StringUtil.normalizeImageUrl(shoppingZoneItem4.appLogo)).centerCrop().into(zone4);
                        }
                    }
                }
            }
            llShoppingZoneContainer.removeAllViews();
            llShoppingZoneContainer.addView(zoneContainer);
        } else if (itemType == Constant.ITEM_TYPE_SEC_KILL) { // 秒殺
            TextView tvCountDownHour = helper.getView(R.id.tv_count_down_hour);
            TextView tvCountDownMinute = helper.getView(R.id.tv_count_down_minute);
            TextView tvCountDownSecond = helper.getView(R.id.tv_count_down_second);
            long now = System.currentTimeMillis();
            long remainTime = item.secKillCountDown - now;
            SLog.info("secKillCountDown[%s], now[%s], remainTime[%s]", item.secKillCountDown, now, remainTime);
            if (remainTime > 0) {
                helper.countDownTimer = new CountDownTimer(remainTime, 100) {
                    public void onTick(long millisUntilFinished) {
                        TimeInfo timeInfo = Time.diff((int) (System.currentTimeMillis() / 1000), (int) (item.secKillCountDown / 1000));
                        if (timeInfo != null) {
                            tvCountDownHour.setText(String.format("%02d", timeInfo.hour));
                            tvCountDownMinute.setText(String.format("%02d", timeInfo.minute));
                            tvCountDownSecond.setText(String.format("%02d", timeInfo.second));
                        }
                    }
                    public void onFinish() {
                        // 倒計時結束
                    }
                }.start();

                countDownMap.put(tvCountDownHour.hashCode(), helper.countDownTimer);
            }

            // 超過3個才顯示【更多】按鈕
            helper.setVisible(R.id.btn_view_more_sec_kill, item.secKillGoodsList.size() > 3);
            helper.addOnClickListener(R.id.btn_view_more_sec_kill);

            RecyclerView rvSecKillList = helper.getView(R.id.rv_sec_kill_list);
            rvSecKillList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderActivityGoodsAdapter secKillGoodsAdapter =
                    new CrossBorderActivityGoodsAdapter(context, Constant.PROMOTION_TYPE_SEC_KILL, R.layout.cross_border_activity_goods_item, item.secKillGoodsList);
            secKillGoodsAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CrossBorderActivityGoods activityGoods = item.secKillGoodsList.get(position);
                    Util.startFragment(GoodsDetailFragment.newInstance(activityGoods.commonId, activityGoods.goodsId));
                }
            });
            rvSecKillList.setAdapter(secKillGoodsAdapter);
        } else if (itemType == Constant.ITEM_TYPE_BARGAIN) { // 砍價
            // 超過3個才顯示【更多】按鈕
            helper.setVisible(R.id.btn_view_more_bargain, item.bargainGoodsList.size() > 3);
            helper.addOnClickListener(R.id.btn_view_more_bargain);

            RecyclerView rvBargainList = helper.getView(R.id.rv_bargain_list);
            rvBargainList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderActivityGoodsAdapter bargainGoodsAdapter =
                    new CrossBorderActivityGoodsAdapter(context, Constant.PROMOTION_TYPE_BARGAIN, R.layout.cross_border_activity_goods_item, item.bargainGoodsList);
            bargainGoodsAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CrossBorderActivityGoods activityGoods = item.bargainGoodsList.get(position);
                    SLog.info("____bargainId[%d]", activityGoods.bargainId);
                    Util.startFragment(GoodsDetailFragment.newInstance(activityGoods.commonId, activityGoods.goodsId, activityGoods.bargainId));
                    HashMap<String, Object> analyticsDataMap = new HashMap<>();
                    UmengAnalytics.onEventObject(UmengAnalyticsActionName.TARIFF_BUY_BARGAIN, analyticsDataMap);
                }
            });
            rvBargainList.setAdapter(bargainGoodsAdapter);
        } else if (itemType == Constant.ITEM_TYPE_GROUP) { // 拼團
            // 超過3個才顯示【更多】按鈕
            helper.setVisible(R.id.btn_view_more_group, item.groupGoodsList.size() > 3);
            helper.addOnClickListener(R.id.btn_view_more_group);

            RecyclerView rvGroupList = helper.getView(R.id.rv_group_list);
            rvGroupList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderActivityGoodsAdapter groupGoodsAdapter =
                    new CrossBorderActivityGoodsAdapter(context, Constant.PROMOTION_TYPE_GROUP, R.layout.cross_border_activity_goods_item, item.groupGoodsList);
            groupGoodsAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CrossBorderActivityGoods activityGoods = item.groupGoodsList.get(position);
                    Util.startFragment(GoodsDetailFragment.newInstance(activityGoods.commonId, activityGoods.goodsId));
                    HashMap<String, Object> analyticsDataMap = new HashMap<>();
                    UmengAnalytics.onEventObject(UmengAnalyticsActionName.TARIFF_BUY_GROUP, analyticsDataMap);
                }
            });
            rvGroupList.setAdapter(groupGoodsAdapter);
        } else if (itemType == Constant.ITEM_TYPE_FLOOR) { // 樓層
            helper.setText(R.id.tv_title, item.floorHeadline)
                    .setText(R.id.tv_sub_title, item.floorSubhead);

            FloorContainer floorContainer = helper.getView(R.id.floor_container);
            floorContainer.removeAllViews();

            RoundedImageView imgFloorBanner = helper.getView(R.id.img_floor_banner);
            imgFloorBanner.setCornerRadiusDimen(R.dimen.dp_4);
            String floorType = item.floorType;
            View btnMore = helper.getView(R.id.btn_more);

            if (CrossBorderHomeItem.FLOOR_TYPE_BANNER.equals(floorType)) { // Banner圖類型
                helper.setGone(R.id.floor_banner_container, true); // 顯示Banner容器
                floorContainer.setVisibility(View.GONE);
                btnMore.setVisibility(View.VISIBLE);
                if (item.floorItemList != null && item.floorItemList.size() > 0) {
                    FloorItem floorItem = item.floorItemList.get(0);
                    Glide.with(context).load(StringUtil.normalizeImageUrl(floorItem.imageName)).centerCrop().into(imgFloorBanner);
                    imgFloorBanner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.handleClickLink(floorItem.linkType, floorItem.linkValue, true);
                        }
                    });

                    btnMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.handleClickLink(floorItem.linkType, floorItem.linkValue, true);
                        }
                    });
                }
            } else { // 圖片類型
                helper.setGone(R.id.floor_banner_container, false);  // 隱藏Banner容器
                floorContainer.setVisibility(View.VISIBLE);
                btnMore.setVisibility(View.INVISIBLE);

                for (int i = 0; i < item.floorItemList.size(); i++) {
                    FloorItem floorItem = item.floorItemList.get(i);
                    ImageView imageView = new ImageView(context);
                    imageView.setTag(R.id.key_meta_data, floorItem);
                    Glide.with(context).load(floorItem.imageName).into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FloorItem imageItem = (FloorItem) v.getTag(R.id.key_meta_data);
                            SLog.info("imageName[%s]", imageItem.imageName);
                            Util.handleClickLink(imageItem.linkType, imageItem.linkValue, true);
                        }
                    });
                    floorContainer.addView(imageView);
                }
            }
        } else if (itemType == Constant.ITEM_TYPE_BEST_STORE) { // 優選好店
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            helper.setGone(R.id.ll_best_store_container, item.storeList.size() > 0);  // 如果沒有店鋪，則隱藏
            GridLayout glStoreContainer = helper.getView(R.id.gl_store_container);
            glStoreContainer.removeAllViews();
            for (Store store : item.storeList) {
                View storeItemView = layoutInflater.inflate(R.layout.cross_border_store_item, glStoreContainer, false);
                ImageView imgStoreFigure = storeItemView.findViewById(R.id.img_store_figure);
                SLog.info("store.storeFigureImage[%s]", store.storeFigureImage);
                Glide.with(context).load(StringUtil.normalizeImageUrl(store.storeFigureImage)).centerCrop().into(imgStoreFigure);
                ((TextView) storeItemView.findViewById(R.id.tv_store_name)).setText(store.storeName);
                storeItemView.findViewById(R.id.rl_store_container).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.startFragment(ShopMainFragment.newInstance(store.storeId));
                    }
                });

                TextView tvTest = new TextView(context);
                tvTest.setText("aaaaaabbb");
                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(context, 127));
                glStoreContainer.addView(storeItemView, layoutParams);
            }
        } else if (itemType == Constant.ITEM_TYPE_NORMAL) {
            helper.addOnClickListener(R.id.cl_container_left, R.id.cl_container_right);

            if (item.goodsPair == null) {
                return;
            }
            GoodsSearchItemPair goodsPair = item.goodsPair;
            if (goodsPair.left != null) {
                ImageView goodsImage = helper.getView(R.id.goods_image_left);
                Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.left.imageSrc)).centerCrop().into(goodsImage);

                ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar_left);
                if (StringUtil.isEmpty(goodsPair.left.storeAvatarUrl)) {
                    Glide.with(context).load(R.drawable.grey_default_avatar).centerCrop().into(imgStoreAvatar);
                } else {
                    Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.left.storeAvatarUrl)).centerCrop().into(imgStoreAvatar);
                }


                if (!StringUtil.isEmpty(goodsPair.left.nationalFlag)) {
                    ImageView imgGoodsNationalFlag = helper.getView(R.id.img_goods_national_flag_left);
                    //fitcenter塞到国旗圆框里
                    Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.left.nationalFlag)).centerInside().into(imgGoodsNationalFlag);
                    imgGoodsNationalFlag.setVisibility(View.VISIBLE);
                }

                helper.setText(R.id.tv_store_name_left, goodsPair.left.storeName);
                helper.setText(R.id.tv_goods_name_left, goodsPair.left.goodsName);
                TextView tvGoodsJingleLeft = helper.getView(R.id.tv_goods_jingle_left);
                if (StringUtil.isEmpty(goodsPair.left.jingle)) {
                    tvGoodsJingleLeft.setVisibility(View.GONE);
                } else {
                    tvGoodsJingleLeft.setText(goodsPair.left.jingle);
                    tvGoodsJingleLeft.setVisibility(View.VISIBLE);
                }

                TextView leftTextView = helper.getView(R.id.tv_goods_price_left);
                if (Util.noPrice(goodsPair.left.goodsModel)) {
                    UiUtil.toConsultUI(leftTextView);
                } else {
                    leftTextView.setText(StringUtil.formatPrice(context, goodsPair.left.price, 1,false));
                    UiUtil.toPriceUI(leftTextView,0);
                }


                helper.setGone(R.id.tv_freight_free_left, goodsPair.left.isFreightFree)
                        .setGone(R.id.tv_gift_left, goodsPair.left.hasGift);
//                        .setGone(R.id.tv_discount_left, item.left.hasDiscount)

                SlantedWidget slantedWidget = helper.getView(R.id.slanted_widget_left);
                if (goodsPair.left.showDiscountLabel) {
                    slantedWidget.setVisibility(View.VISIBLE);
                    slantedWidget.setDiscountInfo(context,goodsPair.left.extendPrice0, goodsPair.left.batchPrice0);
                } else {
                    slantedWidget.setVisibility(View.GONE);
                }

                helper.addOnClickListener(R.id.btn_goto_store_left, R.id.cl_container_left);

                // 設置是否顯示【跨城購】標籤
                helper.setGone(R.id.tv_cross_border_indicator_left, goodsPair.left.tariffEnable == Constant.TRUE_INT);
            }
            // 設置右邊item的可見性

            if (goodsPair.right != null) {
                ImageView goodsImage = helper.getView(R.id.goods_image_right);
                Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.right.imageSrc)).centerCrop().into(goodsImage);

                ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar_right);
                if (StringUtil.isEmpty(goodsPair.right.storeAvatarUrl)) {
                    Glide.with(context).load(R.drawable.grey_default_avatar).centerCrop().into(imgStoreAvatar);
                } else {
                    Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.right.storeAvatarUrl)).centerCrop().into(imgStoreAvatar);
                }


                if (!StringUtil.isEmpty(goodsPair.right.nationalFlag)) {
                    ImageView imgGoodsNationalFlag = helper.getView(R.id.img_goods_national_flag_right);
                    Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.right.nationalFlag)).centerInside().into(imgGoodsNationalFlag);
                    imgGoodsNationalFlag.setVisibility(View.VISIBLE);
                }

                helper.setText(R.id.tv_store_name_right, goodsPair.right.storeName);
                helper.setText(R.id.tv_goods_name_right, goodsPair.right.goodsName);
                TextView tvGoodsJingleRight = helper.getView(R.id.tv_goods_jingle_right);
                if (StringUtil.isEmpty(goodsPair.right.jingle)) {
                    tvGoodsJingleRight.setVisibility(View.GONE);
                } else {
                    tvGoodsJingleRight.setText(goodsPair.right.jingle);
                    tvGoodsJingleRight.setVisibility(View.VISIBLE);
                }

                TextView priceRight = helper.getView(R.id.tv_goods_price_right);
                if (Util.noPrice(goodsPair.right.goodsModel)) {
                    UiUtil.toConsultUI(priceRight);
                } else {
                    priceRight.setText(StringUtil.formatPrice(context, goodsPair.right.price, 1, false));
                    UiUtil.toPriceUI(priceRight, 0);
                }

                helper.setGone(R.id.tv_freight_free_right, goodsPair.right.isFreightFree)
                        .setGone(R.id.tv_gift_right, goodsPair.right.hasGift);
//                        .setGone(R.id.tv_discount_right, item.right.hasDiscount);

                SlantedWidget slantedWidget = helper.getView(R.id.slanted_widget_right);
                if (goodsPair.right.showDiscountLabel) {
                    slantedWidget.setVisibility(View.VISIBLE);
                    slantedWidget.setDiscountInfo(context, goodsPair.right.extendPrice0, goodsPair.right.batchPrice0);
                } else {
                    slantedWidget.setVisibility(View.GONE);
                }

                helper.addOnClickListener(R.id.btn_goto_store_right, R.id.cl_container_right);

                // 設置是否顯示【跨城購】標籤
                helper.setGone(R.id.tv_cross_border_indicator_right, goodsPair.right.tariffEnable == Constant.TRUE_INT);
            } else {
                helper.setGone(R.id.tv_freight_free_right, false)
                        .setGone(R.id.tv_cross_border_indicator_right, false);
            }
            boolean rightHandSideVisible = (goodsPair.right != null);
            helper.setGone(R.id.cl_container_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_name_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_jingle_right, rightHandSideVisible)
                    .setGone(R.id.vw_right_bottom_separator, rightHandSideVisible)
                    .setGone(R.id.btn_goto_store_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_price_right, rightHandSideVisible);
            if (item.goodsPair != null && goodsPair.right != null) {
                TextView tvGoodsJingleRight = helper.getView(R.id.tv_goods_jingle_right);
                if (StringUtil.isEmpty(goodsPair.right.jingle)) {
                    tvGoodsJingleRight.setVisibility(View.GONE);
                } else {
                    tvGoodsJingleRight.setText(goodsPair.right.jingle);
                    tvGoodsJingleRight.setVisibility(View.VISIBLE);
                }
            }
        } else {

        }
    }

    /**
     * 获取当前显示的图片的position
     *
     * @return
     */
    private int getCurrPosition(RecyclerView rvList) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvList.getLayoutManager();
        int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        return position;
    }

    @Override
    public void onViewRecycled(@NonNull CountDownTimerViewHolder holder) {
        super.onViewRecycled(holder);

        SLog.info("onViewRecycled, position[%d]", holder.getAdapterPosition());
    }

    @Override
    public void onViewAttachedToWindow(CountDownTimerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        SLog.info("onViewAttachedToWindow, position[%d]", holder.getAdapterPosition());
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CountDownTimerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        SLog.info("onViewDetachedFromWindow, position[%d]", holder.getAdapterPosition());
    }

    private void handleClickShoppingZone(int zoneId) {
        HashMap<String, Object> analyticsDataMap = new HashMap<>();
        analyticsDataMap.put("zoneId", zoneId);
        UmengAnalytics.onEventObject(UmengAnalyticsActionName.TARIFF_BUY_ZONE, analyticsDataMap);
    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }


        SLog.info("size :  " + countDownMap.size());
        for (int i = 0,length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }
}
