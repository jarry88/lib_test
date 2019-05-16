package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AreaPopupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.domain.Area;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 地區選擇彈框
 * @author zwm
 */
public class AreaPopup extends BottomPopupView implements View.OnClickListener, OnSelectedListener {
    Context context;

    List<Area> areaList = new ArrayList<>();
    OnSelectedListener onSelectedListener;
    AreaPopupAdapter adapter;

    public AreaPopup(@NonNull Context context, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.area_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        RecyclerView rvList = findViewById(R.id.rv_area_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        adapter = new AreaPopupAdapter(R.layout.area_popup_item, areaList);
        rvList.setAdapter(adapter);

        loadAreaData(0);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        }
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        SLog.info("onSelected, type[%d], id[%d], extra[%s]", type, id, extra);
        onSelectedListener.onSelected(type, id, extra);
        dismiss();
    }

    private void loadAreaData(int areaId) {
        EasyJSONObject params = EasyJSONObject.generate(
                "areaId", areaId);
        Api.getUI(Api.PATH_AREA_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(context, responseObj)) {
                    return;
                }

                try {
                    EasyJSONArray easyJSONArray = responseObj.getArray("datas.areaList");
                    areaList.clear();
                    for (Object object : easyJSONArray) {
                        // {"areaId":36,"areaName":"北京市","areaParentId":1,"areaDeep":2,"areaRegion":null,"areaCode":"110100"}
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        Area area = new Area();
                        area.setAreaId(easyJSONObject.getInt("areaId"));
                        area.setAreaName(easyJSONObject.getString("areaName"));
                        area.setAreaParentId(easyJSONObject.getInt("areaParentId"));
                        area.setAreaDeep(easyJSONObject.getInt("areaDeep"));
                        area.setAreaRegion(easyJSONObject.getString("areaRegion"));
                        area.setAreaCode(easyJSONObject.getString("areaCode"));
                        areaList.add(area);
                    }

                    adapter.setNewData(areaList);
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
