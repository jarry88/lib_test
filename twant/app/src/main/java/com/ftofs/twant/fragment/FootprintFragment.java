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
import com.ftofs.twant.adapter.FootprintListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.Footprint;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 我的足跡Fragment
 * @author zwm
 */
public class FootprintFragment extends BaseFragment implements View.OnClickListener {
    BaseQuickAdapter adapter;
    List<Footprint> footprintList = new ArrayList<>();

    /**
     * 編輯模式還是查看模式
     */
    int mode;

    public static FootprintFragment newInstance() {
        Bundle args = new Bundle();

        FootprintFragment fragment = new FootprintFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_footprint, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvFootprintList = view.findViewById(R.id.rv_footprint_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvFootprintList.setLayoutManager(layoutManager);
        adapter = new FootprintListAdapter(_mActivity, R.layout.footprint_item, footprintList);
        rvFootprintList.setAdapter(adapter);

        Util.setOnClickListener(view, R.id.btn_back, this);

        loadFootprintData();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }

    private void loadFootprintData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        Api.postUI(Api.PATH_FOOTPRINT, params, new UICallback() {
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

                    for (Object object : responseObj.getArray("datas.browseList")) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        EasyJSONObject goodsCommon = easyJSONObject.getObject("goodsCommon");

                        footprintList.add(new Footprint(
                                easyJSONObject.getString("addTime").substring(0, 10),
                                easyJSONObject.getInt("storeVo.storeId"),
                                easyJSONObject.getString("storeVo.storeName"),
                                easyJSONObject.getInt("commonId"),
                                goodsCommon.getString("imageSrc"),
                                goodsCommon.getString("goodsName"),
                                goodsCommon.getString("jingle"),
                                Util.getGoodsPrice(goodsCommon)
                        ));
                    }

                    adapter.setNewData(footprintList);
                } catch (Exception e) {
                    e.printStackTrace();
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
