package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AreaPopupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.domain.Area;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
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
public class AreaPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    int popupType;

    List<Area> areaList = new ArrayList<>();
    List<Area> selectedAreaList = new ArrayList<>();

    OnSelectedListener onSelectedListener;
    AreaPopupAdapter adapter;
    int twBlack;

    LinearLayout llAreaContainer;

    public AreaPopup(@NonNull Context context, int popupType, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.popupType = popupType;
        this.onSelectedListener = onSelectedListener;
        twBlack = getResources().getColor(R.color.tw_black, null);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.area_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        llAreaContainer = findViewById(R.id.ll_area_container);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        RecyclerView rvList = findViewById(R.id.rv_area_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        adapter = new AreaPopupAdapter(R.layout.area_popup_item, areaList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("position[%d]", position);
                if (position >= areaList.size()) {
                    return;
                }
                TextView tvArea = new TextView(getContext());
                Area area = areaList.get(position);

                int depth = area.getAreaDeep();
                if (selectedAreaList.size() >= depth) {
                    SLog.info("已经是最后一级, SIZE[%d], DEPTH[%d]", areaList.size(), depth);
                    return;
                }
                selectedAreaList.add(area);

                tvArea.setText(area.getAreaName());
                tvArea.setTextSize(15);
                tvArea.setTextColor(twBlack);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, Util.dip2px(getContext(), 15), 0);
                llAreaContainer.addView(tvArea, layoutParams);

                loadAreaData(area.getAreaId());
            }
        });
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
        SLog.info("onDismiss");
        onSelectedListener.onSelected(popupType, 0, selectedAreaList);
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
