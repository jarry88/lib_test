package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.entity.Spec;
import com.ftofs.twant.entity.SpecButtonData;
import com.ftofs.twant.entity.SpecValue;
import com.ftofs.twant.fragment.ArrivalNoticeFragment;
import com.ftofs.twant.fragment.ConfirmOrderFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.nex3z.flowlayout.FlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 規格選擇框
 * 有些商品沒有規格，只有一個goodsId
 * @author zwm
 */
public class SpecSelectPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    int action;
    List<Spec> specList;
    ImageView skuImage;
    TextView tvPrice;
    TextView tvGoodsStorage;
    TextView tvBuyLimit;

    TextView btnOk;

    Map<String, Integer> specValueIdMap;
    Map<Integer, GoodsInfo> goodsInfoMap;

    // 當前選中的商品的信息
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


    /**
     *
     * @param context
     * @param action
     * @param specList
     * @param specValueIdMap 根據逗號拼接的specValueId字符串，定位出商品的goodsId的映射
     * @param specValueIdList 傳進來的當前選中的specValueId列表，如果為null，則默認都選中第一項
     * @param quantity 数量
     * @param goodsInfoMap
     */
    public SpecSelectPopup(@NonNull Context context, int action, List<Spec> specList,
                           Map<String, Integer> specValueIdMap, List<Integer> specValueIdList,
                           int quantity, Map<Integer, GoodsInfo> goodsInfoMap) {
        super(context);

        this.context = context;
        this.action = action;
        this.specList = specList;
        this.specValueIdMap = specValueIdMap;
        this.goodsInfoMap = goodsInfoMap;
        this.quantity = quantity;

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

        // 加入購物籃
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        if (action == Constant.ACTION_ADD_TO_CART) {
            btnOk.setBackgroundResource(R.drawable.red_button);
        } else if (action == Constant.ACTION_BUY) {
            btnOk.setBackgroundResource(R.drawable.red_button);
        } else if (action == Constant.ACTION_SELECT_SPEC) {
            btnOk.setBackgroundResource(R.drawable.red_button);
        }

        skuImage = findViewById(R.id.sku_image);
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

        SLog.info("specList.size[%d]", specList.size());

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
                button.setTag(specButtonData);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SpecButtonData currData = (SpecButtonData) view.getTag();
                        if (currData.isSelected) {
                            // 如果已經選中，重復點擊，不處理
                            SLog.info("如果已經選中，重復點擊，不處理");
                            return;
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
     * 添加到購物籃
     */
    private void addToCart() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        // 當前選中的goodsId
        int goodsId = getSelectedGoodsId();

        EasyJSONArray buyData = EasyJSONArray.generate(EasyJSONObject.generate(
                "buyNum", abQuantity.getValue(),
                "goodsId", goodsId));

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "buyData", buyData.toString(),
                "clientType", Constant.CLIENT_TYPE_ANDROID);

        SLog.info("params[%s]", params.toString());

        Api.postUI(Api.PATH_ADD_CART, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(context, responseObj)) {
                    return;
                }

                ToastUtil.success(context, "添加購物籃成功");

                // 通知更新購物籃紅點提示
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_UPDATE_TOOLBAR_RED_BUBBLE, null);
                dismiss();
            }
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
        } else if (id == R.id.btn_ok) {
            if (goodsInfo.getFinalStorage() > 0) {
                if (action == Constant.ACTION_ADD_TO_CART) {
                    addToCart();
                } if (action == Constant.ACTION_BUY) {
                    buy();
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

    /**
     * 克隆一份當前選中的規格Id列表
     * @return
     */
    private List<Integer> getSelectedSpecValueIdList() {
        List<Integer> idList = new ArrayList<>();
        for (Integer specValueId : selSpecValueIdList) {
            idList.add(specValueId);
        }

        return idList;
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
            SLog.info("Error!找不到商品Id:" + selectedSpecValueIdStr);
            ToastUtil.error(context, "Error!找不到商品Id:" + selectedSpecValueIdStr);
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
        if (goodsInfo.limitAmount > 0) {
            tvBuyLimit.setText(context.getString(R.string.text_buy_limit) + ": " + goodsInfo.limitAmount + goodsInfo.unitName);
            tvBuyLimit.setVisibility(VISIBLE);
        } else {
            tvBuyLimit.setVisibility(INVISIBLE);
        }

        // 限定購買的數量
        outOfMaxValueReason = "購買數量不能大于庫存數量";
        SLog.info("finalStorage[%d], limitAmount[%d]", finalStorage, goodsInfo.limitAmount);

        int maxValue = finalStorage;
        if (goodsInfo.limitAmount > 0  // limitAmount 大于0才表示有效
                && maxValue > goodsInfo.limitAmount) {
            maxValue = goodsInfo.limitAmount;
            outOfMaxValueReason = String.format("每人限購%d%s", goodsInfo.limitAmount, goodsInfo.unitName);
        }
        SLog.info("maxValue[%d]", maxValue);
        abQuantity.setMaxValue(maxValue, new AdjustButton.OutOfValueCallback() {
            @Override
            public void outOfValue() {
                ToastUtil.error(context, outOfMaxValueReason);
            }
        });


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
