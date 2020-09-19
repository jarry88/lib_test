package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 退貨發貨Fragment
 * @author zwm
 */
public class ReturnShipFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    int refundId;
    int shipId = 0;
    int selectedCompanyIndex = -1; // 選中的物流公司的索引

    EditText etNum;

    List<ListPopupItem> popupItemList = new ArrayList<>();

    TextView tvCompany;

    public static ReturnShipFragment newInstance(int refundId) {
        Bundle args = new Bundle();

        ReturnShipFragment fragment = new ReturnShipFragment();
        fragment.setArguments(args);
        fragment.setRefundId(refundId);

        return fragment;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_return_ship, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNum = view.findViewById(R.id.et_num);
        tvCompany = view.findViewById(R.id.tv_company);
        etNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                SLog.info("here");
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveData();
                }
                return false;
            }
        });

        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_select_company, this);
        Util.setOnClickListener(view, R.id.btn_send_package, this);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "refundId", refundId
        );

        Api.postUI(Api.PATH_RETURN_SHIP, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try{
                    SLog.info("responseStr[%s]",responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    EasyJSONArray shipList = responseObj.getArray("datas.shipList");
                    int idx = 0;
                    for (Object object : shipList) {
                        EasyJSONObject shipObj = (EasyJSONObject) object;

                        ListPopupItem item = new ListPopupItem(idx, 0, 0, shipObj.getSafeString("shipName"), shipObj.getInt("shipId"));
                        popupItemList.add(item);

                        ++idx;
                    }
                } catch (Exception e){
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void saveData() {
        String token = User.getToken();

        /*
        token String 用户登录token
        refundId int 订单号
        shipId int 快递公司id
        shipSn string 快递单号
         */
        if (selectedCompanyIndex == -1) {
            ToastUtil.error(_mActivity, "請選擇快遞公司");
            return;
        }

        String shipSn = etNum.getText().toString().trim();
        if (StringUtil.isEmpty(shipSn)) {
            ToastUtil.error(_mActivity, "請輸入物流單號");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "refundId", refundId,
                "shipId", shipId,
                "shipSn", shipSn
        );

        Api.postUI(Api.PATH_RETURN_SHIP_SAVE, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try{
                    SLog.info("responseStr[%s]",responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.success(_mActivity, "保存成功");

                    Bundle bundle = new Bundle();
                    setFragmentResult(RESULT_OK, bundle); // 不需要數據，只需要通知

                    hideSoftInputPop();
                } catch (Exception e){
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_ok) {
            saveData();
        } else if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_select_company) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.mobile_zone_text),
                            PopupType.SELECT_LOGISTICS_COMPANY, popupItemList, selectedCompanyIndex, this))
                    .show();
        } else if (id == R.id.btn_send_package) {
            Util.startFragment(SendPackageFragment.newInstance());
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.SELECT_LOGISTICS_COMPANY) {
            selectedCompanyIndex = id;
            ListPopupItem item = popupItemList.get(selectedCompanyIndex);
            shipId = (int) item.data;
            tvCompany.setText(item.title);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

