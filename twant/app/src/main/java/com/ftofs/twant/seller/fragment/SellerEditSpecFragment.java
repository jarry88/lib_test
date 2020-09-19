package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.entity.CustomActionData;
import com.gzp.lib_common.base.BaseFragment;
import com.ftofs.twant.interfaces.CustomActionCallback;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.seller.adapter.SellerSpecValueListAdapter;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecListItem;
import com.ftofs.twant.seller.entity.SellerSpecValueListItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 添加或編輯規格組
 * @author zwm
 */
public class SellerEditSpecFragment extends BaseFragment implements View.OnClickListener {
    int action; // 動作：表示添加還是編輯

    RecyclerView rvList;
    SellerSpecValueListAdapter adapter;
    EditText etSpecName;  // 規格名稱

    CustomActionCallback customActionCallback;

    int specId;
    String specName;
    List<SellerSpecValueListItem> specValueList = new ArrayList<>();

    List<String> specValueNameList = new ArrayList<>();

    public static SellerEditSpecFragment newInstance(int action, SellerSpecListItem sellerSpecListItem, CustomActionCallback customActionCallback) {
        Bundle args = new Bundle();

        SellerEditSpecFragment fragment = new SellerEditSpecFragment();
        fragment.setArguments(args);
        fragment.action = action;
        fragment.customActionCallback = customActionCallback;

        if (action == Constant.ACTION_EDIT) {
            fragment.specId = sellerSpecListItem.specId;
            fragment.specName = sellerSpecListItem.specName;

            SLog.info("sellerSpecItemList[%d]", sellerSpecListItem.sellerSpecItemList.size());
            for (SellerSpecItem sellerSpecItem : sellerSpecListItem.sellerSpecItemList) {
                fragment.specValueList.add(new SellerSpecValueListItem(
                        SellerSpecValueListItem.ITEM_TYPE_NORMAL, sellerSpecItem.id, sellerSpecItem.name));
            }
        }

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_edit_spec, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);

        TextView tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        tvFragmentTitle.setText(action == Constant.ACTION_ADD ? "添加規格" : "編輯規格");

        if (action == Constant.ACTION_ADD) {
            // 如果是添加規格的話，先添加3個空的規格值編輯框
            for (int i = 0; i < 3; i++) {
                specValueList.add(new SellerSpecValueListItem(SellerSpecValueListItem.ITEM_TYPE_NORMAL, 0, ""));
            }
        }

        // 底部的【添加規格】按鈕
        specValueList.add(new SellerSpecValueListItem(SellerSpecValueListItem.ITEM_TYPE_FOOTER, 0, null));

        etSpecName = view.findViewById(R.id.et_spec_name);
        if (!StringUtil.isEmpty(specName)) {
            etSpecName.setText(specName);
        }

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new SellerSpecValueListAdapter(specValueList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                if (id == R.id.btn_remove) { // 刪除規格值
                    removeItem(position);
                } else if (id == R.id.btn_add_spec_value) { // 添加規格值
                    if (specValueList.size() >= (35 + 1)) { // 額外加1是因為最後那個【添加規格】按鈕
                        ToastUtil.error(_mActivity, "每個規格最多只能添加35個規格值");
                        return;
                    }
                    int targetIndex = specValueList.size() - 1;
                    specValueList.add(targetIndex, new SellerSpecValueListItem(SellerSpecValueListItem.ITEM_TYPE_NORMAL, 0, ""));
                    adapter.notifyItemInserted(targetIndex);
                }
            }
        });
        rvList.setAdapter(adapter);
    }

    private void removeItem(int position) {
        new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                .asCustom(new TwConfirmPopup(_mActivity, "確定要刪除嗎?", null, new OnConfirmCallback() {
            @Override
            public void onYes() {
                SLog.info("onYes");
                specValueList.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onNo() {
                SLog.info("onNo");
            }
        })).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {
            saveSpec();
        }
    }


    private void saveSpec() {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            String specName = etSpecName.getText().toString().trim();
            if (StringUtil.isEmpty(specName)) {
                ToastUtil.error(_mActivity, "請填寫【規格名稱】");
                return;
            }

            specValueNameList.clear();

            boolean first = true;
            StringBuilder specValueNamesSB = new StringBuilder();
            for (SellerSpecValueListItem item : specValueList) {
                if (item.getItemType() == SellerSpecValueListItem.ITEM_TYPE_NORMAL && !StringUtil.isEmpty(item.specValueName)) {
                    if (!first) {
                        specValueNamesSB.append(",");
                    }
                    first = false;

                    specValueNameList.add(item.specValueName);
                    specValueNamesSB.append(item.specValueName);
                }
            }

            String specValueNames = specValueNamesSB.toString();
            SLog.info("specValueNames[%s]", specValueNames);
            if (StringUtil.isEmpty(specValueNames)) {
                ToastUtil.error(_mActivity, "請添加規格");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "specName", specName,
                    "specValueNames", specValueNames
            );
            String url;
            if (action == Constant.ACTION_ADD) {
                url = Api.PATH_SELLER_ADD_SPEC;
            } else {
                url = Api.PATH_SELLER_EDIT_SPEC;
                params.set("specId", specId);
            }
            SLog.info("params[%s]", params.toString());

            Api.postUI(url, params, new UICallback() {
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

                        ToastUtil.success(_mActivity, "保存成功");

                        if (action == Constant.ACTION_ADD) { // 添加规格
                            specId = responseObj.getInt("datas.specAndValueVo.specId");
                        }


                        SellerSpecListItem result = new SellerSpecListItem(SellerSpecListItem.SPEC_TYPE_CUSTOM, specId, specName);
                        for (String specValueName : specValueNameList) {
                            SellerSpecItem sellerSpecItem = new SellerSpecItem();
                            sellerSpecItem.type = SellerSpecItem.TYPE_SPEC_VALUE;
                            sellerSpecItem.name = specValueName;
                            result.sellerSpecItemList.add(sellerSpecItem);
                        }

                        if (customActionCallback != null) {
                            CustomActionData data = new CustomActionData(
                                    action == Constant.ACTION_ADD ? CustomAction.CUSTOM_ACTION_SELLER_ADD_SPEC.ordinal() : CustomAction.CUSTOM_ACTION_SELLER_EDIT_SPEC.ordinal(),
                                    result);
                            customActionCallback.onCustomActionCall(data);
                        }

                        hideSoftInputPop();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

