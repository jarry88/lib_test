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
import com.ftofs.twant.adapter.AddrListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
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
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                if (id == R.id.btn_use_this_addr) {
                    // 選擇收貨地址，并將地址傳回給上一個Fragment
                    AddrItem addrItem = addrItemList.get(position);
                    pop(addrItem);
                } else if (id == R.id.img_default_addr_indicator) {
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
                                ToastUtil.showNetworkError(_mActivity, e);
                            }

                            @Override
                            public void onResponse(Call call, String responseStr) throws IOException {
                                SLog.info("responseStr[%s]", responseStr);
                                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

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
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                } else if (id == R.id.btn_edit) {
                    SLog.info("編輯地址");
                    AddrItem addrItem = addrItemList.get(position);
                    Util.startFragment(AddAddressFragment.newInstance(Constant.ACTION_EDIT, addrItem));
                } else if (id == R.id.btn_delete) {
                    SLog.info("刪除地址");
                    AddrItem item = addrItemList.get(position);
                    new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                            // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                            .asCustom(new TwConfirmPopup(_mActivity, "確定要刪除地址嗎?", item.areaInfo + " " + item.address, new OnConfirmCallback() {
                                    @Override
                                    public void onYes() {
                                        SLog.info("onYes");
                                        deleteAddress(position);
                                    }

                                    @Override
                                    public void onNo() {
                                        SLog.info("onNo");
                                    }
                                }))
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
            pop(null);
        } else if (id == R.id.btn_add_address) {
            Util.startFragmentForResult(AddAddressFragment.newInstance(Constant.ACTION_ADD, null), RequestCode.ADD_ADDRESS.ordinal());
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

                    addrItemList.clear();
                    EasyJSONArray addressList = responseObj.getSafeArray("datas.addressList");
                    for (Object object : addressList) {
                        EasyJSONObject item = (EasyJSONObject) object;

                        int addressId = item.getInt("addressId");
                        String realName = item.getSafeString("realName");
                        List<Integer> areaIdList = new ArrayList<>();
                        for (int i = 1; i <= 4; ++i) {
                            areaIdList.add(item.getInt("areaId" + i));
                        }
                        int areaId = item.getInt("areaId");
                        String areaInfo = item.getSafeString("areaInfo");
                        String address = item.getSafeString("address");
                        String mobileAreaCode = item.getSafeString("mobileAreaCode");
                        String mobPhone = item.getSafeString("mobPhone");
                        int isDefault = item.getInt("isDefault");

                        addrItemList.add(new AddrItem(addressId, realName, areaIdList, areaId, areaInfo, address, mobileAreaCode, mobPhone, isDefault));
                    }

                    adapter.setNewData(addrItemList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                ToastUtil.success(_mActivity, "刪除成功");

                addrItemList.remove(position);
                adapter.setNewData(addrItemList);
            }
        });
    }

    /**
     * pop操作的統一入口
     * @param addrItem 選中哪個地址項
     */
    private void pop(AddrItem addrItem) {
        Bundle bundle = new Bundle();

        bundle.putString("from", AddrManageFragment.class.getName());
        bundle.putBoolean("isNoAddress", addrItemList.size() == 0);  // 標記是否已經刪除所有收貨地址
        bundle.putParcelable("addrItem", addrItem);

        setFragmentResult(RESULT_OK, bundle);
        hideSoftInputPop();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop(null);
        return true;
    }
}
