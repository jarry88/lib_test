package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.TangramCellType;
import com.ftofs.twant.entity.StickyCellData;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
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
import com.tmall.wireless.tangram.support.SimpleClickSupport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class NewHomeFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvList;
    TangramEngine tangramEngine;

    boolean popAd = false;
    String appPopupAdImage;
    String appPopupAdLinkType;
    String appPopupAdLinkValue;
    long showAppPopupAdTimestamp;

    boolean carouselLoaded;
    boolean newArrivalsLoaded;

    BasePopupView popupViewAd;

    public static NewHomeFragment newInstance() {
        Bundle args = new Bundle();

        NewHomeFragment fragment = new NewHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_test, this);
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
        });

        loadCarousel();

        loadNewArrivals();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        tangramEngine.unbindView();
    }


    private void test() {

        if (true) {
            pop();
            return;
        }
        try {
            GroupBasicAdapter<Card, ?> adapter = tangramEngine.getGroupBasicAdapter();
            List<Card> cardList = adapter.getGroups();
            SLog.info("cardList.size[%d]", cardList.size());

            // 获取StickyView
            Card card = cardList.get(1); // 索引为1的是StickyView

            JSONArray cells = new JSONArray();

            JSONObject obj = new JSONObject();
            obj.put("type", TangramCellType.STICKY_CELL);
            StickyCellData stickyCellData = new StickyCellData();
            stickyCellData.goodsCommonCount = 100; stickyCellData.storeCount = 200; stickyCellData.wantPostCount = 300;
            obj.put("data", stickyCellData);

            cells.put(obj);

            List<BaseCell> cs = tangramEngine.parseComponent(cells);


            card.setCells(cs);
            card.notifyDataChange();
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    /**
     * 加載輪播圖片
     */
    private void loadCarousel() {
        Api.getUI(Api.PATH_HOME_CAROUSEL, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                try {
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    GroupBasicAdapter<Card, ?> adapter = tangramEngine.getGroupBasicAdapter();
                    List<Card> cardList = adapter.getGroups();
                    SLog.info("cardList.size[%d]", cardList.size());

                    // 获取StickyView
                    Card card = cardList.get(1); // 索引为1的是StickyView

                    JSONArray cells = new JSONArray();

                    JSONObject obj = new JSONObject();
                    obj.put("type", TangramCellType.STICKY_CELL);
                    StickyCellData stickyCellData = new StickyCellData();
                    // 店鋪、商品、想要帖三類數據
                    stickyCellData.goodsCommonCount = responseObj.getInt("datas.goodsCommonCount");;
                    stickyCellData.storeCount = responseObj.getInt("datas.storeCount");
                    stickyCellData.wantPostCount = responseObj.getInt("datas.wantPostCount");

                    // 導航欄
                    // 获取活动入口按钮信息
                    String enableAppIndexNavigationStr = responseObj.getSafeString("datas.enableAppIndexNavigation");
                    int enableAppIndexNavigation = Integer.parseInt(enableAppIndexNavigationStr);
                    stickyCellData.activityEnable = (enableAppIndexNavigation == Constant.TRUE_INT);
                    if (stickyCellData.activityEnable) {
                        stickyCellData.appIndexNavigationImage = responseObj.getSafeString("datas.appIndexNavigationImage");
                        stickyCellData.appIndexNavigationLinkType = responseObj.getSafeString("datas.appIndexNavigationLinkType");
                        stickyCellData.appIndexNavigationLinkValue = StringUtil.normalizeImageUrl(responseObj.getSafeString("datas.appIndexNavigationLinkValue"));

                        SLog.info("appIndexNavigationImage[%s], appIndexNavigationLinkType[%s], appIndexNavigationLinkValue[%s]",
                                stickyCellData.appIndexNavigationImage, stickyCellData.appIndexNavigationLinkType, stickyCellData.appIndexNavigationLinkValue);
                    }

                    obj.put("data", stickyCellData);
                    cells.put(obj);
                    List<BaseCell> cs = tangramEngine.parseComponent(cells);
                    card.setCells(cs);
                    card.notifyDataChange();
                    // END OF 获取StickyView


                    // 获取CarouselView
                    card = cardList.get(2); // 索引为2的是CarouselView
                    cells = new JSONArray();

                    EasyJSONArray itemList = responseObj.getSafeArray("datas.webSliderItem");
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

                        obj = new JSONObject();
                        obj.put("type", TangramCellType.CAROUSEL_CELL);
                        obj.put("data", webSliderItem);
                        cells.put(obj);
                    }
                    cs = tangramEngine.parseComponent(cells);
                    card.setCells(cs);
                    card.notifyDataChange();
                    // END OF 获取CarouselView

                    // 彈出廣告
                    String enableAppPopupAdStr = responseObj.getSafeString("datas.enableAppPopupAd");

                    int enableAppPopupAd = Integer.parseInt(enableAppPopupAdStr);
                    if (enableAppPopupAd == Constant.TRUE_INT) {
                        // 0 -- 当前APP版本只彈一次 1 -- 每次訪問彈出
                        String enableEveryTimeAppPopupAdStr = responseObj.getSafeString("datas.enableEveryTimeAppPopupAd");
                        int enableEveryTimeAppPopupAd = Integer.parseInt(enableEveryTimeAppPopupAdStr);

                        if (enableEveryTimeAppPopupAd == Constant.TRUE_INT) {
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
        SLog.info("json[%s]", json);

        try {
            JSONArray data = new JSONArray(json);
            tangramEngine.setData(data);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void showPopupAd() {
        // 每次resume時都顯示一次
        long resumeTimestamp = ((MainActivity) _mActivity).resumeTimestamp;
        if (popAd && showAppPopupAdTimestamp != resumeTimestamp ) {
            if (StringUtil.isEmpty(appPopupAdImage)) {
                return;
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
            popAd = false;

            String appVersion = BuildConfig.VERSION_NAME.replace(".", "_");
            SLog.info("appVersion[%s]", appVersion);

            String key = String.format(SPField.FIELD_POPUP_AD_STATUS_APP_VER, appVersion);
            Hawk.put(key, Constant.TRUE_INT);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_goto_top) {
            rvList.scrollToPosition(0);
        } else if (id == R.id.btn_publish_want_post) {
            Util.startFragment(AddPostFragment.newInstance(false));
        } else if (id == R.id.btn_test) {
            test();
        }
    }
}
