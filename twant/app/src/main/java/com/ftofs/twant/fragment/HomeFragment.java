package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.TangramCellType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.ShoppingZoneItem;
import com.ftofs.twant.entity.StickyCellData;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ActivityPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.orhanobut.hawk.Hawk;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.core.adapter.GroupBasicAdapter;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.structure.BaseCell;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvList;
    TangramEngine tangramEngine;

    boolean floatButtonShown = true;  // 浮動按鈕是否有顯示
    LinearLayout llFloatButtonContainer;
    private static final int FLOAT_BUTTON_SCROLLING_EFFECT_DELAY = 800; // 浮動按鈕滑動顯示與隱藏效果的延遲時間(毫秒)

    boolean popAd = false;
    int enableEveryTimeAppPopupAd;  // 0 -- 当前APP版本只彈一次 1 -- 每次訪問彈出 2 -- 每天首次訪問彈出
    String appPopupAdImage;
    String appPopupAdLinkType;
    String appPopupAdLinkValue;
    long showAppPopupAdTimestamp;

    boolean carouselLoaded = false;
    boolean newArrivalsLoaded = false;

    BasePopupView popupViewAd;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        llFloatButtonContainer = view.findViewById(R.id.ll_float_button_container);
        Util.setOnClickListener(view, R.id.btn_test, this);
        // view.findViewById(R.id.btn_test).setVisibility(Config.PROD?View.GONE:View.VISIBLE);
        Util.setOnClickListener(view, R.id.btn_goto_top, this);
        Util.setOnClickListener(view, R.id.btn_publish_want_post, this);


        tangramEngine = ((MainActivity) _mActivity).getTangramEngine();
        rvList = view.findViewById(R.id.rv_list);

        // 绑定 RecyclerView
        tangramEngine.bindView(rvList);

        // 监听 RecyclerView 的滚动事件
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //在 scroll 事件中触发 engine 的 onScroll，内部会触发需要异步加载的卡片去提前加载数据
                tangramEngine.onScrolled();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                SLog.info("newState[%d]", newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideFloatButton();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    llFloatButtonContainer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showFloatButton();
                        }
                    }, FLOAT_BUTTON_SCROLLING_EFFECT_DELAY);
                }
            }
        });

        loadNewArrivals();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_CAN_SHOW_OTHER_POPUP) {
            showPopupAd();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
        tangramEngine.unbindView();
    }


    /**
     * 加載輪播圖片
     */
    private void loadCarousel() {
        String url = Api.PATH_HOME_INDEX;
        Api.getUI(url, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, "", "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                if (Config.USE_DEVELOPER_TEST_DATA) {
                    responseStr = "{\"code\":200,\"datas\":{\"appPopupAdLinkValue\":\"44\",\"appIndexNavigationLinkType\":\"shoppingZone\",\"appIndexNavigationLinkValue\":\"55\",\"appPopupAdImage\":\"image/52/77/527754d1f3cc3443bc0599fc589f2b60.png\",\"appIndexNavigationImage\":\"image/2c/96/2c964f98709a1800d7f5263b85e12af0.gif\",\"storeCount\":134,\"enableAppIndexNavigation\":\"1\",\"webSliderItem\":[{\"linkValue\":\"44\",\"image\":\"image/b4/b2/b4b2ddf3d8e04d9fe36860341993f80a.png\",\"linkType\":\"shoppingZone\",\"goodsCommons\":\"[{\\\"commonId\\\":4413,\\\"goodsName\\\":\\\"咨詢商品\\\",\\\"goodsImage\\\":\\\"image/db/fe/dbfe4c9b33ccf85212079017128e06e8.jpg\\\",\\\"goodsPrice\\\":null,\\\"goodsPrice0\\\":999999999.00,\\\"goodsPrice2\\\":999999999.00,\\\"goodsCountry\\\":52,\\\"adminCountry\\\":{\\\"countryId\\\":52,\\\"countryCn\\\":\\\"新國家new\\\",\\\"countryEn\\\":\\\"newnew\\\",\\\"nationalFlag\\\":\\\"image/e7/51/e751d99076dba0448e0339e8283616ca.jpg\\\",\\\"countryContinents\\\":\\\"歐洲\\\",\\\"countrySort\\\":123},\\\"storeId\\\":0,\\\"goodsVideo\\\":\\\"\\\",\\\"updateTime\\\":\\\"1970-01-01 08:00:00\\\",\\\"categoryId1\\\":0,\\\"totalStorage\\\":0,\\\"goodsSaleNum\\\":0,\\\"goodsLike\\\":0,\\\"isLike\\\":0,\\\"storeVo\\\":null,\\\"imageSrc\\\":null,\\\"jingle\\\":null,\\\"batchPrice0\\\":null,\\\"webPriceMin\\\":null,\\\"appPriceMin\\\":null,\\\"storeClassId\\\":0}]\",\"goodsIds\":\"4413\"},{\"linkValue\":\"39\",\"image\":\"image/5a/d5/5ad50c12fbb40e88a52f679c279eac50.jpg\",\"linkType\":\"shoppingZone\",\"goodsCommons\":\"[{\\\"commonId\\\":3850,\\\"goodsName\\\":\\\"Earl Grey Tea 豪門伯爵紅茶\\\",\\\"goodsImage\\\":\\\"image/4c/52/4c521eee10b805546248130e56a0a15e.jpg\\\",\\\"goodsPrice\\\":null,\\\"goodsPrice0\\\":97.00,\\\"goodsPrice2\\\":97.00,\\\"goodsCountry\\\":29,\\\"adminCountry\\\":{\\\"countryId\\\":29,\\\"countryCn\\\":\\\"英國\\\",\\\"countryEn\\\":\\\"UK\\\",\\\"nationalFlag\\\":\\\"image/da/06/da068db3533c6b66ac2a7472c083852d.png\\\",\\\"countryContinents\\\":\\\"歐洲\\\",\\\"countrySort\\\":0},\\\"storeId\\\":0,\\\"goodsVideo\\\":\\\"\\\",\\\"updateTime\\\":\\\"1970-01-01 08:00:00\\\",\\\"categoryId1\\\":0,\\\"totalStorage\\\":0,\\\"goodsSaleNum\\\":0,\\\"goodsLike\\\":0,\\\"isLike\\\":0,\\\"storeVo\\\":null,\\\"imageSrc\\\":null,\\\"jingle\\\":null,\\\"batchPrice0\\\":null,\\\"webPriceMin\\\":null,\\\"appPriceMin\\\":null,\\\"storeClassId\\\":0},{\\\"commonId\\\":3852,\\\"goodsName\\\":\\\"English Breakfast Tea 英國早餐紅茶\\\",\\\"goodsImage\\\":\\\"image/55/aa/55aa8ad30d25d7e73c2fc13c61fd4219.jpg\\\",\\\"goodsPrice\\\":null,\\\"goodsPrice0\\\":88.00,\\\"goodsPrice2\\\":88.00,\\\"goodsCountry\\\":29,\\\"adminCountry\\\":{\\\"countryId\\\":29,\\\"countryCn\\\":\\\"英國\\\",\\\"countryEn\\\":\\\"UK\\\",\\\"nationalFlag\\\":\\\"image/da/06/da068db3533c6b66ac2a7472c083852d.png\\\",\\\"countryContinents\\\":\\\"歐洲\\\",\\\"countrySort\\\":0},\\\"storeId\\\":0,\\\"goodsVideo\\\":\\\"\\\",\\\"updateTime\\\":\\\"1970-01-01 08:00:00\\\",\\\"categoryId1\\\":0,\\\"totalStorage\\\":0,\\\"goodsSaleNum\\\":0,\\\"goodsLike\\\":0,\\\"isLike\\\":0,\\\"storeVo\\\":null,\\\"imageSrc\\\":null,\\\"jingle\\\":null,\\\"batchPrice0\\\":null,\\\"webPriceMin\\\":null,\\\"appPriceMin\\\":null,\\\"storeClassId\\\":0},{\\\"commonId\\\":3854,\\\"goodsName\\\":\\\"Pure Camomile 洋甘菊花草茶\\\",\\\"goodsImage\\\":\\\"image/52/5e/525ee0a839c4a72e9202069c42c374ee.jpg\\\",\\\"goodsPrice\\\":null,\\\"goodsPrice0\\\":95.00,\\\"goodsPrice2\\\":95.00,\\\"goodsCountry\\\":29,\\\"adminCountry\\\":{\\\"countryId\\\":29,\\\"countryCn\\\":\\\"英國\\\",\\\"countryEn\\\":\\\"UK\\\",\\\"nationalFlag\\\":\\\"image/da/06/da068db3533c6b66ac2a7472c083852d.png\\\",\\\"countryContinents\\\":\\\"歐洲\\\",\\\"countrySort\\\":0},\\\"storeId\\\":0,\\\"goodsVideo\\\":\\\"\\\",\\\"updateTime\\\":\\\"1970-01-01 08:00:00\\\",\\\"categoryId1\\\":0,\\\"totalStorage\\\":0,\\\"goodsSaleNum\\\":0,\\\"goodsLike\\\":0,\\\"isLike\\\":0,\\\"storeVo\\\":null,\\\"imageSrc\\\":null,\\\"jingle\\\":null,\\\"batchPrice0\\\":null,\\\"webPriceMin\\\":null,\\\"appPriceMin\\\":null,\\\"storeClassId\\\":0}]\",\"goodsIds\":\"3850,3852,3854\"},{\"linkValue\":\"2009\",\"image\":\"image/9c/7d/9c7d97055e732bed07aaa922ed182c11.jpg\",\"linkType\":\"postId\",\"goodsCommons\":\"[{\\\"commonId\\\":4034,\\\"goodsName\\\":\\\"百&nbsp;分號%%\\\",\\\"goodsImage\\\":\\\"image/a6/a5/a6a505ba0caf4a3f984be6aa19c4b8f3.jpg\\\",\\\"goodsPrice\\\":null,\\\"goodsPrice0\\\":350.00,\\\"goodsPrice2\\\":350.00,\\\"goodsCountry\\\":52,\\\"adminCountry\\\":{\\\"countryId\\\":52,\\\"countryCn\\\":\\\"新國家new\\\",\\\"countryEn\\\":\\\"newnew\\\",\\\"nationalFlag\\\":\\\"image/e7/51/e751d99076dba0448e0339e8283616ca.jpg\\\",\\\"countryContinents\\\":\\\"歐洲\\\",\\\"countrySort\\\":123},\\\"storeId\\\":0,\\\"goodsVideo\\\":\\\"\\\",\\\"updateTime\\\":\\\"1970-01-01 08:00:00\\\",\\\"categoryId1\\\":0,\\\"totalStorage\\\":0,\\\"goodsSaleNum\\\":0,\\\"goodsLike\\\":0,\\\"isLike\\\":0,\\\"storeVo\\\":null,\\\"imageSrc\\\":null,\\\"jingle\\\":null,\\\"batchPrice0\\\":null,\\\"webPriceMin\\\":null,\\\"appPriceMin\\\":null,\\\"storeClassId\\\":0}]\",\"goodsIds\":\"4034\"},{\"linkValue\":\"20\",\"image\":\"image/f3/2e/f32eedf54b55658a50013833b733fd40.jpg\",\"linkType\":\"shoppingZone\",\"goodsCommons\":\"null\",\"goodsIds\":\"\"},{\"linkValue\":\"281\",\"image\":\"image/12/e8/12e8a3a103315483821d33656a39e726.png\",\"linkType\":\"store\",\"goodsCommons\":\"null\",\"goodsIds\":\"\"},{\"linkValue\":\"39\",\"image\":\"image/71/c8/71c8b1fa736a23166ad3689770b07a9c.jpg\",\"linkType\":\"shoppingZone\",\"goodsCommons\":\"[{\\\"commonId\\\":3858,\\\"goodsName\\\":\\\"特效保濕乳霜\\\",\\\"goodsImage\\\":\\\"image/9e/8b/9e8bbba58858292777c5e309968e90ad.jpg\\\",\\\"goodsPrice\\\":null,\\\"goodsPrice0\\\":11.00,\\\"goodsPrice2\\\":11.00,\\\"goodsCountry\\\":51,\\\"adminCountry\\\":{\\\"countryId\\\":51,\\\"countryCn\\\":\\\"其他\\\",\\\"countryEn\\\":\\\"other\\\",\\\"nationalFlag\\\":\\\"\\\",\\\"countryContinents\\\":\\\"亞洲\\\",\\\"countrySort\\\":0},\\\"storeId\\\":0,\\\"goodsVideo\\\":\\\"\\\",\\\"updateTime\\\":\\\"1970-01-01 08:00:00\\\",\\\"categoryId1\\\":0,\\\"totalStorage\\\":0,\\\"goodsSaleNum\\\":0,\\\"goodsLike\\\":0,\\\"isLike\\\":0,\\\"storeVo\\\":null,\\\"imageSrc\\\":null,\\\"jingle\\\":null,\\\"batchPrice0\\\":null,\\\"webPriceMin\\\":null,\\\"appPriceMin\\\":null,\\\"storeClassId\\\":0},{\\\"commonId\\\":3861,\\\"goodsName\\\":\\\"醫學速效去粉刺淡印精華\\\",\\\"goodsImage\\\":\\\"image/99/27/9927a4d3a3b5e6ec9271edf1c4967316.jpg\\\",\\\"goodsPrice\\\":null,\\\"goodsPrice0\\\":300.00,\\\"goodsPrice2\\\":300.00,\\\"goodsCountry\\\":51,\\\"adminCountry\\\":{\\\"countryId\\\":51,\\\"countryCn\\\":\\\"其他\\\",\\\"countryEn\\\":\\\"other\\\",\\\"nationalFlag\\\":\\\"\\\",\\\"countryContinents\\\":\\\"亞洲\\\",\\\"countrySort\\\":0},\\\"storeId\\\":0,\\\"goodsVideo\\\":\\\"\\\",\\\"updateTime\\\":\\\"1970-01-01 08:00:00\\\",\\\"categoryId1\\\":0,\\\"totalStorage\\\":0,\\\"goodsSaleNum\\\":0,\\\"goodsLike\\\":0,\\\"isLike\\\":0,\\\"storeVo\\\":null,\\\"imageSrc\\\":null,\\\"jingle\\\":null,\\\"batchPrice0\\\":null,\\\"webPriceMin\\\":null,\\\"appPriceMin\\\":null,\\\"storeClassId\\\":0},{\\\"commonId\\\":4028,\\\"goodsName\\\":\\\"柔润防晒隔离乳SPF50+ PA++++\\\",\\\"goodsImage\\\":\\\"image/a6/a5/a6a505ba0caf4a3f984be6aa19c4b8f3.jpg\\\",\\\"goodsPrice\\\":null,\\\"goodsPrice0\\\":540.00,\\\"goodsPrice2\\\":100.00,\\\"goodsCountry\\\":52,\\\"adminCountry\\\":{\\\"countryId\\\":52,\\\"countryCn\\\":\\\"新國家new\\\",\\\"countryEn\\\":\\\"newnew\\\",\\\"nationalFlag\\\":\\\"image/e7/51/e751d99076dba0448e0339e8283616ca.jpg\\\",\\\"countryContinents\\\":\\\"歐洲\\\",\\\"countrySort\\\":123},\\\"storeId\\\":0,\\\"goodsVideo\\\":\\\"\\\",\\\"updateTime\\\":\\\"1970-01-01 08:00:00\\\",\\\"categoryId1\\\":0,\\\"totalStorage\\\":0,\\\"goodsSaleNum\\\":0,\\\"goodsLike\\\":0,\\\"isLike\\\":0,\\\"storeVo\\\":null,\\\"imageSrc\\\":null,\\\"jingle\\\":null,\\\"batchPrice0\\\":null,\\\"webPriceMin\\\":null,\\\"appPriceMin\\\":null,\\\"storeClassId\\\":0}]\",\"goodsIds\":\"3858,3861,4028\"},{\"linkValue\":\"19\",\"image\":\"image/75/72/75725a5bfd433b90120f32eecba2ba6d.jpg\",\"linkType\":\"shoppingZone\",\"goodsCommons\":\"null\",\"goodsIds\":\"\"},{\"linkValue\":\"20\",\"image\":\"image/f0/97/f0973dea5cbfd17fd4e4be57aa4a5e3f.png\",\"linkType\":\"shoppingZone\",\"goodsCommons\":\"null\",\"goodsIds\":\"\"},{\"linkValue\":\"44\",\"image\":\"image/f1/dd/f1dd030592af592ec106d6105680a8fc.png\",\"linkType\":\"shoppingZone\",\"goodsCommons\":\"null\",\"goodsIds\":\"\"},{\"linkValue\":\"46\",\"image\":\"image/80/d9/80d9cbce13ba7407fcda7841d1c8a142.png\",\"linkType\":\"shoppingZone\",\"goodsCommons\":\"null\",\"goodsIds\":\"\"}],\"goodsCommonCount\":919,\"wantPostCount\":661,\"enableAppPopupAd\":\"1\",\"enableEveryTimeAppPopupAd\":\"1\",\"shoppingZoneList\":[{\"zoneId\":20,\"zoneName\":\"ID是20\",\"webLogo\":\"image/62/fe/62fe19302eaf9eb9a38132f4fee50b5f.png\",\"appLogo\":\"image/b4/b0/b4b01440e700a07328d6db2af1b6dce3.png\"},{\"zoneId\":48,\"zoneName\":\"6.10測試\",\"webLogo\":\"image/a2/0f/a20ff5b620f6e1b7d2a1df447bace35c.jpeg\",\"appLogo\":\"image/91/4b/914bd76de461becbbd69a47b1ab8f5c4.jpg\"},{\"zoneId\":39,\"zoneName\":\"測試的我\",\"webLogo\":\"image/12/f6/12f6865170093810fbd912c7df5a12af.png\",\"appLogo\":\"image/7e/ec/7eecda92d99b88756f2c632a93e5d284.png\"},{\"zoneId\":49,\"zoneName\":\"遊戲專場\",\"webLogo\":\"image/da/55/da55c66dd262ba0088e2462d07e87734.png\",\"appLogo\":\"image/05/79/0579bc7c3f8a1891c1b689fd1d1cd9ca.png\"}],\"appPopupAdLinkType\":\"shoppingZone\"}}";
                }
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                try {
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, "", responseStr, "");
                        return;
                    }

                    GroupBasicAdapter<Card, ?> adapter = tangramEngine.getGroupBasicAdapter();
                    List<Card> cardList = adapter.getGroups();
                    SLog.info("cardList.size[%d]", cardList.size());

                    // 获取StickyView
                    Card card = cardList.get(1); // 索引为1的是StickyView

                    JSONArray cells = new JSONArray();

                    JSONObject obj = new JSONObject();
                    JSONObject objSlogan = new JSONObject();
                    obj.put("type", TangramCellType.STICKY_CELL);
                    objSlogan.put("type", TangramCellType.SLOGAN_CELL);
                    StickyCellData stickyCellData = new StickyCellData();
                    // 店鋪、商品、想要帖三類數據
                    stickyCellData.goodsCommonCount = responseObj.getInt("datas.goodsCommonCount");;
                    stickyCellData.storeCount = responseObj.getInt("datas.storeCount");
                    stickyCellData.wantPostCount = responseObj.getInt("datas.wantPostCount");
                    EasyJSONArray shoppingZoneList = responseObj.getArray("datas.shoppingZoneList");
                    SLog.info(shoppingZoneList.toString());
                    if (!Util.isJsonNull(shoppingZoneList)) {
                        stickyCellData.zoneItemList=new ArrayList<>();
                        for (Object object : shoppingZoneList) {
                            EasyJSONObject zone = (EasyJSONObject) object;
                            stickyCellData.zoneItemList.add(ShoppingZoneItem.parse(zone)) ;
                        }
                    }

                    // 導航欄
                    // 获取活动入口按钮信息
                    String enableAppIndexNavigationStr = responseObj.getSafeString("datas.enableAppIndexNavigation");
                    int enableAppIndexNavigation = Integer.parseInt(enableAppIndexNavigationStr);
                    stickyCellData.activityEnable = (enableAppIndexNavigation == Constant.TRUE_INT);

                    if (stickyCellData.activityEnable) {
                        stickyCellData.appIndexNavigationImage = responseObj.getSafeString("datas.appIndexNavigationImage");
                        stickyCellData.appIndexNavigationLinkType = responseObj.getSafeString("datas.appIndexNavigationLinkType");
                        stickyCellData.appIndexNavigationLinkValue = responseObj.getSafeString("datas.appIndexNavigationLinkValue");

                        SLog.info("appIndexNavigationImage[%s], appIndexNavigationLinkType[%s], appIndexNavigationLinkValue[%s]",
                                stickyCellData.appIndexNavigationImage, stickyCellData.appIndexNavigationLinkType, stickyCellData.appIndexNavigationLinkValue);
                    }

                    obj.put("data", stickyCellData);
                    objSlogan.put("data", stickyCellData);
                    cells.put(obj);
                    JSONArray cells3=new JSONArray();
                    cells3.put(objSlogan);
                    List<BaseCell> cs = tangramEngine.parseComponent(cells);
                    card.setCells(cs);
                    card.setCells(cs);
                    cardList.get(3).setCells(tangramEngine.parseComponent(cells3));
                    cardList.get(3).notifyDataChange();
                    card.notifyDataChange();
//                    cardList.get(2).notifyDataChange();
                    // END OF 获取StickyView


                    // 彈出廣告
                    String enableAppPopupAdStr = responseObj.getSafeString("datas.enableAppPopupAd");

                    int enableAppPopupAd = Integer.parseInt(enableAppPopupAdStr);
                    if (enableAppPopupAd == Constant.TRUE_INT) {
                        // 0 -- 当前APP版本只彈一次 1 -- 每次訪問彈出 2 -- 每天首次訪問彈出
                        String enableEveryTimeAppPopupAdStr = responseObj.getSafeString("datas.enableEveryTimeAppPopupAd");
                        enableEveryTimeAppPopupAd = Integer.parseInt(enableEveryTimeAppPopupAdStr);

                        if (enableEveryTimeAppPopupAd == Constant.ENABLE_EVERY_TIME_APP_POPUP_AD_ALWAYS || enableEveryTimeAppPopupAd == Constant.ENABLE_EVERY_TIME_APP_POPUP_AD_DAILY_ONCE) {
                            popAd = true;
                        } else { // 查看当前版本是否有弹出
                            String appVersion = BuildConfig.VERSION_NAME.replace(".", "_");
                            SLog.info("appVersion[%s]", appVersion);

                            String key = String.format(SPField.FIELD_POPUP_AD_STATUS_APP_VER, appVersion);
                            int popupCount = Hawk.get(key, Constant.FALSE_INT);
                            popAd = popupCount == Constant.FALSE_INT;
                        }

                        if (popAd) {
                            appPopupAdImage = responseObj.getSafeString("datas.appPopupAdImage");
                            appPopupAdLinkType = responseObj.getSafeString("datas.appPopupAdLinkType");
                            appPopupAdLinkValue = responseObj.getSafeString("datas.appPopupAdLinkValue");

                            SLog.info("appPopupAdImage[%s], appPopupAdLinkType[%s], appPopupAdLinkValue[%s]",
                                    appPopupAdImage, appPopupAdLinkType, appPopupAdLinkValue);
                            showPopupAd();
                        }

                    }
                    carouselLoaded = true;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    /**
     * 加載最新入駐
     */
    private void loadNewArrivals() {
        String json = AssetsUtil.loadText(_mActivity, "tangram/home.json");
        // SLog.info("json[%s]", json);

        try {
            JSONArray data = new JSONArray(json);
            tangramEngine.setData(data);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    } /**
     * 加載頂部購物專場數據
     */
    private void loadShopping() {
        String json = AssetsUtil.loadText(_mActivity, "tangram/home.json");
        // SLog.info("json[%s]", json);

        try {
            JSONArray data = new JSONArray(json);
            tangramEngine.setData(data);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void showPopupAd() {
        MainActivity mainActivity = (MainActivity) _mActivity;
        if (!mainActivity.canShowOtherPopup) {
            return;
        }
        SLog.info("___here");
        long now = System.currentTimeMillis();
        // 每次resume時都顯示一次
        long resumeTimestamp = ((MainActivity) _mActivity).resumeTimestamp;
        SLog.info("___here, popAd[%s], showAppPopupAdTimestamp[%s], resumeTimestamp[%s]", popAd, showAppPopupAdTimestamp, resumeTimestamp);
        if (popAd
                && showAppPopupAdTimestamp != resumeTimestamp /* 用於控制：如果在顯示，則忽略 */ ) {
            SLog.info("___here");
            if (StringUtil.isEmpty(appPopupAdImage)) {
                return;
            }
            SLog.info("___here");
            // 如果是enableEveryTimeAppPopupAd值2 -- 每天首次訪問彈出，還要判斷下當天是否有彈出過
            if (enableEveryTimeAppPopupAd == Constant.ENABLE_EVERY_TIME_APP_POPUP_AD_DAILY_ONCE) {
                long lastShowTimestamp = Hawk.get(SPField.FIELD_POPUP_AD_TIMESTAMP, 0L);
                String today = new Jarbon().toDateString();
                String lastShowDate = new Jarbon(lastShowTimestamp).toDateString();

                SLog.info("today[%s], lastShowDate[%s]", today, lastShowDate);
                if (today.equals(lastShowDate)) {
                    // 如果當天有顯示過，則不顯示
                    return;
                }
            }

            if (popupViewAd == null) {
                popupViewAd = new XPopup.Builder(_mActivity)
                        .dismissOnBackPressed(true) // 按返回键是否关闭弹窗，默认为true
                        .dismissOnTouchOutside(true) // 点击外部是否关闭弹窗，默认为true
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new ActivityPopup(_mActivity, appPopupAdImage,
                                appPopupAdLinkType, appPopupAdLinkValue));
            }

            if (popupViewAd.isDismiss()) {
                popupViewAd.show();
            }

            showAppPopupAdTimestamp = resumeTimestamp;  // 記錄最近一次顯示的時間戳
            if (enableEveryTimeAppPopupAd == Constant.ENABLE_EVERY_TIME_APP_POPUP_AD_VERSION_ONCE) {
                popAd = false;
            }

            String appVersion = BuildConfig.VERSION_NAME.replace(".", "_");
            SLog.info("appVersion[%s]", appVersion);

            String key = String.format(SPField.FIELD_POPUP_AD_STATUS_APP_VER, appVersion);
            Hawk.put(key, Constant.TRUE_INT);
            Hawk.put(SPField.FIELD_POPUP_AD_TIMESTAMP, now);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_goto_top) {
            rvList.scrollToPosition(0);
        } else if (id == R.id.btn_publish_want_post) {
//            Util.startFragment(AddPostFragment.newInstance(false));
            ApiUtil.addPost(_mActivity,false);
        } else if (id == R.id.btn_test) {
            Util.startFragment(LabFragment.newInstance());
        }
    }

    private void showFloatButton() {
        if (floatButtonShown){
            return;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llFloatButtonContainer.getLayoutParams();
        layoutParams.rightMargin = Util.dip2px(_mActivity, 0);
        llFloatButtonContainer.setLayoutParams(layoutParams);
        floatButtonShown = true;
    }

    private void hideFloatButton() {
        if (!floatButtonShown) {
            return;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llFloatButtonContainer.getLayoutParams();
        layoutParams.rightMargin = Util.dip2px(_mActivity,  -30.25f);
        llFloatButtonContainer.setLayoutParams(layoutParams);
        floatButtonShown = false;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        updateMainSelectedFragment(MainFragment.HOME_FRAGMENT);

        // 加載輪播圖片
        if (!carouselLoaded) {
            loadCarousel();
        }

        showPopupAd();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
