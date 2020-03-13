package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AreaPopupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.Area;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
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


/**
 * 地區選擇彈框
 * @author zwm
 */
public class AreaPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    PopupType popupType;

    List<Area> areaList = new ArrayList<>();
    List<Area> selectedAreaList = new ArrayList<>();
    List<AreaItemView> areaItemViewList = new ArrayList<>();

    OnSelectedListener onSelectedListener;
    AreaPopupAdapter adapter;
    int twBlack;

    LinearLayout llAreaContainer;

    public AreaPopup(@NonNull Context context, PopupType popupType, OnSelectedListener onSelectedListener) {
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

                AreaItemView areaItemView = new AreaItemView(getContext());
                Area area = areaList.get(position);

                int depth = area.getAreaDeep();
                if (selectedAreaList.size() >= depth) {
                    SLog.info("已经是最后一级, SIZE[%d], DEPTH[%d]", areaList.size(), depth);
                    setAddress();
                    return;
                }
                selectedAreaList.add(area);

                // 將之前的AreaItemView取消高亮
                for (AreaItemView itemView : areaItemViewList) {
                    // itemView.setStatus(Constant.STATUS_UNSELECTED);
                }
                areaItemView.setText(area.getAreaName());
                areaItemView.setStatus(Constant.STATUS_SELECTED);
                areaItemView.setDepth(depth);
                areaItemView.setAreaId(area.getAreaId());
                areaItemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 如果點擊自己，將自己和后面的數據出隊列，重新加載上一級的地址列表
                        AreaItemView itemView = (AreaItemView) v;
                        int depth = itemView.getDepth();
                        SLog.info("depth[%d]", depth);
                        int index = depth - 1;  // 點擊到的item的索引，從0開始
                        for (int i = selectedAreaList.size() - 1; i >= index ; i--) {
                            selectedAreaList.remove(i);
                        }

                        for (int i = areaItemViewList.size() - 1; i >= index ; i--) {
                            areaItemViewList.remove(i);
                        }

                        int childCount = llAreaContainer.getChildCount();
                        if (childCount - index > 0) {
                            llAreaContainer.removeViews(index, childCount - index);
                        }

                        int parentIndex = index - 1;
                        if (parentIndex == -1) {
                            loadAreaData(0);
                        } else {
                            Area parentArea = selectedAreaList.get(parentIndex);
                            loadAreaData(parentArea.getAreaId());
                        }
                    }
                });
                areaItemViewList.add(areaItemView);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, Util.dip2px(getContext(), 15), 0);
                llAreaContainer.addView(areaItemView, layoutParams);

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
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_dismiss:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void setAddress() {
        if (popupType == PopupType.MEMBER_ADDRESS) {
            setMemberAddress();
        } else {
            onSelectedListener.onSelected(popupType, 0, selectedAreaList);
            dismiss();
        }
    }

    private void setMemberAddress() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        if (selectedAreaList.size() < 2) {
            ToastUtil.error(context, "請選擇區域");
            return;
        }

        /*
        token String 当前登录令牌
addressProvinceId int 省级id
addressCityId int 市级id
addressAreaId int 区级 id
addressAreaInfo String 地区全名
         */
        int addressProvinceId = selectedAreaList.get(0).getAreaId();
        int addressCityId = selectedAreaList.get(1).getAreaId();
        int addressAreaId = 0;
        if (selectedAreaList.size() > 2) {
            addressAreaId = selectedAreaList.get(2).getAreaId();
        }

        String addressAreaInfoTmp = selectedAreaList.get(0).getAreaName() + " " + selectedAreaList.get(1).getAreaName();
        if (selectedAreaList.size() > 2) {
            addressAreaInfoTmp += " " + selectedAreaList.get(2).getAreaName();
        }
        final String addressAreaInfo = addressAreaInfoTmp;

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "addressProvinceId", addressProvinceId,
                "addressCityId", addressCityId,
                "addressAreaId", addressAreaId,
                "addressAreaInfo", addressAreaInfo);

        SLog.info("params[%s]", params.toString());

        Api.postUI(Api.PATH_SET_MEMBER_ADDRESS, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(context, responseObj)) {
                        return;
                    }

                    ToastUtil.success(context, "保存成功");
                    onSelectedListener.onSelected(popupType, 0, addressAreaInfo);
                    dismiss();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadAreaData(int areaId) {
        SLog.info("loadAreaData, areaId[%d]", areaId);
        EasyJSONObject params = EasyJSONObject.generate(
                "areaId", areaId);
        Api.getUI(Api.PATH_AREA_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);



                try {
                    if (ToastUtil.isError(responseObj)) {
                        int code = responseObj.getInt("code");
                        String error = responseObj.getSafeString("datas.error");
                        if (code == 400 && "无下级".equals(error)) {
                            setAddress();
                        }
                    }

                    EasyJSONArray easyJSONArray = responseObj.getSafeArray("datas.areaList");
                    areaList.clear();
                    for (Object object : easyJSONArray) {
                        // {"areaId":36,"areaName":"北京市","areaParentId":1,"areaDeep":2,"areaRegion":null,"areaCode":"110100"}
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        Area area = new Area();
                        area.setAreaId(easyJSONObject.getInt("areaId"));
                        area.setAreaName(easyJSONObject.getSafeString("areaName"));
                        area.setAreaParentId(easyJSONObject.getInt("areaParentId"));
                        area.setAreaDeep(easyJSONObject.getInt("areaDeep"));
                        area.setAreaRegion(easyJSONObject.getSafeString("areaRegion"));
                        area.setAreaCode(easyJSONObject.getSafeString("areaCode"));
                        areaList.add(area);
                    }

                    adapter.setNewData(areaList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
}
