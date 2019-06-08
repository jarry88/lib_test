package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.TrustValueListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.TrustValueItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 信賴值明細Fragment
 * @author zwm
 */
public class TrustValueFragment extends BaseFragment implements View.OnClickListener {
    /*
    【我的積分】列表與【我的信賴值】列表共用一個Fragment，所以通過dataType字段來區分
     */

    public static final int DATA_TYPE_BONUS = 1;
    public static final int DATA_TYPE_TRUST_VALUE = 2;

    int dataType;

    TextView tvFragmentTitle;
    List<TrustValueItem> trustValueItemList = new ArrayList<>();

    BaseQuickAdapter adapter;

    public static TrustValueFragment newInstance(int dataType) {
        Bundle args = new Bundle();

        args.putInt("dataType", dataType);
        TrustValueFragment fragment = new TrustValueFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trust_value, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        dataType = args.getInt("dataType");

        Util.setOnClickListener(view, R.id.btn_back, this);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);

        RecyclerView rvTrustValueList = view.findViewById(R.id.rv_trust_value_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvTrustValueList.setLayoutManager(layoutManager);
        adapter = new TrustValueListAdapter(R.layout.trust_value_item, trustValueItemList);
        rvTrustValueList.setAdapter(adapter);

        loadTrustValueData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    private void loadTrustValueData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        String path;
        if (dataType == DATA_TYPE_BONUS) {
            path = Api.PATH_BONUS_LOG;
        } else {
            path = Api.PATH_TRUST_VALUE_LOG;
        }

        Api.postUI(path, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    int total = 0;
                    if (dataType == DATA_TYPE_BONUS) {
                        total = responseObj.getInt("datas.totalPoints");
                    } else {
                        total = responseObj.getInt("datas.totalExpPoints");
                    }

                    String fragmentTitle;
                    if (dataType == DATA_TYPE_BONUS) {
                        fragmentTitle = String.format(getString(R.string.bonus_fragment_title), total);
                    } else {
                        fragmentTitle = String.format(getString(R.string.trust_value_fragment_title), total);
                    }

                    tvFragmentTitle.setText(fragmentTitle);

                    EasyJSONArray logList = responseObj.getArray("datas.logList");
                    for (Object object : logList) {
                        EasyJSONObject log = (EasyJSONObject) object;

                        trustValueItemList.add(new TrustValueItem(
                                log.getString("description"),
                                log.getString("operationStageText"),
                                log.getInt("points"),
                                log.getString("addTime")));
                    }
                    adapter.setNewData(trustValueItemList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}