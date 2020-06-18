package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecMapItem;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;
import com.ftofs.twant.seller.widget.SellerSelectSpecPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 【賣家】編輯規格與圖片
 * @author zwm
 */
public class SellerEditSpecFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    int commonId;

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

    // 規格值Id的字符串拼接(例5,12,8)與SKU信息的映射關係
    Map<String, SellerSpecPermutation> specValueIdStringMap = new HashMap<>();
    // 規格值Id的字符串拼接(例5,12,8)的列表
    List<String> specValueIdStringList = new ArrayList<>();

    LinearLayout llSelectedSpecContainer;

    public static SellerEditSpecFragment newInstance(int commonId, EasyJSONArray specJsonVoList) {
        Bundle args = new Bundle();

        SellerEditSpecFragment fragment = new SellerEditSpecFragment();
        fragment.setArguments(args);
        fragment.commonId = commonId;
        fragment.specJsonVoList = specJsonVoList;
        SLog.info("specJsonVoList[%s]", specJsonVoList);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_edit_spec, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llSelectedSpecContainer = view.findViewById(R.id.ll_selected_spec_container);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_save, this);
        Util.setOnClickListener(view, R.id.btn_add_spec, this);

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
                    sellerSpecItem.id = specValue.getInt("specValueId");
                    sellerSpecItem.name = specValue.getSafeString("specValueName");
                    sellerSpecItem.selected = true;
                    sellerSpecItem.type = SellerSpecItem.TYPE_SPEC_VALUE;

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
                            .asCustom(new SellerSelectSpecPopup(_mActivity, Constant.ACTION_EDIT, specId, item.sellerSpecItemList, null, SellerEditSpecFragment.this))
                            .show();
                }
            });

            LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = Util.dip2px(_mActivity, 8);
            llSelectedSpecContainer.addView(selectedSpecItemView, layoutParams);
        }
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

                            sellerSpecMapItem.sellerSpecItemList.add(sellerSpecItem);
                        }

                        sellerSpecMap.put(specId, sellerSpecMapItem);
                    }
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

            String path = Api.PATH_SELLER_EDIT_GOODS_DETAIL + Api.makeQueryString(EasyJSONObject.generate(
                 "token", token
            ));


            EasyJSONObject params = EasyJSONObject.generate();
            Api.postJsonUi(path, params.toString(), new UICallback() {
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

            for (Map.Entry<Integer, SellerSpecMapItem> entry : sellerSpecMap.entrySet()) {
                if (entry.getValue().selected) {
                    continue;
                }

                SellerSpecItem sellerSpecItem = new SellerSpecItem();
                sellerSpecItem.id = entry.getKey();
                sellerSpecItem.name = entry.getValue().specName;

                sellerSpecItemList.add(sellerSpecItem);
                sellerSpecValueMap.put(entry.getKey(), entry.getValue().sellerSpecItemList);
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
        }
    }

    private void popWithResult() {
        hideSoftInputPop();
    }


    private void addSpec() {
        if (sellerSpecMap.size() < 1) {
            ToastUtil.error(_mActivity, "沒有可用的規格");
            return;
        }

        List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();
        Map<Integer, List<SellerSpecItem>> sellerSpecValueMap = new HashMap<>();

        for (Map.Entry<Integer, SellerSpecMapItem> entry : sellerSpecMap.entrySet()) {
            if (entry.getValue().selected) {
                continue;
            }

            SellerSpecItem sellerSpecItem = new SellerSpecItem();
            sellerSpecItem.id = entry.getKey();
            sellerSpecItem.name = entry.getValue().specName;

            sellerSpecItemList.add(sellerSpecItem);
            sellerSpecValueMap.put(entry.getKey(), entry.getValue().sellerSpecItemList);
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
}
