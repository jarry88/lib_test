package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PackageListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.PackageItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 查包裹Fragment
 * @author zwm
 */
public class QueryPackageFragment extends BaseFragment implements View.OnClickListener {
    public static final int PACKAGE_TYPE_MY_SEND = 1;
    public static final int PACKAGE_TYPE_MY_RECEIVE = 2;

    int packageType = PACKAGE_TYPE_MY_SEND;

    RecyclerView rvPackageList;
    List<PackageItem> packageItemList = new ArrayList<>();

    PackageListAdapter adapter;

    String queryOrderNumber; // 待查詢的單號

    public static QueryPackageFragment newInstance() {
        Bundle args = new Bundle();

        QueryPackageFragment fragment = new QueryPackageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_package, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleTabManager simpleTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                SLog.info("id[%d]", id);

                if (isRepeat) {
                    return;
                }

                if (id == R.id.btn_my_send) {
                    SLog.info("我的寄件");
                    packageType = PACKAGE_TYPE_MY_SEND;
                } else {
                    SLog.info("我的收件");
                    packageType = PACKAGE_TYPE_MY_RECEIVE;
                }

                loadPackageData();
            }
        };

        simpleTabManager.add(view.findViewById(R.id.btn_my_send));
        simpleTabManager.add(view.findViewById(R.id.btn_my_receive));

        Util.setOnClickListener(view, R.id.btn_back, this);

        rvPackageList = view.findViewById(R.id.rv_package_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvPackageList.setLayoutManager(layoutManager);
        adapter = new PackageListAdapter(packageItemList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_search_package) {
                    startForResult(ExpressQueryFragment.newInstance(queryOrderNumber), RequestCode.LOGISTICS_QUERY.ordinal());
                } else if (id == R.id.btn_copy) {
                    // 復制快遞客戶單號
                    PackageItem packageItem = packageItemList.get(position);
                    String text = packageItem.customerOrderNumber;
                    if (!StringUtil.isEmpty(text)) {
                        ClipboardUtils.copyText(_mActivity, text);
                        ToastUtil.success(_mActivity, "復制成功");
                    }
                }
            }
        });
        rvPackageList.setAdapter(adapter);

        loadPackageData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    private void loadPackageData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();


        String type;
        if (packageType == PACKAGE_TYPE_MY_SEND) {
            type = "consigner";
        } else {
            type = "consignee";
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "type", type);

        if (!StringUtil.isEmpty(queryOrderNumber)) {
            try {
                params.set("number", queryOrderNumber);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }

        SLog.info("params[%s]", params.toString());
        Api.getUI(Api.PATH_SEARCH_LOGISTICS, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    packageItemList.clear();

                    PackageItem packageItem = new PackageItem();
                    packageItem.itemType = PackageItem.ITEM_TYPE_SEARCH;
                    if (StringUtil.isEmpty(queryOrderNumber)) {
                        packageItem.searchPackageHint = getString(R.string.search_package_hint);
                    } else {
                        packageItem.searchPackageHint = queryOrderNumber;
                    }
                    packageItemList.add(packageItem);

                    EasyJSONArray resultList = responseObj.getSafeArray("datas.resultList");
                    for (Object object : resultList) {
                        EasyJSONObject result = (EasyJSONObject) object;

                        packageItem = new PackageItem();
                        packageItem.itemType = PackageItem.ITEM_TYPE_INFO;
                        packageItem.state = result.getInt("state");
                        packageItem.updateTime = result.getSafeString("updateTime");
                        packageItem.customerOrderNumber = result.getSafeString("customerOrderNumber");
                        packageItem.originalOrderNumber = result.getSafeString("originalOrderNumber");
                        packageItem.createTime = result.getSafeString("createTime");

                        packageItem.consignerName = result.getSafeString("consignerName");
                        packageItem.consignerPhone = result.getSafeString("consignerPhone");
                        packageItem.consignerAddress = result.getSafeString("consignerAddress");

                        packageItem.consigneeName = result.getSafeString("consigneeName");
                        packageItem.consigneePhone = result.getSafeString("consigneePhone");
                        packageItem.consigneeAddress = result.getSafeString("consigneeAddress");

                        packageItemList.add(packageItem);
                    }


                    adapter.setNewData(packageItemList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

        SLog.info("requestCode[%d], resultCode[%d]", requestCode, resultCode);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        queryOrderNumber = data.getString("queryOrderNumber"); // 更新要查詢的快遞單號
        SLog.info("queryOrderNumber[%s]", queryOrderNumber);

        loadPackageData();
    }
}
