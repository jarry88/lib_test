package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.EditorResultInterface;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.entity.SellerGoodsPicVo;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecMapItem;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;
import com.ftofs.twant.seller.widget.SellerSelectSpecPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 【賣家】編輯規格與圖片
 * @author zwm
 */
public class SellerEditGoodsSpecFragment extends BaseFragment
        implements View.OnClickListener, OnSelectedListener, EditorResultInterface {
    int commonId;

    EditText etGoodsVideoUrl;

    // 當前選中的規格列表
    EasyJSONArray specJsonVoList;

    // 未選中的規格組Id與規格組信息的映射關係
    Map<Integer, SellerSpecMapItem> sellerSpecMap = new HashMap<>();

    // 已選中的規格組Id與規格組信息的映射關係
    List<SellerSpecMapItem> sellerSelectedSpecList = new ArrayList<>();

    // SpecId 與 SpecName的映射
    Map<Integer, String> specMap = new HashMap<>();
    // SpecValueId 與 SpecValueName的映射
    Map<Integer, String> specValueMap = new HashMap<>();

    // 規格值Id的字符串拼接(例5,8,12)與SKU信息的映射關係，規格值Id按升序排列
    Map<String, SellerSpecPermutation> specValueIdStringMap = new HashMap<>();
    // 規格值Id的字符串拼接(例5,8,12)的列表，規格值Id按升序排列
    List<String> specValueIdStringList = new ArrayList<>();

    // colorId與圖片列表的映射關係
    Map<Integer, List<SellerGoodsPicVo>> colorImageMap = new HashMap<>();

    LinearLayout llSelectedSpecContainer;

    // 原先已經添加的specValue集合
    Set<Integer> initSpecValueSet = new HashSet<>();

    // 标记SKU数据是否已经加载
    boolean isSkuDataLoaded = false;


    public static SellerEditGoodsSpecFragment newInstance(int commonId, EasyJSONArray specJsonVoList) {
        Bundle args = new Bundle();

        SellerEditGoodsSpecFragment fragment = new SellerEditGoodsSpecFragment();
        fragment.setArguments(args);
        fragment.commonId = commonId;
        fragment.specJsonVoList = specJsonVoList;
        SLog.info("specJsonVoList[%s]", specJsonVoList);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_edit_goods_spec, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etGoodsVideoUrl = view.findViewById(R.id.et_goods_video_url);
        llSelectedSpecContainer = view.findViewById(R.id.ll_selected_spec_container);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_save, this);
        Util.setOnClickListener(view, R.id.btn_add_spec, this);
        Util.setOnClickListener(view, R.id.btn_view_sku_detail, this);

        try {
            for (Object object : specJsonVoList) {
                EasyJSONObject specJsonVo = (EasyJSONObject) object;
                SellerSpecMapItem item = new SellerSpecMapItem();

                item.selected = true;
                item.specId = specJsonVo.getInt("specId");
                item.specName = specJsonVo.getSafeString("specName");
                EasyJSONArray specValueList = specJsonVo.getSafeArray("specValueList");
                for (Object object2 : specValueList) {
                    EasyJSONObject specValue = (EasyJSONObject) object2;

                    SellerSpecItem sellerSpecItem = new SellerSpecItem();
                    int specValueId = specValue.getInt("specValueId");
                    sellerSpecItem.id = specValueId;
                    sellerSpecItem.name = specValue.getSafeString("specValueName");
                    sellerSpecItem.selected = true;
                    sellerSpecItem.type = SellerSpecItem.TYPE_SPEC_VALUE;

                    initSpecValueSet.add(specValueId); // 初始選中的specValueId

                    item.sellerSpecItemList.add(sellerSpecItem);
                }

                sellerSelectedSpecList.add(item);
            }

            updateSelectedSpecView();
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        loadSellerSpecList();
    }

    /**
     * 更新選中的規格列表的顯示
     */
    private void updateSelectedSpecView() {
        // 先清空所有子View
        llSelectedSpecContainer.removeAllViews();

        for (SellerSpecMapItem sellerSpecMapItem : sellerSelectedSpecList) {
            ViewGroup selectedSpecItemView = (ViewGroup) LayoutInflater.from(_mActivity)
                    .inflate(R.layout.seller_selected_spec_value_desc, llSelectedSpecContainer, false);

            TextView textView = new TextView(_mActivity);
            textView.setTextSize(13);
            textView.setText("【" + sellerSpecMapItem.specName + "】   ");
            selectedSpecItemView.addView(textView, 0);


            for (SellerSpecItem sellerSpecItem : sellerSpecMapItem.sellerSpecItemList) {
                textView = new TextView(_mActivity);
                textView.setTextSize(13);
                textView.setText(sellerSpecItem.name + "   ");
                selectedSpecItemView.addView(textView, 1);
            }

            View btnEdit = selectedSpecItemView.findViewById(R.id.btn_edit);
            btnEdit.setTag(sellerSpecMapItem.specId);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int specId = (int) v.getTag();
                    SLog.info("specId[%d]", specId);


                    SellerSpecMapItem item = sellerSpecMap.get(specId);
                    if (item == null) {
                        return;
                    }

                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new SellerSelectSpecPopup(_mActivity, Constant.ACTION_EDIT, specId, item.sellerSpecItemList, null, SellerEditGoodsSpecFragment.this))
                            .show();
                }
            });

            View btnRemove = selectedSpecItemView.findViewById(R.id.btn_remove);
            btnRemove.setTag(sellerSpecMapItem.specId);
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int specId = (int) v.getTag();
                    SLog.info("specId[%d]", specId);


                    SellerSpecMapItem item = sellerSpecMap.get(specId);
                    if (item == null) {
                        return;
                    }

                    new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                            // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                            .setPopupCallback(new XPopupCallback() {
                                @Override
                                public void onShow() {
                                }
                                @Override
                                public void onDismiss() {
                                }
                            }).asCustom(new TwConfirmPopup(_mActivity, "確定要刪除規格嗎?",null, "確定", "取消",new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            deleteSpec(specId);
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    })).show();
                }
            });

            LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = Util.dip2px(_mActivity, 8);
            llSelectedSpecContainer.addView(selectedSpecItemView, layoutParams);
        }
    }

    /**
     * 刪除規格
     * @param specId
     */
    private void deleteSpec(int specId) {
        SellerSpecMapItem item = sellerSpecMap.get(specId);
        if (item == null) {
            return;
        }

        item.selected = false;
        for (SellerSpecItem specItem : item.sellerSpecItemList) {
            specItem.selected = false;
        }

        int i = 0;
        for (; i < sellerSelectedSpecList.size(); i++) {
            item = sellerSelectedSpecList.get(i);
            if (item.specId == specId) {
                break;
            }
        }
        if (i < sellerSelectedSpecList.size()) {
            sellerSelectedSpecList.remove(i);
        }

        updateSelectedSpecView();
    }

    private void loadSkuData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "commonId", commonId
        );

        SLog.info("url[%s], params[%s]", Api.PATH_SELLER_GET_SKU_INFO, params);
        Api.postUI(Api.PATH_SELLER_GET_SKU_INFO, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    // 處理規格信息
                    EasyJSONArray goodsJsonVoList = responseObj.getSafeArray("datas.goodsJsonVoList");
                    for (Object object : goodsJsonVoList) {
                        EasyJSONObject goodsJsonVo = (EasyJSONObject) object;

                        String specValueIds = goodsJsonVo.getSafeString("specValueIds");
                        specValueIds = StringUtil.sortSpecValueIdString(specValueIds);
                        SellerSpecPermutation permutation = specValueIdStringMap.get(specValueIds);
                        if (permutation != null) {
                            /*
                                public double price;
                                public String goodsSN = "";  // 商品編號
                                public int storage; // 庫存
                                public int reserved; // 預存庫存
                             */
                            permutation.price = goodsJsonVo.getDouble("goodsPrice0");
                            permutation.goodsSN = goodsJsonVo.getSafeString("goodsSerial");
                            permutation.storage = goodsJsonVo.getInt("goodsStorage");
                            permutation.reserved = goodsJsonVo.getInt("reserveStorage");
                        }
                    }

                    // 處理圖片信息
                    EasyJSONArray goodsPicVoList = responseObj.getSafeArray("datas.goodsPicVoList");
                    for (Object object : goodsPicVoList) {
                        EasyJSONObject goodsPicVo = (EasyJSONObject) object;

                        int colorId = goodsPicVo.getInt("colorId");
                        List<SellerGoodsPicVo> sellerGoodsPicVoList = colorImageMap.get(colorId);
                        if (sellerGoodsPicVoList == null) {
                            sellerGoodsPicVoList = new ArrayList<>();
                            colorImageMap.put(colorId, sellerGoodsPicVoList);
                        }

                        SellerGoodsPicVo sellerGoodsPicVo = new SellerGoodsPicVo();
                        /*
                        public int colorId;
                        public String colorName;
                        public String imageName;  // 遠端圖片路徑（相對路徑）
                        public String absolutePath;  // 本地圖片路徑（值不為空,表示待上傳）
                        public int imageSort;
                        public int isDefault;
                         */
                        sellerGoodsPicVo.colorId = colorId;
                        sellerGoodsPicVo.colorName = specValueMap.get(colorId);
                        sellerGoodsPicVo.imageName = goodsPicVo.getSafeString("imageName");
                        sellerGoodsPicVo.imageSort = goodsPicVo.getInt("imageSort");
                        sellerGoodsPicVo.isDefault = goodsPicVo.getInt("isDefault");

                        sellerGoodsPicVoList.add(sellerGoodsPicVo);
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadSellerSpecList() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        SLog.info("url[%s], params[%s]", Api.PATH_SELLER_SPEC_LIST, params);
        Api.postUI(Api.PATH_SELLER_SPEC_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }

                    EasyJSONArray specAndValueList = responseObj.getSafeArray("datas.specAndValueList");
                    for (Object object : specAndValueList) {
                        EasyJSONObject specObj = (EasyJSONObject) object;

                        SellerSpecMapItem sellerSpecMapItem = new SellerSpecMapItem();
                        int specId = specObj.getInt("specId");
                        String specName = specObj.getSafeString("specName");

                        specMap.put(specId, specName);

                        sellerSpecMapItem.specId = specId;
                        sellerSpecMapItem.specName = specName;
                        // 判斷這個規格組是否已選擇
                        for (SellerSpecMapItem selectedSpec : sellerSelectedSpecList) {
                            if (selectedSpec.specId == specId) {
                                sellerSpecMapItem.selected = selectedSpec.selected;
                                break;
                            }
                        }

                        EasyJSONArray specValueList = specObj.getSafeArray("specValueList");
                        for (Object object2 : specValueList) {
                            EasyJSONObject specValueObj = (EasyJSONObject) object2;
                            int specValueId = specValueObj.getInt("specValueId");
                            String specValueName = specValueObj.getSafeString("specValueName");

                            specValueMap.put(specValueId, specValueName);

                            SellerSpecItem sellerSpecItem = new SellerSpecItem();
                            sellerSpecItem.type = SellerSpecItem.TYPE_SPEC_VALUE;
                            sellerSpecItem.id = specValueId;
                            sellerSpecItem.name = specValueName;
                            if (initSpecValueSet.contains(specValueId)) {
                                sellerSpecItem.selected = true;
                            }

                            sellerSpecMapItem.sellerSpecItemList.add(sellerSpecItem);
                        }

                        sellerSpecMap.put(specId, sellerSpecMapItem);
                    }

                    generateSpecPermutation();

                    loadSkuData();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_save) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            String goodsVideoUrl = etGoodsVideoUrl.getText().toString().trim();
            if (!StringUtil.isEmpty(goodsVideoUrl) && !StringUtil.isYoutubeUrl(goodsVideoUrl)) {
                ToastUtil.error(_mActivity, "商品視頻鏈接不合規");
                return;
            }

            String url = Api.PATH_SELLER_EDIT_GOODS_DETAIL + Api.makeQueryString(EasyJSONObject.generate(
                 "token", token
            ));


            EasyJSONObject params = EasyJSONObject.generate(
                    "commonId", commonId,
                    "editType", 3,
                    "goodsVideo", goodsVideoUrl
            );

            Pair<Boolean, String> result = saveSpecInfo(params);
            if (!result.first) {
                ToastUtil.error(_mActivity, result.second);
                return;
            }

            SLog.info("url[%s], params[%s]", url, params);

            if (false) {
                return;
            }

            Api.postJsonUi(url, params.toString(), new UICallback() {
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

                        popWithResult();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } else if (id == R.id.btn_add_spec) {
            if (sellerSpecMap.size() < 1) {
                ToastUtil.error(_mActivity, "沒有可用的規格");
                return;
            }

            List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();
            Map<Integer, List<SellerSpecItem>> sellerSpecValueMap = new HashMap<>();

            int selectedCount = 0;
            for (Map.Entry<Integer, SellerSpecMapItem> entry : sellerSpecMap.entrySet()) {
                if (entry.getValue().selected) {
                    selectedCount++;
                    continue;
                }

                SellerSpecItem sellerSpecItem = new SellerSpecItem();
                sellerSpecItem.id = entry.getKey();
                sellerSpecItem.name = entry.getValue().specName;

                sellerSpecItemList.add(sellerSpecItem);
                sellerSpecValueMap.put(entry.getKey(), entry.getValue().sellerSpecItemList);
            }

            if (selectedCount >= 3) {
                ToastUtil.error(_mActivity, "每個商品最多只能添加3個規格");
                return;
            }

            if (sellerSpecItemList.size() < 1) {
                ToastUtil.error(_mActivity, "已添加所有的規格");
                return;
            }

            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new SellerSelectSpecPopup(_mActivity, Constant.ACTION_ADD, 0, sellerSpecItemList, sellerSpecValueMap, this))
                    .show();
        } else if (id == R.id.btn_view_sku_detail) {
            // 查看SKU詳情
            if (sellerSelectedSpecList.size() < 1) {
                ToastUtil.error(_mActivity, "請先添加規格");
                return;
            }

            // 查看是否有选【颜色】规格
            SellerSpecMapItem colorItem = sellerSpecMap.get(Constant.COLOR_SPEC_ID);
            SellerSpecMapItem colorSpecMapItem = null;
            if (colorItem != null && colorItem.selected) { // 如果有選顏色
                colorSpecMapItem = new SellerSpecMapItem();

                colorSpecMapItem.specId = colorItem.specId;
                colorSpecMapItem.specName = colorItem.specName;

                for (SellerSpecItem elem : colorItem.sellerSpecItemList) {
                    if (elem.selected) {
                        colorSpecMapItem.sellerSpecItemList.add(elem);
                    }
                }
            }

            start(SellerSkuEditorFragment.newInstance(this,
                    specValueIdStringList,
                    specValueIdStringMap,
                    colorSpecMapItem,
                    colorImageMap));
        }
    }

    private void popWithResult() {
        hideSoftInputPop();
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("type[%s], id[%d], extra[%s]", type, id, extra);
        try {
            if (type == PopupType.SELLER_SELECT_SPEC) {
                EasyJSONObject dataObj = (EasyJSONObject) extra;
                int specId = dataObj.getInt("specId");
                int action = dataObj.getInt("action");
                EasyJSONArray specValueIdList = dataObj.getArray("specValueIdList");

                // 设置选中状态
                SellerSpecMapItem selectedItem = sellerSpecMap.get(specId);
                if (selectedItem == null) {
                    return;
                }

                selectedItem.selected = true;
                for (SellerSpecItem elem : selectedItem.sellerSpecItemList) {
                    elem.selected = false;
                }

                SellerSpecMapItem item = null;
                if (action == Constant.ACTION_ADD) {
                    item = new SellerSpecMapItem();
                } else {
                    for (SellerSpecMapItem elem : sellerSelectedSpecList) {
                        if (elem.specId == specId) {
                            item = elem;
                            item.sellerSpecItemList.clear();
                            break;
                        }
                    }
                }

                if (item == null) {
                    return;
                }

                item.specId = specId;
                item.specName = specMap.get(specId);
                for (Object object : specValueIdList) {
                    int specValueId = (int) object;

                    SellerSpecItem sellerSpecItem = new SellerSpecItem();
                    sellerSpecItem.id = specValueId;
                    sellerSpecItem.name = specValueMap.get(specValueId);
                    item.sellerSpecItemList.add(sellerSpecItem);

                    for (SellerSpecItem elem : selectedItem.sellerSpecItemList) {
                        if (elem.id == specValueId) {
                            elem.selected = true;
                            break;
                        }
                    }
                }

                if (action == Constant.ACTION_ADD) {
                    sellerSelectedSpecList.add(item);
                }

                updateSelectedSpecView();

                generateSpecPermutation();
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    /**
     * 根據當前已選中的Spec值生成SKU枚舉值
     */
    private void generateSpecPermutation() {
        // 先清空列表
        specValueIdStringList.clear();

        int totalSkuCount = 1;

        for (SellerSpecMapItem item : sellerSelectedSpecList) {
            totalSkuCount *= item.sellerSpecItemList.size();
        }
        SLog.info("totalSkuCount[%d]", totalSkuCount);

        for (int i = 0; i < totalSkuCount; i++) {
            int n = i;
            StringBuilder specValueIdStringSB = new StringBuilder();
            StringBuilder skuDesc = new StringBuilder();  // SKU規格描述
            List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();  // 規格值列表

            int colorId = 0;
            for (int j = 0; j < sellerSelectedSpecList.size(); j++) {
                SellerSpecMapItem sellerSpecMapItem = sellerSelectedSpecList.get(j);

                int index = n % sellerSpecMapItem.sellerSpecItemList.size();
                SellerSpecItem specItem = sellerSpecMapItem.sellerSpecItemList.get(index);
                if (sellerSpecMapItem.specId == Constant.COLOR_SPEC_ID) {
                    colorId = specItem.id;
                }

                sellerSpecItemList.add(specItem);

                specValueIdStringSB.append(specItem.id).append(",");
                skuDesc.append(specItem.name).append("/");

                n /= sellerSpecMapItem.sellerSpecItemList.size();
            }

            SLog.info("skuDesc[%s]", skuDesc.toString());
            String specValueIdString = StringUtil.trim(specValueIdStringSB.toString(), new char[] {','});
            specValueIdString = StringUtil.sortSpecValueIdString(specValueIdString);
            specValueIdStringList.add(specValueIdString);

            SellerSpecPermutation permutation = specValueIdStringMap.get(specValueIdString);
            if (permutation == null) {  // 如果不在Map裏面，則新建一個
                permutation = new SellerSpecPermutation();

                permutation.colorId = colorId;
                permutation.specValueIdString = specValueIdString;
                permutation.specValueNameString = StringUtil.trim(skuDesc.toString(), new char[] {'/'});
                permutation.sellerSpecItemList = sellerSpecItemList;

                specValueIdStringMap.put(specValueIdString, permutation);
            }
        }
    }

    @Override
    public void setEditorResult(Map<String, SellerSpecPermutation> specValueIdStringMap, Map<Integer, List<SellerGoodsPicVo>> colorImageMap) {
        this.specValueIdStringMap = specValueIdStringMap;
        this.colorImageMap = colorImageMap;
        SLog.info("specValueIdStringMap[%s]", Util.specValueIdStringMapToJSONString(specValueIdStringMap));
    }

    /**
     * 保存规格信息
     * @return  错误消息： 如果成功，返回null
     *                   如果失败，返回具体的错误消息
     */

    /**
     * 保存规格信息到params參數
     * @param params
     * @return 結構對 first -- true：保存成功  false：保存失敗
     *               second -- 錯誤消息
     */
    private Pair<Boolean, String> saveSpecInfo(EasyJSONObject params) {
        try {
            SLog.info("sellerSelectedSpecList.size[%d]", sellerSelectedSpecList.size());
            if (sellerSelectedSpecList.size() < 1) {
                return new Pair<>(false, "請添加商品規格");
            }

            /*
            "specJsonVoList": [
                {
                    "specId": 1,
                    "specName": "顔色",
                    "specValueList": [
                        {
                            "specValueId": 16,
                            "specValueName": "白色",
                            "imageSrc": ""
                        },
                        {
                            "specValueId": 18,
                            "specValueName": "粉色",
                            "imageSrc": ""
                        }
                    ]
                },
                {
                    "specId": 2,
                    "specName": "尺碼",
                    "specValueList": [
                        {
                            "specValueId": 35,
                            "specValueName": "XXS",
                            "imageSrc": ""
                        }
                    ]
                }
            ]
             */

            EasyJSONArray specJsonVoList = EasyJSONArray.generate();
            for (SellerSpecMapItem item : sellerSelectedSpecList) {
                EasyJSONArray specValueArr = EasyJSONArray.generate();
                for (SellerSpecItem specItem : item.sellerSpecItemList) {
                    specValueArr.append(
                            EasyJSONObject.generate(
                                    "specValueId", specItem.id,
                                    "specValueName", specItem.name
                            )
                    );
                }

                specJsonVoList.append(EasyJSONObject.generate(
                        "specId", item.specId,
                        "specName", item.specName,
                        "specValueList", specValueArr));
            }
            SLog.info("specJsonVoList[%s]", specJsonVoList.toString());
            params.set("specJsonVoList", specJsonVoList);

            EasyJSONArray goodsJsonVoList = EasyJSONArray.generate();
            // 生成PermutationList
            for (String specValueIdString : specValueIdStringList) {
                SellerSpecPermutation permutation = specValueIdStringMap.get(specValueIdString);
                /*
                "goodsSpecs": "A+B,,,XXS", #規格值 多個用三個英文逗號,,,隔開
            "goodsFullSpecs": "顔色：A+B；尺碼：XXS", #完整規格
            "specValueIds": "16,35", #規格值ID，多個用英文逗號隔開
            "goodsPrice0": 10, #sku價格
            "goodsSerial": "", #商家編號
            "goodsStorage": 20, #縂庫存
            "colorId": 16, #主規格值ID
            "reserveStorage": #預留庫存
                 */
                goodsJsonVoList.append(EasyJSONObject.generate(
                        "specValueIds", permutation.specValueIdString,
                        "goodsPrice0", permutation.price,
                        "goodsSerial", permutation.goodsSN,
                        "goodsStorage", permutation.storage,
                        "colorId", permutation.colorId,
                        "reserveStorage", permutation.reserved
                ));
            }
            params.set("goodsJsonVoList", goodsJsonVoList);


            EasyJSONArray goodsPicVoList = EasyJSONArray.generate();
            for (Map.Entry<Integer, List<SellerGoodsPicVo>> entry: colorImageMap.entrySet()) {
                /*
                {
                    "colorId": 16, #規格值ID
                    "imageName": "image/a8/5a/a85a133391e94416decc6668f5334bdf.jpg", #圖片路徑
                    "imageSort": 0, #圖片排序
                    "isDefault": 1 #1是0否主圖，同一組(colorId相同)規格只有一張主圖
                }
                 */
                List<SellerGoodsPicVo> picVoList = entry.getValue();
                for (SellerGoodsPicVo vo : picVoList) {
                    goodsPicVoList.append(EasyJSONObject.generate(
                            "colorId", vo.colorId,
                            "imageName", vo.imageName,
                            "imageSort", vo.imageSort,
                            "isDefault", vo.isDefault
                    ));
                }

            }
            params.set("goodsPicVoList", goodsPicVoList);

            return new Pair<>(true, null);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));

            return new Pair<>(false, "保存規格數據失敗");
        }
    }
}
