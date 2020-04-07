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
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.entity.Spec;
import com.ftofs.twant.entity.SpecButtonData;
import com.ftofs.twant.entity.SpecValue;
import com.ftofs.twant.fragment.ArrivalNoticeFragment;
import com.ftofs.twant.fragment.ConfirmOrderFragment;
import com.ftofs.twant.fragment.ViewPagerFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.SloganView;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.nex3z.flowlayout.FlowLayout;

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
public class SpecSelectPopup extends BottomPopupView implements View.OnClickListener {
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

    // 調整數量
    AdjustButton abQuantity;
    int quantity;  // 外面傳進來的數量初始值

    String outOfMaxValueReason; // 購買數量超過庫存數或限購數的提示
    private boolean isShowing;
    private List<String> currGalleryImageList;
    private int currPosition;


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
     */
    public SpecSelectPopup(@NonNull Context context, int action, int commonId, List<Spec> specList,
                           Map<String, Integer> specValueIdMap, List<Integer> specValueIdList,
                           int quantity, Map<Integer, GoodsInfo> goodsInfoMap, List<String> viewPagerFragment, int limitBuy) {
        super(context);

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
            btnOk.setBackgroundResource(R.drawable.blue_button);
        } else if (action == Constant.ACTION_BUY) {
            btnOk.setBackgroundResource(R.drawable.blue_button);
        } else if (action == Constant.ACTION_SELECT_SPEC) {
            btnOk.setBackgroundResource(R.drawable.blue_button);
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
                ToastUtil.error(context, "購買數量不能小于1");
            }
        });

        if (specList != null) {
            populateData();
        } else {
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
                        EasyJSONArray goodsSpecValueArr = (EasyJSONArray) EasyJSONArray.parse(goodsSpecValues);
                        for (Object object : goodsSpecValueArr) {
                            EasyJSONObject mapItem = (EasyJSONObject) object;
                            SLog.info("kkkkey[%s], vvvalue[%s]", mapItem.getSafeString("specValueIds"), mapItem.getInt("goodsId"));
                            // 有些沒有規格的產品，只有一個goodsId，且specValueIds字符串為空串
                            specValueIdMap.put(mapItem.getSafeString("specValueIds"), mapItem.getInt("goodsId"));
                        }

                        goodsInfoMap = new HashMap<>();
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
                        }
                        if (viewPagerFragment != null) {
                            viewPagerFragment.updateMap(goodsInfoMap);
                        }
                        populateData();
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
            if (specValueIdList != null && specValueIdList.size() > 0) {
                selSpecValueIdList.add(specValueIdList.get(i));
            } else {
                selSpecValueIdList.add(0);
            }

            selSpecButtonList.add(null);
        }

        int position = 0;
        LinearLayout llSpecContainer = findViewById(R.id.ll_spec_container);
        for (Spec spec : specList) {
            LinearLayout llSpec = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_spec,
                    null, false);
            TextView tvSpecName = llSpec.findViewById(R.id.tv_spec_name);
            tvSpecName.setText(spec.specName);

            int index = 0;
            int currSpecValueId = selSpecValueIdList.get(position);  // 當前選中的specValueId
            FlowLayout flSpecButtonContainer = llSpec.findViewById(R.id.fl_spec_button_container);
            for (SpecValue specValue : spec.specValueList) {

                TextView button = new TextView(context);
                boolean isSelected = false;
                if ((currSpecValueId != 0 && specValue.specValueId == currSpecValueId) || // 如果有傳specValueIdList的話，選中相等的
                        (currSpecValueId == 0 && index == 0) // 如果沒有傳specValueIdList的話，默認選中第1個
                ) {
                    button.setBackgroundResource(R.drawable.spec_item_selected_bg);
                    button.setTextColor(context.getColor(R.color.tw_blue));
                    isSelected = true;
                } else {
                    button.setBackgroundResource(R.drawable.spec_item_unselected_bg);
                    button.setTextColor(context.getColor(R.color.tw_black));
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
                            SLog.info("選中第%d",specButtonData.index);
                        }

                        // 前一個選中的按鈕
                        TextView prevButton = selSpecButtonList.get(currData.position);
                        SpecButtonData prevData = (SpecButtonData) prevButton.getTag();
                        prevData.isSelected = false;
                        currData.isSelected = true;

                        // 當前選中的按鈕
                        TextView currButton = (TextView) view;
                        // 將前一個選中的按鈕的邊框變灰，當前選中的變為高亮色
                        prevButton.setTextColor(context.getColor(R.color.tw_black));
                        prevButton.setBackgroundResource(R.drawable.spec_item_unselected_bg);
                        currButton.setTextColor(context.getColor(R.color.tw_blue));
                        currButton.setBackgroundResource(R.drawable.spec_item_selected_bg);

                        selSpecValueIdList.set(currData.position, currData.specValueId);
                        selSpecButtonList.set(currData.position, currButton);
                        currPosition=((SpecButtonData) currButton.getTag()).index;
                        updateCurrGoodsId();
                    }
                });

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Util.dip2px(context, 32));
                button.setGravity(Gravity.CENTER);
                flSpecButtonContainer.addView(button, layoutParams);
                ++index;
            }
            llSpecContainer.addView(llSpec);
            ++position;
        }
        updateCurrGoodsId();
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
        dismiss();

        // 當前選中的goodsId
        int goodsId = getSelectedGoodsId();
        // [{"buyNum":21,"goodsId":446},{"buyNum":2,"goodsId":445}]
        EasyJSONArray easyJSONArray = EasyJSONArray.generate(EasyJSONObject.generate(
                "buyNum", abQuantity.getValue(),
                "goodsId", goodsId));

        Util.startFragment(ConfirmOrderFragment.newInstance(0, easyJSONArray.toString()));
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
            if (viewPagerFragment == null) {
                viewPagerFragment = ViewPagerFragment.newInstance(null);
            }
            viewPagerFragment.updateMap(goodsInfoMap);
            int goodsId= specValueIdMap.get(getSelectedSpecValueIdStr());
            viewPagerFragment.setStartPage(goodsId);

            Util.startFragment(viewPagerFragment);
            dismiss();
        } else if (id == R.id.btn_ok) {
            if (goodsInfo.getFinalStorage() > 0) {
                if (action == Constant.ACTION_ADD_TO_CART) {
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
     * @return
     */
    private int getSelectedGoodsId() {
        boolean first = true;
        StringBuilder specValueIds = new StringBuilder();
        for (Integer specValueId : selSpecValueIdList) {
            if (!first) {
                specValueIds.append(",");
            }
            specValueIds.append(specValueId);
            first = false;
        }
        SLog.info("specValueIds[%s]", specValueIds);

        int goodsId = specValueIdMap.get(specValueIds.toString());
        SLog.info("goodsId[%d]", goodsId);
        return goodsId;
    }

    private String getSelectedSpecValueIdStr() {
        return StringUtil.implode(",", selSpecValueIdList);
    }

    /**
     * 更新當前選中的GoodsId
     */
    private void updateCurrGoodsId() {
        String selectedSpecValueIdStr = getSelectedSpecValueIdStr();
        SLog.info("selectedSpecValueIdStr[%s]", selectedSpecValueIdStr);
        Integer goodsId = specValueIdMap.get(selectedSpecValueIdStr);
        SLog.info("goodsId[%s]", goodsId);
        if (goodsId == null) {
            SLog.info("Error!找不到產品Id:" + selectedSpecValueIdStr);
            ToastUtil.error(context, "Error!找不到產品Id:" + selectedSpecValueIdStr);
            return;
        }

        // 更新圖片的顯示
        goodsInfo = goodsInfoMap.get(goodsId);
        if (goodsInfo == null) {
            SLog.info("Error!找不到goodsId:" + goodsId);
            ToastUtil.error(context, "Error!找不到goodsId:" + goodsId);
            return;
        }

        String imageSrc = goodsInfo.imageSrc;
        if (!StringUtil.isEmpty(imageSrc)) {
            Glide.with(context).load(imageSrc).centerCrop().into(skuImage);
        }

        int finalStorage = goodsInfo.getFinalStorage();
        SLog.info("goodsInfo.price[%s]", goodsInfo.price);
        tvPrice.setText(StringUtil.formatPrice(context, goodsInfo.price, 0));
        tvGoodsStorage.setText("( 庫存: " + finalStorage + goodsInfo.unitName + " )");
        if (limitBuy > 0) {
            tvBuyLimit.setText(context.getString(R.string.text_buy_limit) + ": " + limitBuy + goodsInfo.unitName);
            tvBuyLimit.setVisibility(VISIBLE);
        } else {
            tvBuyLimit.setVisibility(INVISIBLE);
        }

        // 限定購買的數量
        outOfMaxValueReason = "購買數量不能大于庫存數量";
        SLog.info("finalStorage[%d], limitAmount[%d]", finalStorage, goodsInfo.limitAmount);

        int maxValue = finalStorage;
        if (limitBuy > 0  // limitAmount 大于0才表示有效
                && maxValue > limitBuy) {
            maxValue = limitBuy;
            outOfMaxValueReason = String.format("商品限購，最多在買%d%s", limitBuy, goodsInfo.unitName);
        }
        if (limitBuy < 0) {
            maxValue = 1;
            outOfMaxValueReason = getResources().getString(R.string.out_of_buy_limit);
        }
        SLog.info("maxValue[%d]", maxValue);
        abQuantity.setMaxValue(maxValue, new AdjustButton.OutOfValueCallback() {
            @Override
            public void outOfValue() {
                ToastUtil.error(context, outOfMaxValueReason);
            }
        });
//        abQuantity.


        if (finalStorage > 0) {
            if (action == Constant.ACTION_ADD_TO_CART) {
                btnOk.setText(R.string.text_want_to_add_to_cart);
            } else if (action == Constant.ACTION_BUY) {
                btnOk.setText(R.string.text_want_to_buy);
            } else if (action == Constant.ACTION_SELECT_SPEC) {
                btnOk.setText(R.string.ok);
            }
        } else {
            btnOk.setText(R.string.text_arrival_notice);
        }
    }
}
