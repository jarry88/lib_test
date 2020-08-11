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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.FootprintListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Footprint;
import com.ftofs.twant.entity.footprint.BaseStatus;
import com.ftofs.twant.entity.footprint.DateStatus;
import com.ftofs.twant.entity.footprint.GoodsStatus;
import com.ftofs.twant.entity.footprint.StoreStatus;
import com.ftofs.twant.entity.footprint.TotalStatus;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 瀏覽記憶Fragment
 * @author zwm
 */
public class FootprintFragment extends BaseFragment implements View.OnClickListener {
    FootprintListAdapter adapter;
    List<Footprint> footprintList = new ArrayList<>();
    TotalStatus totalStatus = new TotalStatus();
    TextView btnEdit;

    /**
     * 根據position和StatusType查找Status對象的Map
     * statusType有以下幾種類型
     * SELECT_STATUS_GOODS
     * SELECT_STATUS_STORE
     * SELECT_STATUS_DATE
     */
    Map<String, BaseStatus> positionTypeStatusMap = new HashMap<>();

    /**
     * 編輯模式還是查看模式
     */
    int mode = Constant.MODE_VIEW;

    RelativeLayout rlBottomActionBar;


    ScaledButton btnSelectAll;
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
        adapter = new FootprintListAdapter(_mActivity, R.layout.footprint_item, footprintList, positionTypeStatusMap);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_select_date || id == R.id.btn_select_store || id == R.id.btn_select_goods) {
                    String key = position + "|";
                    if (id == R.id.btn_select_date) {
                        key += Footprint.SELECT_STATUS_DATE;
                    } else if (id == R.id.btn_select_store) {
                        key += Footprint.SELECT_STATUS_STORE;
                    } else {
                        key += Footprint.SELECT_STATUS_GOODS;
                    }
                    positionTypeStatusMap.get(key).switchCheckStatus(BaseStatus.PHRASE_TARGET);

                    adapter.notifyDataSetChanged();
                } else if (id == R.id.ll_store_name_container) {
                    Footprint footprint = footprintList.get(position);
                    int storeId = footprint.storeId;
                    Util.startFragment(ShopMainFragment.newInstance(storeId));
                } else if (id == R.id.ll_goods_container) {
                    Footprint footprint = footprintList.get(position);
                    int commonId = footprint.commonId;
                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                }
            }
        });
        rvFootprintList.setAdapter(adapter);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_delete_footprint, this);
        btnEdit = view.findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);

        rlBottomActionBar = view.findViewById(R.id.rl_bottom_action_bar);
        btnSelectAll = view.findViewById(R.id.btn_select_all);
        btnSelectAll.setOnClickListener(this);
        totalStatus.setRadio(btnSelectAll);

        loadFootprintData();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_edit) {
            mode = 1 - mode;
            adapter.setMode(mode);
            adapter.notifyDataSetChanged();

            if (mode == Constant.MODE_EDIT) {
                rlBottomActionBar.setVisibility(View.VISIBLE);
                btnEdit.setText(getString(R.string.text_finish));
                btnEdit.setTextColor(getResources().getColor(R.color.tw_red, null));
            } else {
                rlBottomActionBar.setVisibility(View.GONE);
                btnEdit.setText(getString(R.string.text_edit));
                btnEdit.setTextColor(getResources().getColor(R.color.tw_black, null));
            }
        } else if (id == R.id.btn_select_all) {
            totalStatus.switchCheckStatus(BaseStatus.PHRASE_TARGET);
            adapter.notifyDataSetChanged();
        } else if (id == R.id.btn_delete_footprint) {
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
                    }).asCustom(new TwConfirmPopup(_mActivity, "確定要刪除選中的足跡嗎?", null, new OnConfirmCallback() {
                            @Override
                            public void onYes() {
                                SLog.info("onYes");
                                deleteFootprint();
                            }

                            @Override
                            public void onNo() {
                                SLog.info("onNo");
                            }
                        }))
                    .show();
        }
    }

    private void deleteFootprint() {
        // 收集選中的足跡
        List<Integer> idList = getSelectedFootprint();
        if (idList == null || idList.size() < 1) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Integer footprintId :idList) {
            if (!first) {
                sb.append(",");
            }
            sb.append(footprintId);

            first = false;
        }
        SLog.info("sb[%s]", sb.toString());

        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_DELETE_FOOTPRINT;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "browseIds", sb.toString());

        Api.postUI(url, params, new UICallback() {
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

                    // 刪除成功，重新加載數據
                    ToastUtil.success(_mActivity, "刪除成功");
                    loadFootprintData();
                } catch (Exception e) {

                }
            }
        });
    }

    private List<Integer> getSelectedFootprint() {
        List<Integer> idList = new ArrayList<>();
        for (DateStatus dateStatus : totalStatus.dateStatusList) {
            for (StoreStatus storeStatus : dateStatus.storeStatusList) {
                for (GoodsStatus goodsStatus : storeStatus.goodsStatusList) {
                    if (goodsStatus.isChecked()) {
                        idList.add(goodsStatus.footprintId);
                    }
                }
            }
        }

        return idList;
    }


    private void loadFootprintData() {
        mode = Constant.MODE_VIEW;
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_FOOTPRINT;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        Api.postUI(url, params, new UICallback() {
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

                    int position = 0;
                    String key;
                    DateStatus dateStatus = null;
                    StoreStatus storeStatus = null;
                    footprintList.clear();
                    positionTypeStatusMap.clear();

                    for (Object object : responseObj.getSafeArray("datas.browseList")) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        EasyJSONObject goodsCommon = easyJSONObject.getSafeObject("goodsCommon");

                        int footprintId = easyJSONObject.getInt("browseId");
                        String date = easyJSONObject.getSafeString("addTime").substring(0, 10);
                        int storeId = easyJSONObject.getInt("storeVo.storeId");

                        footprintList.add(new Footprint(
                                footprintId,
                                date,
                                storeId,
                                easyJSONObject.getSafeString("storeVo.storeName"),
                                easyJSONObject.getInt("commonId"),
                                goodsCommon.getSafeString("imageSrc"),
                                goodsCommon.getSafeString("goodsName"),
                                goodsCommon.getSafeString("jingle"),
                                Util.getSpuPrice(goodsCommon)
                        ));


                        if (position > 0) {
                            // 查看前一項，日期或商店是否相同
                            Footprint prevFootprint = footprintList.get(position - 1);

                            boolean isDifferentDate = false;
                            // 如果日期與前一項不相同，則顯示日期
                            if (!prevFootprint.date.equals(date)) {
                                isDifferentDate = true;
                                dateStatus = new DateStatus();
                                totalStatus.dateStatusList.add(dateStatus);
                                key = position + "|" + Footprint.SELECT_STATUS_DATE;
                                dateStatus.parent = totalStatus;
                                positionTypeStatusMap.put(key, dateStatus);
                            }

                            // 如果與前一項不同一天或與前一項不同一家商店，則顯示商店名信息
                            if (prevFootprint.storeId != storeId || isDifferentDate) {
                                storeStatus = new StoreStatus();
                                dateStatus.storeStatusList.add(storeStatus);
                                key = position + "|" + Footprint.SELECT_STATUS_STORE;
                                storeStatus.parent = dateStatus;
                                positionTypeStatusMap.put(key, storeStatus);
                            }

                            GoodsStatus goodsStatus = new GoodsStatus(footprintId);
                            storeStatus.goodsStatusList.add(goodsStatus);
                            key = position + "|" + Footprint.SELECT_STATUS_GOODS;
                            goodsStatus.parent = storeStatus;
                            positionTypeStatusMap.put(key, goodsStatus);
                        } else { // position == 0
                            GoodsStatus goodsStatus = new GoodsStatus(footprintId);
                            storeStatus = new StoreStatus();
                            storeStatus.goodsStatusList.add(goodsStatus);
                            key = position + "|" + Footprint.SELECT_STATUS_GOODS;
                            goodsStatus.parent = storeStatus;
                            positionTypeStatusMap.put(key, goodsStatus);

                            dateStatus = new DateStatus();
                            dateStatus.storeStatusList.add(storeStatus);
                            key = position + "|" + Footprint.SELECT_STATUS_STORE;
                            storeStatus.parent = dateStatus;
                            positionTypeStatusMap.put(key, storeStatus);

                            totalStatus.dateStatusList.add(dateStatus);
                            key = position + "|" + Footprint.SELECT_STATUS_DATE;
                            dateStatus.parent = totalStatus;
                            positionTypeStatusMap.put(key, dateStatus);
                        }

                        position++;
                    }

                    adapter.setNewData(footprintList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
}
