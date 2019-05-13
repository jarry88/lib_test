package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Spec;
import com.ftofs.twant.entity.SpecButtonData;
import com.ftofs.twant.entity.SpecValue;
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
import okhttp3.Response;

/**
 * 規格選擇框
 * @author zwm
 */
public class SpecSelectPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    List<Spec> specList;
    int selectedIndex;  // 當前選中的索引
    ImageView skuImage;
    Map<String, Integer> specValueIdMap;

    // 規格的數量
    int specCount;
    // 當前選中規格按鈕的引用
    List<TextView> selSpecButtonList;
    // 當前選中的SpecValueId
    List<Integer> selSpecValueIdList;

    public SpecSelectPopup(@NonNull Context context, List<Spec> specList, Map<String, Integer> specValueIdMap) {
        super(context);

        this.context = context;
        this.specList = specList;
        this.specValueIdMap = specValueIdMap;

        // 規格的數量
        specCount = specList.size();
        SLog.info("specCount[%d]", specCount);
        selSpecValueIdList = new ArrayList<>(specCount);
        selSpecButtonList = new ArrayList<>(specCount);
        for (int i = 0; i < specCount; i++) {
            selSpecValueIdList.add(0);
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

        // 加入購物車
        findViewById(R.id.btn_ok).setOnClickListener(this);

        skuImage = findViewById(R.id.sku_image);

        SLog.info("specList.size[%d]", specList.size());
        if (specList.size() < 1) {
            return;
        }

        // 找到第1個規格的照片
        Spec firstSpec = specList.get(0);
        String firstImageSrc = firstSpec.specValueList.get(0).imageSrc;
        SLog.info("firstImageSrc[%s]", firstImageSrc);
        Glide.with(this).load(firstImageSrc).into(skuImage);

        int position = 0;
        LinearLayout llSpecContainer = findViewById(R.id.ll_spec_container);
        for (Spec spec : specList) {
            LinearLayout llSpec = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_spec,
                    null, false);
            TextView tvSpecName = llSpec.findViewById(R.id.tv_spec_name);
            tvSpecName.setText(spec.specName);

            int index = 0;
            FlowLayout flSpecButtonContainer = llSpec.findViewById(R.id.fl_spec_button_container);
            for (SpecValue specValue : spec.specValueList) {
                TextView button = new TextView(context);
                boolean isSelected = false;
                if (index == selectedIndex) {
                    button.setBackgroundResource(R.drawable.spec_item_selected_bg);
                    isSelected = true;
                } else {
                    button.setBackgroundResource(R.drawable.spec_item_unselected_bg);
                }

                button.setText(specValue.specValueName);
                button.setTextSize(14);

                int color = getResources().getColor(R.color.tw_black, null);
                button.setTextColor(color);
                button.setPadding(Util.dip2px(context, 16), Util.dip2px(context, 6),
                        Util.dip2px(context, 16), Util.dip2px(context, 6));

                if (isSelected) {
                    // 如果是開始的選中狀態，記錄一下
                    selSpecButtonList.set(position, button);
                    selSpecValueIdList.set(position, specValue.specValueId);
                }

                SpecButtonData specButtonData = new SpecButtonData(position, spec.specId, specValue.specValueId, isSelected);
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
                        prevButton.setBackgroundResource(R.drawable.spec_item_unselected_bg);
                        currButton.setBackgroundResource(R.drawable.spec_item_selected_bg);

                        selSpecValueIdList.set(currData.position, currData.specValueId);
                        selSpecButtonList.set(currData.position, currButton);
                    }
                });

                flSpecButtonContainer.addView(button);
                ++index;
            }
            llSpecContainer.addView(llSpec);
            ++position;
        }

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


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_title_padding || id == R.id.btn_close) {
            dismiss();
        } else if (id == R.id.btn_ok) {
            // 添加購物車
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            // 當前選中的goodsId
            int goodsId = getSelectedGoodsId();

            EasyJSONArray buyData = EasyJSONArray.generate(EasyJSONObject.generate("buyNum", 1, "goodsId", goodsId));

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "buyData", buyData.toString(),
                    "clientType", Constant.CLIENT_TYPE_ANDROID);

            Api.postUI(Api.PATH_ADD_CART, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(context, responseObj)) {
                        return;
                    }

                    ToastUtil.show(context, "添加購物車成功");
                    dismiss();
                }
            });
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
}
