package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.LogisticsMessageListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.LogisticsMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.LogUtil;
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
 * 交易物流消息列表
 * @author zwm
 */
public class LogisticsMessageListFragment  extends BaseFragment implements View.OnClickListener {
    BaseQuickAdapter adapter;
    List<LogisticsMessage> logisticsMessageList = new ArrayList<>();

    int tplClass;

    public static LogisticsMessageListFragment newInstance(int tplClass) {
        Bundle args = new Bundle();

        args.putInt("tplClass", tplClass);
        LogisticsMessageListFragment fragment = new LogisticsMessageListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logistics_message_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        tplClass = args.getInt("tplClass");

        TextView tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        if (tplClass == Constant.MESSAGE_CATEGORY_LOGISTICS) {
            tvFragmentTitle.setText(getString(R.string.text_logistics_message));
        } else if (tplClass == Constant.MESSAGE_CATEGORY_REFUND) {
            tvFragmentTitle.setText(getString(R.string.text_refund_message));
        }

        RecyclerView rvLogisticsMessageList = view.findViewById(R.id.rv_logistics_message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvLogisticsMessageList.setLayoutManager(layoutManager);
        adapter = new LogisticsMessageListAdapter(_mActivity, R.layout.logistics_message_item, logisticsMessageList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        rvLogisticsMessageList.setAdapter(adapter);

        Util.setOnClickListener(view, R.id.btn_back, this);

        loadLogisticsMessage();
    }

    private void loadLogisticsMessage() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_MESSAGE_LIST;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "tplClass", tplClass);

        SLog.info("params[%s]", params.toString());
        Api.getUI(url, params, new UICallback() {
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

                    EasyJSONArray messageClassVoList = responseObj.getSafeArray("datas.memberMessageList");
                    if (messageClassVoList.length() < 1) {
                        ToastUtil.info(_mActivity, "暫無消息");
                        return;
                    }
                    for (Object object : messageClassVoList) {
                        EasyJSONObject memberMessage = (EasyJSONObject) object;

                        LogisticsMessage logisticsMessage = new LogisticsMessage();
                        logisticsMessage.id = memberMessage.getInt("messageId");
                        logisticsMessage.content = memberMessage.getSafeString("messageContent");
                        logisticsMessage.time = memberMessage.getSafeString("addTime");

                        logisticsMessageList.add(logisticsMessage);
                    }
                    adapter.setNewData(logisticsMessageList);
                } catch (Exception e) {

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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }
}
