package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.ftofs.twant.seller.adapter.SellerSpecListAdapter;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecListItem;
import com.ftofs.twant.util.LogUtil;
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

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 【賣家】規格組列表
 * @author zwm
 */
public class SellerSpecFragment extends BaseFragment implements View.OnClickListener, CustomActionCallback {
    List<SellerSpecListItem> sellerSpecList = new ArrayList<>();
    RecyclerView rvList;
    SellerSpecListAdapter adapter;

    public static SellerSpecFragment newInstance() {
        Bundle args = new Bundle();

        SellerSpecFragment fragment = new SellerSpecFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_spec, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_add_spec, this);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new SellerSpecListAdapter(_mActivity, R.layout.seller_spec_list_item, sellerSpecList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                SellerSpecListItem item = sellerSpecList.get(position);

                if (id == R.id.btn_delete) {
                    showDeleteSpecConfirm(item.specId);
                } else if (id == R.id.btn_edit) {
                    start(SellerEditSpecFragment.newInstance(Constant.ACTION_EDIT, item, SellerSpecFragment.this));
                }
            }
        });
        rvList.setAdapter(adapter);

        loadSellerSpecList();
    }

    private void showDeleteSpecConfirm(int specId) {
        new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
              .asCustom(new TwConfirmPopup(_mActivity, "確定要刪除嗎?", null, new OnConfirmCallback() {
            @Override
            public void onYes() {
                SLog.info("onYes");
                deleteSpec(specId);
            }

            @Override
            public void onNo() {
                SLog.info("onNo");
            }
        }))
                .show();
    }

    private void deleteSpec(int specId) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_DELETE_SPEC + "/" + specId;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );
        SLog.info("url[%s], params[%s]", url, params.toString());

        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    ToastUtil.success(_mActivity, "刪除成功");

                    // 根据specId查找位置
                    int index = 0;
                    for (; index < sellerSpecList.size(); index++) {
                        SellerSpecListItem item = sellerSpecList.get(index);
                        if (item.specId == specId) {
                            break;
                        }
                    }

                    if (index == sellerSpecList.size()) { // NOT FOUND
                        return;
                    }

                    sellerSpecList.remove(index);
                    adapter.notifyItemRemoved(index);
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

        String url = Api.PATH_SELLER_SPEC_LIST;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        SLog.info("url[%s], params[%s]", url, params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }

                    EasyJSONArray specAndValueList = responseObj.getSafeArray("datas.specAndValueList");
                    for (Object object : specAndValueList) {
                        EasyJSONObject specObj = (EasyJSONObject) object;

                        int storeId = specObj.getInt("storeId");
                        int specId = specObj.getInt("specId");
                        String specName = specObj.getSafeString("specName");


                        SellerSpecListItem sellerSpecListItem = new SellerSpecListItem(
                                storeId == Constant.FALSE_INT ? SellerSpecListItem.SPEC_TYPE_SYSTEM : SellerSpecListItem.SPEC_TYPE_CUSTOM,
                                specId, specName
                        );


                        EasyJSONArray specValueList = specObj.getSafeArray("specValueList");
                        for (Object object2 : specValueList) {
                            EasyJSONObject specValueObj = (EasyJSONObject) object2;
                            int specValueId = specValueObj.getInt("specValueId");
                            String specValueName = specValueObj.getSafeString("specValueName");

                            SellerSpecItem sellerSpecItem = new SellerSpecItem();
                            sellerSpecItem.type = SellerSpecItem.TYPE_SPEC_VALUE;
                            sellerSpecItem.id = specValueId;
                            sellerSpecItem.name = specValueName;

                            sellerSpecListItem.sellerSpecItemList.add(sellerSpecItem);
                        }

                        sellerSpecList.add(sellerSpecListItem);
                    }

                    adapter.setNewData(sellerSpecList);
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
        } else if (id == R.id.btn_add_spec) {
            start(SellerEditSpecFragment.newInstance(Constant.ACTION_ADD, null, this));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onCustomActionCall(CustomActionData customActionData) {
        int action = customActionData.action;
        if (action == CustomAction.CUSTOM_ACTION_SELLER_EDIT_SPEC.ordinal()) {
            // 根据specId定出位置
            SellerSpecListItem item = (SellerSpecListItem) customActionData.data;
            int specId = item.specId;
            int index = 0;
            for (; index < sellerSpecList.size(); index++) {
                if (sellerSpecList.get(index).specId == specId) {
                    break;
                }
            }

            if (index == sellerSpecList.size()) { // NOT FOUND
                return;
            }

            sellerSpecList.set(index, item);
            adapter.notifyItemChanged(index);
        } else if (action == CustomAction.CUSTOM_ACTION_SELLER_ADD_SPEC.ordinal()) {
            SellerSpecListItem item = (SellerSpecListItem) customActionData.data;
            sellerSpecList.add(item);
            adapter.notifyItemInserted(sellerSpecList.size() - 1);
            rvList.scrollToPosition(sellerSpecList.size() - 1);  // 滑动到最后
        }
    }
}






