package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.RealNameListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.RealNameListItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.RealNameInstructionPopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 實名認證信息列表
 * @author zwm
 */
public class RealNameListFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvList;
    RealNameListAdapter realNameListAdapter;
    List<RealNameListItem> realNameItemList = new ArrayList<>();

    public static RealNameListFragment newInstance() {
        Bundle args = new Bundle();

        RealNameListFragment fragment = new RealNameListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_real_name_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));

        realNameListAdapter = new RealNameListAdapter(R.layout.real_name_list_item, realNameItemList);
        realNameListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                RealNameListItem item = realNameItemList.get(position);

                if (id == R.id.btn_edit_real_name_info) {
                    startForResult(AddRealNameInfoFragment.newInstance(Constant.ACTION_EDIT, item), RequestCode.REAL_NAME_INFO.ordinal());
                } else if (id == R.id.btn_delete_real_name_info) {
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
                            }).asCustom(new TwConfirmPopup(_mActivity, "確定要刪除嗎?", null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");
                            deleteRealNameInfo(position);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    })).show();
                }
            }
        });

        // 設置空頁面
        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.empty_real_name_info_placeholder, null, false);
        // 添加按鈕事件處理
        emptyView.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(AddRealNameInfoFragment.newInstance(Constant.ACTION_ADD, null), RequestCode.REAL_NAME_INFO.ordinal());
            }
        });

        // 查看實名認證說明
        emptyView.findViewById(R.id.btn_view_real_name_prompt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new RealNameInstructionPopup(_mActivity))
                        .show();
            }
        });

        realNameListAdapter.setEmptyView(emptyView);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_add_real_name_info, this);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
               "token", token
        );

        Api.getUI(Api.PATH_REAL_NAME_LIST, params, new UICallback() {
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

                    realNameItemList.clear();
                    EasyJSONArray consigneeNameAuthList = responseObj.getSafeArray("datas.consigneeNameAuthList");
                    for (Object object : consigneeNameAuthList) {
                        EasyJSONObject itemObj = (EasyJSONObject) object;
                        RealNameListItem item = new RealNameListItem();
                        item.authId = itemObj.getInt("authId");
                        item.name = itemObj.getSafeString("authConsigneeName");
                        item.idNum = itemObj.getSafeString("idCartNumber");

                        realNameItemList.add(item);
                    }
                    rvList.setAdapter(realNameListAdapter);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void deleteRealNameInfo(int position) {
        RealNameListItem item = realNameItemList.get(position);
        String token = User.getToken();
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "authId", item.authId
        );

        Api.postUI(Api.PATH_DELETE_REAL_NAME_INFO, params, new UICallback() {
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

                    ToastUtil.success(_mActivity, "刪除成功");
                    realNameItemList.remove(position);
                    realNameListAdapter.notifyDataSetChanged();
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
            pop();
        } else if (id == R.id.btn_add_real_name_info) {
            startForResult(AddRealNameInfoFragment.newInstance(Constant.ACTION_ADD, null), RequestCode.REAL_NAME_INFO.ordinal());
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        SLog.info("onFragmentResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == RequestCode.REAL_NAME_INFO.ordinal()) {
            SLog.info("data[%s]",data.toString());
            boolean reloadData = data.getBoolean("reloadData");
            if (reloadData) { // 如果有變動，則重新加載數據
                loadData();
            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
