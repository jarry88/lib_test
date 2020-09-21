package com.ftofs.twant.widget;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.entity.SkuGalleryItem;
import com.ftofs.twant.entity.Spec;
import com.ftofs.twant.entity.SpecButtonData;
import com.ftofs.twant.entity.SpecValue;
import com.ftofs.twant.fragment.ArrivalNoticeFragment;
import com.ftofs.twant.fragment.NewConfirmOrderFragment;
import com.ftofs.twant.fragment.ViewPagerFragment;
import com.ftofs.twant.fragment.SkuImageFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.nex3z.flowlayout.FlowLayout;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 規格選擇框
 * 有些產品沒有規格，只有一個goodsId
 * @author zwm
 */
public class SpecSelectPopup extends BottomPopupView implements View.OnClickListener, SimpleCallback {
    private final int discountState;
    private int limitBuy;
    private ViewPagerFragment viewPagerFragment;
    Context context;
    int action;
    int commonId;
    List<Spec> specList;
    ImageView skuImage;
    TextView tvPrice;
    TextView tvGoodsStorage;
    TextView tvBuyLimit;
    List<SkuGalleryItem> skuGalleryItemList;

    TextView btnOk;

    Map<String, Integer> specValueIdMap;
    List<Integer> specValueIdList;
    Map<Integer, GoodsInfo> goodsInfoMap;

    // 當前選中的產品的信息
    GoodsInfo goodsInfo;

    // 規格的數量
    int specCount;
    // 當前選中規格按鈕的引用
    List<TextView> selSpecButtonList;
    // 當前選中的SpecValueId
    List<Integer> selSpecValueIdList;
    // SpecValueId與Button的映射關係
    Map<Integer, TextView> specValueIdButtonMap = new HashMap<>();

    // 調整數量
    AdjustButton abQuantity;
    int quantity;  // 外面傳進來的數量初始值

    String outOfMaxValueReason; // 購買數量超過庫存數或限購數的提示
    private boolean isShowing;
    private List<String> currGalleryImageList;

    boolean canViewSkuImage = false;
    boolean groupBuyMode;

    int goId; // 開團Id 0: 表示自己開團  -1: 表示無效

    int noSpecGoodsId = 0; // 无规格的商品的goodsId, 只有一个goodsId

    public SpecSelectPopup(@NonNull Context context, int action, int commonId, List<Spec> specList,
                           Map<String, Integer> specValueIdMap, List<Integer> specValueIdList,
                           int quantity, Map<Integer, GoodsInfo> goodsInfoMap, List<String> viewPagerFragment, int limitBuy,
                           int discountState, List<SkuGalleryItem> skuGalleryItemList) {
        this(context, action, commonId, specList, specValueIdMap, specValueIdList,
                    quantity, goodsInfoMap, viewPagerFragment, limitBuy,
                    discountState, skuGalleryItemList, false, Constant.INVALID_GO_ID);
    }


    /**
     * @param context
     * @param action
     * @param commonId  spuId，在specList為空的情況下才需要
     * @param specList  如果specList為空，表示在裏面加載數據
     * @param specValueIdMap 根據逗號拼接的specValueId字符串，定位出產品的goodsId的映射
     * @param specValueIdList 傳進來的當前選中的specValueId列表，如果為null，則默認都選中第一項
     * @param quantity 数量
     * @param goodsInfoMap
     * @param viewPagerFragment 圖片瀏覽器
     * @param limitBuy
     * @param skuGalleryItemList sku圖片列表（如果specList為null，skuGalleryItemList也必須為null）
     * @param groupBuyMode 是否為團購模式
     * @param goId 開團Id
     */
    public SpecSelectPopup(@NonNull Context context, int action, int commonId, List<Spec> specList,
                           Map<String, Integer> specValueIdMap, List<Integer> specValueIdList,
                           int quantity, Map<Integer, GoodsInfo> goodsInfoMap, List<String> viewPagerFragment, int limitBuy,
                           int discountState, List<SkuGalleryItem> skuGalleryItemList, boolean groupBuyMode, int goId) {
        super(context);

        SLog.info("groupBuyMode[%s]", groupBuyMode);

        this.context = context;
        this.action = action;
        this.commonId = commonId;
        this.specList = specList;
        this.specValueIdMap = specValueIdMap;
        this.specValueIdList = specValueIdList;
        this.goodsInfoMap = goodsInfoMap;
        this.quantity = quantity;
        this.currGalleryImageList = viewPagerFragment;
        this.limitBuy = limitBuy;
        this.discountState = discountState;
        this.skuGalleryItemList = skuGalleryItemList;
        this.groupBuyMode = groupBuyMode;
        this.goId = goId;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.spec_select_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        // 關閉Popup
        // findViewById(R.id.ll_title_padding).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);

        // 加入購物袋
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        if (action == Constant.ACTION_ADD_TO_CART) {
            btnOk.setBackgroundResource(R.drawable.blue_button1);
        } else if (action == Constant.ACTION_BUY) {
            btnOk.setBackgroundResource(R.drawable.blue_button1);
        } else if (action == Constant.ACTION_SELECT_SPEC) {
            btnOk.setBackgroundResource(R.drawable.blue_button1);
        }

        skuImage = findViewById(R.id.sku_image);
        skuImage.setOnClickListener(this);
        tvPrice = findViewById(R.id.tv_price);
        tvGoodsStorage = findViewById(R.id.tv_goods_storage);
        tvBuyLimit = findViewById(R.id.tv_buy_limit);

        abQuantity = findViewById(R.id.ab_quantity);
        abQuantity.setValue(quantity);
        abQuantity.setMinValue(1, new AdjustButton.OutOfValueCallback() {
            @Override
            public void outOfValue() {
                ToastUtil.error(context, "購買數量不能再減少了");
            }
        });

        if (specList != null) {
            populateData();
            canViewSkuImage = true;
        } else {
            assert skuGalleryItemList == null;
            canViewSkuImage = false;
            String url = Api.PATH_SKU_LIST + "/" + commonId;
            EasyJSONObject params = EasyJSONObject.generate("commonId", commonId);
            Api.getUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(context, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(context, responseObj)) {
                        return;
                    }

                    try {
                        specList = new ArrayList<>();

                        EasyJSONObject goodsCommon = responseObj.getSafeObject("datas.goodsCommon");

                        String unitName = goodsCommon.getSafeString("unitName");

                        EasyJSONArray specJson = goodsCommon.getSafeArray("specJson");
                        for (Object object : specJson) {
                            EasyJSONObject specObj = (EasyJSONObject) object;

                            Spec specItem = new Spec();

                            specItem.specId = specObj.getInt("specId");
                            specItem.specName = specObj.getSafeString("specName");

                            EasyJSONArray specValueList = specObj.getSafeArray("specValueList");

                            for (Object object2 : specValueList) {
                                EasyJSONObject specValue = (EasyJSONObject) object2;
                                int specValueId = specValue.getInt("specValueId");
                                String specValueName = specValue.getSafeString("specValueName");
                                String imageSrc = specValue.getSafeString("imageSrc");

                                SpecValue specValueItem = new SpecValue(specValueId, specValueName, imageSrc);
                                specItem.specValueList.add(specValueItem);

                            }
                            specList.add(specItem);
                        }


                        specValueIdMap = new HashMap<>();
                        String goodsSpecValues = goodsCommon.getSafeString("goodsSpecValues");
                        EasyJSONArray goodsSpecValueArr = EasyJSONArray.parse(goodsSpecValues);
                        for (Object object : goodsSpecValueArr) {
                            EasyJSONObject mapItem = (EasyJSONObject) object;
                            SLog.info("kkkkey[%s], vvvalue[%s]", mapItem.getSafeString("specValueIds"), mapItem.getInt("goodsId"));
                            // 有些沒有規格的產品，只有一個goodsId，且specValueIds字符串為空串
                            specValueIdMap.put(mapItem.getSafeString("specValueIds"), mapItem.getInt("goodsId"));
                        }

                        goodsInfoMap = new HashMap<>();
                        skuGalleryItemList = new ArrayList<>();
                        EasyJSONArray goodsInfoVoList = goodsCommon.getSafeArray("goodsList");
                        for (Object object : goodsInfoVoList) {
                            GoodsInfo goodsInfo = new GoodsInfo();

                            EasyJSONObject goodsInfoVo = (EasyJSONObject) object;
                            int goodsId = goodsInfoVo.getInt("goodsId");
                            EasyJSONArray giftVoList = goodsInfoVo.getSafeArray("giftVoList");

                            List<GiftItem> giftItemList = new ArrayList<>();
                            for (Object object2 : giftVoList) {
                                EasyJSONObject giftVo = (EasyJSONObject) object2;

                                GiftItem giftItem = (GiftItem) EasyJSONBase.jsonDecode(GiftItem.class, giftVo.toString());
                                giftItemList.add(giftItem);
                            }

                            goodsInfo.goodsId = goodsId;
                            goodsInfo.commonId = commonId;
                            goodsInfo.goodsFullSpecs = goodsInfoVo.getSafeString("goodsFullSpecs");
                            goodsInfo.specValueIds = goodsInfoVo.getSafeString("specValueIds");
                            goodsInfo.goodsPrice0 = goodsInfoVo.getDouble("goodsPrice0");
                            goodsInfo.price = Util.getSkuPrice(goodsInfoVo);
                            SLog.info("__goodsInfo.price[%s], goodsInfoVo[%s]", goodsInfo.price, goodsInfoVo.toString());
                            goodsInfo.imageSrc = goodsInfoVo.getSafeString("imageSrc");
                            goodsInfo.goodsStorage = goodsInfoVo.getInt("goodsStorage");
                            goodsInfo.reserveStorage = goodsInfoVo.getInt("reserveStorage");
                            goodsInfo.limitAmount = goodsInfoVo.getInt("limitAmount");
                            goodsInfo.unitName = unitName;

                            goodsInfoMap.put(goodsId, goodsInfo);

                            SkuGalleryItem skuGalleryItem = new SkuGalleryItem(
                                    goodsId,
                                    StringUtil.normalizeImageUrl(goodsInfo.imageSrc),
                                    goodsInfoVo.getSafeString("goodsSpecString"),
                                    goodsInfo.goodsPrice0,
                                    goodsInfo.specValueIds
                            );

                            skuGalleryItemList.add(skuGalleryItem);
                        }
                        if (viewPagerFragment != null) {
                            viewPagerFragment.updateMap(goodsInfoMap);
                        }
                        populateData();

                        canViewSkuImage = true;
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        }
    }

    private void populateData() {
        SLog.info("specList.size[%d]", specList.size());

        // 規格的數量
        specCount = specList.size();
        SLog.info("specCount[%d]", specCount);
        selSpecValueIdList = new ArrayList<>(specCount);
        selSpecButtonList = new ArrayList<>(specCount);
        for (int i = 0; i < specCount; i++) {
            if (specValueIdList != null && i < specValueIdList.size()) {
                selSpecValueIdList.add(specValueIdList.get(i));
            } else {
                selSpecValueIdList.add(0);
            }

            selSpecButtonList.add(null);  // 先全部初始化為null
        }

        int position = 0; // 哪一類規格
        LinearLayout llSpecContainer = findViewById(R.id.ll_spec_container);
        for (Spec spec : specList) {
            LinearLayout llSpec = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_spec,
                    null, false);
            TextView tvSpecName = llSpec.findViewById(R.id.tv_spec_name);
            tvSpecName.setText(spec.specName);

            int index = 0; // 一類規格裏面的具體值
            int currSpecValueId = selSpecValueIdList.get(position);  // 當前選中的specValueId
            FlowLayout flSpecButtonContainer = llSpec.findViewById(R.id.fl_spec_button_container);
            for (SpecValue specValue : spec.specValueList) {

                TextView button = new TextView(context);
                boolean isSelected = false;
                if ((currSpecValueId != 0 && specValue.specValueId == currSpecValueId) || // 如果有傳specValueIdList的話，選中相等的
                        (currSpecValueId == 0 && index == 0) // 如果沒有傳specValueIdList的話，默認選中第1個
                ) {
                    setButtonSelected(button, true);
                    isSelected = true;
                } else {
                    setButtonSelected(button, false);
                }

                button.setText(specValue.specValueName);
                button.setTextSize(14);

                button.setPadding(Util.dip2px(context, 16), 0, Util.dip2px(context, 16), 0);

                if (isSelected) {
                    // 如果是開始的選中狀態，記錄一下
                    selSpecButtonList.set(position, button);
                    selSpecValueIdList.set(position, specValue.specValueId); // 這個也必須再次記錄一下，如果外面沒傳進來specValueIdList的話
                }

                SpecButtonData specButtonData = new SpecButtonData(position, spec.specId, specValue.specValueId, specValue.imageSrc, isSelected);
                specButtonData.index = index;
                button.setTag(specButtonData);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SpecButtonData currData = (SpecButtonData) view.getTag();
                        if (currData.isSelected) {
                            // 如果已經選中，重復點擊，不處理
                            SLog.info("如果已經選中，重復點擊，不處理");
                            return;
                        } else {
                            SLog.info("選中第%d個", specButtonData.index);
                        }

                        // 前一個選中的按鈕
                        TextView prevButton = selSpecButtonList.get(currData.position);
                        if (prevButton == null) {
                            return;
                        }
                        SpecButtonData prevData = (SpecButtonData) prevButton.getTag();
                        prevData.isSelected = false;
                        currData.isSelected = true;

                        // 當前選中的按鈕
                        TextView currButton = (TextView) view;
                        // 將前一個選中的按鈕的邊框變灰，當前選中的變為高亮色
                        setButtonSelected(prevButton, false);
                        setButtonSelected(currButton, true);

                        selSpecValueIdList.set(currData.position, currData.specValueId);
                        selSpecButtonList.set(currData.position, currButton);

                        int goodsId = getSelectedGoodsId();
                        updateCurrGoodsId(goodsId);
                    }
                });
                specValueIdButtonMap.put(specValue.specValueId, button);

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Util.dip2px(context, 32));
                button.setGravity(Gravity.CENTER);
                flSpecButtonContainer.addView(button, layoutParams);
                ++index;
            }
            llSpecContainer.addView(llSpec);
            ++position;
        }
        int goodsId = getSelectedGoodsId();
        updateCurrGoodsId(goodsId);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    private void setButtonSelected(TextView button, boolean selected) {
        if (selected) {
            button.setTextColor(context.getColor(R.color.tw_blue));
            button.setBackgroundResource(R.drawable.spec_item_selected_bg);
        } else {
            button.setTextColor(context.getColor(R.color.tw_black));
            button.setBackgroundResource(R.drawable.spec_item_unselected_bg);
        }
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    /**
     * 添加到購物袋
     */
    private void addToCart() {
        // 當前選中的goodsId
        int goodsId = getSelectedGoodsId();
        int buyNum = abQuantity.getValue();

        Util.changeCartContent(context, goodsId, buyNum, data -> {
            ToastUtil.success(context, "添加購物袋成功");
            dismiss();
        });
    }

    /**
     * 直接購買
     */
    private void buy() {
        if (Config.PROD) {
            HashMap<String, Object> analyticsDataMap = new HashMap<>();
            analyticsDataMap.put("commonId", commonId);
            MobclickAgent.onEventObject(TwantApplication.Companion.get(), UmengAnalyticsActionName.GOODS_BUY, analyticsDataMap);
        }

        if (!User.isLogin()) {
            dismiss();
            Util.showLoginFragment();
            return;
        }

        dismiss();

        // 當前選中的goodsId
        int goodsId = getSelectedGoodsId();
        // [{"buyNum":21,"goodsId":446},{"buyNum":2,"goodsId":445}]
        EasyJSONObject buyItem = EasyJSONObject.generate(
                "buyNum", abQuantity.getValue(),
                "goodsId", goodsId);

        if (goId != Constant.INVALID_GO_ID) {
            try {
                buyItem.set("goId", goId);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }

        EasyJSONArray easyJSONArray = EasyJSONArray.generate(buyItem);

        SLog.info("groupBuyMode[%s]", groupBuyMode);
        Util.startFragment(NewConfirmOrderFragment.newInstance(0, easyJSONArray.toString(), groupBuyMode ? Constant.TRUE_INT : Constant.FALSE_INT,
                goId, Constant.INVALID_BARGAIN_OPEN_ID));
    }

    private void selectSpecs() {
        dismiss();

        // 當前選中的goodsId
        int goodsId = getSelectedGoodsId();
        EasyJSONObject easyJSONObject = EasyJSONObject.generate(
                "goodsId", goodsId,
                "quantity", abQuantity.getValue());

        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SELECT_SPECS, easyJSONObject.toString());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_title_padding || id == R.id.btn_close) {
            dismiss();
        }else if (id==R.id.sku_image){
            /*
            if (viewPagerFragment == null) {
                viewPagerFragment = ViewPagerFragment.newInstance(null);
            }
            viewPagerFragment.updateMap(goodsInfoMap);
            int goodsId= specValueIdMap.get(getSelectedSpecValueIdStr());
            viewPagerFragment.setStartPage(goodsId);

            Util.startFragment(viewPagerFragment);
            dismiss();
             */

            if (!canViewSkuImage) {
                return;
            }
            Util.startFragment(SkuImageFragment.newInstance(goodsInfo.goodsId, skuGalleryItemList, this, this));
            dismiss();
        } else if (id == R.id.btn_ok&&goodsInfo != null) {
            if (goodsInfo.getFinalStorage() > 0) {
                if (action == Constant.ACTION_ADD_TO_CART) {
                    if (Config.PROD) {
                        HashMap<String, Object> analyticsDataMap = new HashMap<>();
                        analyticsDataMap.put("commonId", goodsInfo.commonId);
                        MobclickAgent.onEventObject(TwantApplication.Companion.get(), UmengAnalyticsActionName.GOODS_ADD_TO_CART, analyticsDataMap);
                    }

                    addToCart();
                } if (action == Constant.ACTION_BUY) {
                    SLog.info("購買商品 limitBuy %d",limitBuy);
                    if (limitBuy < 0) {
                        ToastUtil.error(context, getResources().getString(R.string.out_of_buy_limit));
                    } else if(limitBuy==0) {
                        buy();
                    } else if (limitBuy > 0) {
                        buy();
                    }
                } else {
                    // 選擇規格
                    selectSpecs();
                }
            } else {
                // 如果沒有庫存，跳轉到【到貨通知】
                Util.startFragment(ArrivalNoticeFragment.newInstance(goodsInfo.commonId, goodsInfo.goodsId));
                dismiss();
            }
        }
    }

    /**
     * 獲取當前選中的goodsId
     * @return 成功 - 返回商品Id
     *         失敗 - 返回0
     */
    private int getSelectedGoodsId() {
        String specValueIds = getSelectedSpecValueIdStr();
        SLog.info("specValueIds[%s]", specValueIds);

        Integer goodsId = specValueIdMap.get(specValueIds);
        if (goodsId == null) {
            SLog.info("goodsId[%d]", goodsId);
            return noSpecGoodsId;
        }
        return goodsId;
    }

    private String getSelectedSpecValueIdStr() {
        return StringUtil.implode(",", selSpecValueIdList);
    }

    /**
     * 更新當前選中的GoodsId
     */
    private void updateCurrGoodsId(int goodsId) {
        // 更新圖片的顯示
        goodsInfo = goodsInfoMap.get(goodsId);
        if (goodsInfo == null) { // 如果商品没有规格，会来到这里
            SLog.info("Error!找不到goodsId:" + goodsId);

            for (Map.Entry<Integer, GoodsInfo> entry : goodsInfoMap.entrySet()) {
                goodsId = entry.getKey();
                noSpecGoodsId = goodsId;
                SLog.info("goodsId[%d]", goodsId);
                goodsInfo = goodsInfoMap.get(goodsId);
            }

            if (goodsInfo == null) {
                ToastUtil.error(context, "Error!找不到goodsId:" + goodsId);
                return;
            }
        }

        String imageSrc = goodsInfo.imageSrc;
        if (!StringUtil.isEmpty(imageSrc)) {
            Glide.with(context).load(imageSrc).centerCrop().into(skuImage);
        }

        int finalStorage = goodsInfo.getFinalStorage();
        SLog.info("goodsInfo.price[%s]", goodsInfo.price);
        if (groupBuyMode && goodsInfo.isGroup == Constant.TRUE_INT) {
            tvPrice.setText(StringUtil.formatPrice(context, goodsInfo.groupPrice, 0) + " (原價: " + StringUtil.formatPrice(context, goodsInfo.goodsPrice0, 0) + ")");
        } else {
            tvPrice.setText(StringUtil.formatPrice(context, goodsInfo.price, 0));
        }
        tvGoodsStorage.setText("( 庫存: " + finalStorage + goodsInfo.unitName + " )");

        // 限定購買的數量
        outOfMaxValueReason = "購買數量不能大于庫存數量";
        SLog.info("finalStorage[%d], limitAmount[%d]", finalStorage, goodsInfo.limitAmount);
        //任務4346 庫存為0時禁止點擊
        if (finalStorage == 0) {
            abQuantity.setZero();
        }
        int maxValue = finalStorage;
        if (limitBuy > 0  // limitAmount 大于0才表示有效
                && maxValue > limitBuy) {
            maxValue = limitBuy;
            outOfMaxValueReason = String.format("商品限購，最多在買%d%s", limitBuy, goodsInfo.unitName);
            tvBuyLimit.setText(context.getString(R.string.text_buy_limit) + ": " + limitBuy + goodsInfo.unitName);
            tvBuyLimit.setVisibility(VISIBLE);
        } else if (limitBuy < 0) {
            maxValue = 1;
            outOfMaxValueReason = getResources().getString(R.string.out_of_buy_limit);
        } else {
            //對原有限購邏輯兼容
            if (goodsInfo.limitAmount > 0&&discountState==2) {
                tvBuyLimit.setText(context.getString(R.string.text_buy_limit) + ": " + goodsInfo.limitAmount + goodsInfo.unitName);
                tvBuyLimit.setVisibility(VISIBLE);
                if (maxValue > goodsInfo.limitAmount) {
                    maxValue = goodsInfo.limitAmount;
                    outOfMaxValueReason = String.format("每人限購%d%s", goodsInfo.limitAmount, goodsInfo.unitName);
                }
            } else {
                tvBuyLimit.setVisibility(INVISIBLE);
            }

        }
        SLog.info("maxValue[%d]", maxValue);
        abQuantity.setMaxValue(maxValue, new AdjustButton.OutOfValueCallback() {
            @Override
            public void outOfValue() {
                ToastUtil.error(context, outOfMaxValueReason);
            }
        });

        // 如果有庫存，則將默認的購買數量設置為1
        if (finalStorage > 0 && maxValue > 0){
            SLog.info("MINVALUE[%d], MAXVALUE[%d]", abQuantity.minValue, abQuantity.maxValue);
            abQuantity.setValue(1);
        }


        if (finalStorage > 0) {
            if (action == Constant.ACTION_ADD_TO_CART) {
                btnOk.setText(R.string.text_want_to_add_to_cart);
            } else if (action == Constant.ACTION_BUY) {
                if (groupBuyMode && goodsInfo.isGroup == Constant.TRUE_INT) {
                    btnOk.setText("想拼團");
                } else {
                    btnOk.setText(R.string.text_want_to_buy);
                }
            } else if (action == Constant.ACTION_SELECT_SPEC) {
                btnOk.setText(R.string.ok);
            }
        } else {
            btnOk.setText(R.string.text_arrival_notice);
        }
    }

    @Override
    public void onSimpleCall(Object data) {
        EasyJSONObject easyJSONObject = (EasyJSONObject) data;
        try {
            int type = easyJSONObject.getInt("type");
            if (type == PopupType.SELECT_SKU_IMAGE.ordinal()) {
                int goodsId = easyJSONObject.getInt("goodsId");
                String specValueIds = easyJSONObject.getSafeString("specValueIds");

                SLog.info("goodsId[%d]", goodsId);

                updateCurrGoodsId(goodsId);

                // 取消之前按鈕的選中狀態
                for (TextView button : selSpecButtonList) {
                    SpecButtonData currData = (SpecButtonData) button.getTag();
                    setButtonSelected(button, false);
                    currData.isSelected = false;
                }

                // 設置當前按鈕的選中狀態
                String[] specValueIdArr = specValueIds.split(",");
                for (int i = 0; i < specValueIdArr.length; i++) {
                    int specValueId = Integer.parseInt(specValueIdArr[i]);
                    TextView button = specValueIdButtonMap.get(specValueId);
                    setButtonSelected(button, true);

                    SpecButtonData currData = (SpecButtonData) button.getTag();
                    currData.isSelected = true;

                    // 如果是開始的選中狀態，記錄一下
                    selSpecButtonList.set(currData.position, button);
                    selSpecValueIdList.set(currData.position, currData.specValueId);
                }
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
}
