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
import com.ftofs.twant.adapter.AddrListAdapter;
import com.ftofs.twant.adapter.OrderListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 地址管理Fragment
 * @author zwm
 */
public class AddrManageFragment extends BaseFragment implements View.OnClickListener {
    List<AddrItem> addrItemList = new ArrayList<>();
    BaseQuickAdapter adapter;

    public static AddrManageFragment newInstance() {
        Bundle args = new Bundle();

        AddrManageFragment fragment = new AddrManageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addr_management, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_add_address, this);

        RecyclerView rvAddrList = view.findViewById(R.id.rv_addr_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvAddrList.setLayoutManager(layoutManager);
        adapter = new AddrListAdapter(R.layout.addr_item, addrItemList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                int id = view.getId();
                if (id == R.id.img_default_addr_indicator) {
                    SLog.info("設為默認地址");

                    try {
                        AddrItem item = addrItemList.get(position);
                        String token = User.getToken();
                        if (StringUtil.isEmpty(token)) {
                            return;
                        }
                        EasyJSONObject params = item.toEasyJSONObject();


                        params.set("token", token);
                        params.set("isDefault", 1);
                        SLog.info("params[%s]", params);
                        Api.postUI(Api.PATH_EDIT_ADDRESS, params, new UICallback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseStr = response.body().string();
                                SLog.info("responseStr[%s]", responseStr);
                                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                                if (ToastUtil.checkError(_mActivity, responseObj)) {
                                    return;
                                }

                                // 設置成功，重新刷新數據
                                int index = 0;
                                for (AddrItem addrItem : addrItemList) {
                                    if (index == position) {
                                        addrItem.isDefault = 1;
                                    } else {
                                        addrItem.isDefault = 0;
                                    }
                                    ++index;
                                }
                                adapter.setNewData(addrItemList);
                            }
                        });
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                    }
                } else if (id == R.id.btn_edit) {
                    SLog.info("編輯地址");
                } else if (id == R.id.btn_delete) {
                    SLog.info("刪除地址");
                    AddrItem item = addrItemList.get(position);
                    new XPopup.Builder(getContext())
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
                            }).asConfirm("確定要刪除地址嗎?", item.areaInfo + " " + item.address,
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    deleteAddress(position);
                                }
                            }, null, false)
                            .show();
                }
            }
        });
        rvAddrList.setAdapter(adapter);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        loadAddrData();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_add_address) {
            MainFragment mainFragment = MainFragment.getInstance();
            mainFragment.start(AddAddressFragment.newInstance());
        }
    }

    private void loadAddrData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token);
        Api.postUI(Api.PATH_LIST_ADDRESS, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    addrItemList.clear();
                    EasyJSONArray addressList = responseObj.getArray("datas.addressList");
                    for (Object object : addressList) {
                        EasyJSONObject item = (EasyJSONObject) object;

                        int addressId = item.getInt("addressId");
                        String realName = item.getString("realName");
                        List<Integer> areaIdList = new ArrayList<>();
                        for (int i = 1; i <= 4; ++i) {
                            areaIdList.add(item.getInt("areaId") + i);
                        }
                        int areaId = item.getInt("areaId");
                        String areaInfo = item.getString("areaInfo");
                        String address = item.getString("address");
                        String mobileAreaCode = item.getString("mobileAreaCode");
                        String mobPhone = item.getString("mobPhone");
                        int isDefault = item.getInt("isDefault");


                        addrItemList.add(new AddrItem(addressId, realName, areaIdList, areaId, areaInfo, address, mobileAreaCode, mobPhone, isDefault));
                    }

                    adapter.setNewData(addrItemList);
                } catch (Exception e) {

                }
            }
        });
    }

    private void deleteAddress(final int position) {
        int addressId = addrItemList.get(position).addressId;

        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "addressId", addressId);
        Api.postUI(Api.PATH_DELETE_ADDRESS, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                ToastUtil.show(_mActivity, "刪除成功");

                addrItemList.remove(position);
                adapter.setNewData(addrItemList);
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
