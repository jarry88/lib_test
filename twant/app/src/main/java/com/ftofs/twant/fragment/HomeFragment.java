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
import com.ftofs.twant.entity.ShoppingZoneItem;
import com.ftofs.twant.entity.StickyCellData;
import com.ftofs.twant.go853.Go853HouseListFragment;
import com.ftofs.twant.kotlin.BlackTestFragment;
import com.ftofs.twant.login.service.LoginServiceImpl;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ActivityPopup;
import com.github.richardwrq.krouter.annotation.Inject;
import com.github.richardwrq.krouter.api.core.KRouter;
import com.gzp.lib_common.base.Jarbon;
import com.gzp.lib_common.service.ConstantsPath;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.orhanobut.hawk.Hawk;
import com.superlht.htloading.view.HTLoading;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.core.adapter.GroupBasicAdapter;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.structure.BaseCell;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

import static com.ftofs.twant.util.Util.dip2px;

public class HomeFragment extends MainBaseFragment implements View.OnClickListener {
    RecyclerView rvList;
    TangramEngine tangramEngine;

    BasePopupView loadPopup;

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
    @Inject(name = ConstantsPath.LOGIN_SERVICE_PATH)
    LoginServiceImpl loginService;
    private HTLoading htLoading;


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KRouter.INSTANCE.inject(this);
        EventBus.getDefault().register(this);
        llFloatButtonContainer = view.findViewById(R.id.ll_float_button_container);
        Util.setOnClickListener(view, R.id.btn_test, this);
        view.findViewById(R.id.btn_test).setVisibility(Config.PROD?View.GONE:View.VISIBLE);
        view.findViewById(R.id.btn_test).setLongClickable(true);
        view.findViewById(R.id.btn_test).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Util.startFragment(new BlackTestFragment());
                return true;
            }
        });
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
    private void showLoadingPopup() {
        if (loadPopup == null) {
            loadPopup = Util.createLoadingPopup(_mActivity);
        }
        loadPopup.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_CAN_SHOW_OTHER_POPUP) {
            showPopupAd();
        }
        if (message.messageType == EBMessageType.LOADING_POPUP_DISMISS&&loadPopup!=null) {
            loadPopup.dismiss();
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
        String url = Config.USE_DEVELOPER_TEST_DATA ? "https://snailpad.cn/tmp/home_index.json" : Api.PATH_HOME_INDEX;

        Api.getUI(url, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, "", "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
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
                    stickyCellData.imSessionCount = responseObj.optInt("datas.imSessionCount");
                    stickyCellData.memberCount = responseObj.optInt("datas.memberCount");
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
            popAd = false;  // 只要顯示過了，就不顯示了

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
//            requireContext().startActivity(new Intent(_mActivity, TestActivity.class));
//            LoginServiceImplWrap.INSTANCE.start(requireContext());
//            final BasePopupView load=Util.createLoadingPopup(requireContext()).show();

//            UiUtilsKt.createLoadingPopup(requireContext()).show();
            Util.startFragment(new Go853HouseListFragment());
//            loginService.start(_mActivity);
        }
    }

    private void showFloatButton() {
        if (floatButtonShown){
            return;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llFloatButtonContainer.getLayoutParams();
        layoutParams.rightMargin = dip2px(_mActivity, 0);
        llFloatButtonContainer.setLayoutParams(layoutParams);
        floatButtonShown = true;
    }

    private void hideFloatButton() {
        if (!floatButtonShown) {
            return;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llFloatButtonContainer.getLayoutParams();
        layoutParams.rightMargin = dip2px(_mActivity,  -30.25f);
        llFloatButtonContainer.setLayoutParams(layoutParams);
        floatButtonShown = false;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        updateMainSelectedFragment(MainFragment.HOME_FRAGMENT);
        if (htLoading != null) {
            htLoading.dismiss();
        }
        SLog.info("carouselLoaded[%s]", carouselLoaded);
        // 加載輪播圖片
        if (!carouselLoaded) {
            loadCarousel();
        }

        // showPopupAd();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
