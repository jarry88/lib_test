package com.ftofs.twant.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AddFriendSearchResultAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.appserver.AppServiceImpl;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.AddFriendSearchResultItem;
import com.github.richardwrq.krouter.annotation.Inject;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.base.callback.IntentCallBack;
import com.gzp.lib_common.service.AppService;
import com.gzp.lib_common.service.ConstantsPath;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 添加朋友Fragment
 * @author zwm
 */
public class AddFriendFragment extends BaseFragment implements View.OnClickListener {
    EditText etKeyword;

    AddFriendSearchResultAdapter adapter;

    List<AddFriendSearchResultItem> addFriendSearchResultItemList = new ArrayList<>();
    public static AddFriendFragment newInstance() {
        Bundle args = new Bundle();

        AddFriendFragment fragment = new AddFriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friend, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_my_qr_code, this);
        Util.setOnClickListener(view, R.id.btn_scan_qr_code, this);

        etKeyword = view.findViewById(R.id.et_keyword);
        etKeyword.setOnEditorActionListener((v, actionId, event) -> {
            SLog.info("onEditorAction");
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = etKeyword.getText().toString().trim();

                if (StringUtil.isEmpty(keyword)) {
                    ToastUtil.error(_mActivity, "請輸入" + getString(R.string.input_search_friend_hint));
                    return true;
                }

                hideSoftInput();
                doSearch(keyword);
                return true;
            }
            return false;
        });

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        adapter = new AddFriendSearchResultAdapter(R.layout.add_friend_search_result_item, addFriendSearchResultItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AddFriendSearchResultItem item = addFriendSearchResultItemList.get(position);
                start(MemberInfoFragment.newInstance(item.memberName));
            }
        });
        rvList.setAdapter(adapter);
    }

    private void doSearch(String keyword) {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "keyword", keyword);
        SLog.info("params[%s]", params);

        Api.postUI(Api.PATH_SEARCH_USER, params, new UICallback() {
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

                    addFriendSearchResultItemList.clear();
                    EasyJSONArray memberList = responseObj.getSafeArray("datas.memberList");
                    for (Object object : memberList) {
                        EasyJSONObject member = (EasyJSONObject) object;

                        AddFriendSearchResultItem item = new AddFriendSearchResultItem();
                        item.memberName = member.getSafeString("memberName");
                        item.avatar = member.getSafeString("avatar");
                        item.nickname = member.getSafeString("nickName");
                        item.memberSignature = member.getSafeString("memberSignature");

                        addFriendSearchResultItemList.add(item);
                    }

                    adapter.setNewData(addFriendSearchResultItemList);

                    if (addFriendSearchResultItemList.size() == 0) {
                        ToastUtil.error(_mActivity, "沒有符合條件的用戶");
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
        } else if (id == R.id.btn_my_qr_code) {
            Util.startFragment(QrCodeCardFragment.newInstance());
        } else if (id == R.id.btn_scan_qr_code) {
            startCaptureActivity(AppServiceImpl.Companion.getCaptureIntent());
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SLog.info("onActivityResult, requestCode[%d]", requestCode);

        /*
         * 处理二维码扫描结果
         */
        if (requestCode == RequestCode.SCAN_QR_CODE.ordinal()) {
            Util.handleQRCodeResult(_mActivity, data);
        }
    }
}
