package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PackageListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.PackageItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                    Util.startFragment(ExpressQueryFragment.newInstance());
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
            pop();
        }
    }

    private void loadPackageData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        String type;
        if (packageType == PACKAGE_TYPE_MY_SEND) {
            type = "consigner";
        } else {
            type = "consignee";
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "type", type);

        SLog.info("params[%s]", params.toString());
        Api.getUI(Api.PATH_SEARCH_LOGISTICS, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    PackageItem packageItem = new PackageItem();
                    packageItem.itemType = PackageItem.ITEM_TYPE_SEARCH;
                    packageItemList.add(packageItem);

                    packageItem = new PackageItem();
                    packageItem.itemType = PackageItem.ITEM_TYPE_INFO;
                    packageItemList.add(packageItem);

                    packageItem = new PackageItem();
                    packageItem.itemType = PackageItem.ITEM_TYPE_INFO;
                    packageItemList.add(packageItem);

                    adapter.setNewData(packageItemList);
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
