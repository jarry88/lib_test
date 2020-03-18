package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 寄包裹Fragment
 * @author zwm
 */
public class SendPackageFragment extends BaseFragment implements View.OnClickListener {
    TextView tvSenderName;
    TextView btnInputSenderInfo;
    TextView tvReceiverName;
    TextView btnInputReceiverInfo;

    Set<String> packageTypeSet = new HashSet<>();
    EditText etCustomPackageType;

    EditText etPackageWeight;
    EditText etPackageLength;
    EditText etPackageWidth;
    EditText etPackageHeight;
    EditText etRemark;

    String senderName;
    int senderAreaId1;
    int senderAreaId2;
    int senderAreaId3;
    int senderAreaId4;
    int senderAreaId;
    String senderAreaInfo;
    String senderAddress;
    String senderMobile;
    String senderMobileAreaCode;

    String receiverName;
    int receiverAreaId1;
    int receiverAreaId2;
    int receiverAreaId3;
    int receiverAreaId4;
    int receiverAreaId;
    String receiverAreaInfo;
    String receiverAddress;
    String receiverMobile;
    String receiverMobileAreaCode;

    public static SendPackageFragment newInstance() {
        Bundle args = new Bundle();

        SendPackageFragment fragment = new SendPackageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_package, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_query_package, this);

        tvSenderName = view.findViewById(R.id.tv_sender_name);
        btnInputSenderInfo = view.findViewById(R.id.btn_input_sender_info);
        btnInputSenderInfo.setOnClickListener(this);
        Util.setOnClickListener(view, R.id.btn_select_sender_address, this);

        tvReceiverName = view.findViewById(R.id.tv_receiver_name);
        btnInputReceiverInfo = view.findViewById(R.id.btn_input_receiver_info);
        btnInputReceiverInfo.setOnClickListener(this);
        Util.setOnClickListener(view, R.id.btn_select_receiver_address, this);

        etCustomPackageType = view.findViewById(R.id.et_custom_package_type);
        etPackageWeight = view.findViewById(R.id.et_package_weight);
        etPackageLength = view.findViewById(R.id.et_package_length);
        etPackageWidth = view.findViewById(R.id.et_package_width);
        etPackageHeight = view.findViewById(R.id.et_package_height);
        etRemark = view.findViewById(R.id.et_remark);

        Util.setOnClickListener(view, R.id.btn_package_type_daily_use, this);
        Util.setOnClickListener(view, R.id.btn_package_type_digital, this);
        Util.setOnClickListener(view, R.id.btn_package_type_clothes, this);
        Util.setOnClickListener(view, R.id.btn_package_type_food, this);
        Util.setOnClickListener(view, R.id.btn_package_type_file, this);
        Util.setOnClickListener(view, R.id.btn_package_type_medicine, this);

        Util.setOnClickListener(view, R.id.btn_commit, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_query_package:
                Util.startFragment(QueryPackageFragment.newInstance());
                break;
            case R.id.btn_input_sender_info:
                startForResult(SenderInfoFragment.newInstance(SenderInfoFragment.DATA_TYPE_SENDER_INFO), RequestCode.INPUT_SENDER_INFO.ordinal());
                break;
            case R.id.btn_input_receiver_info:
                startForResult(SenderInfoFragment.newInstance(SenderInfoFragment.DATA_TYPE_RECEIVER_INFO), RequestCode.INPUT_RECEIVER_INFO.ordinal());
                break;
            case R.id.btn_select_sender_address:
                startForResult(AddrManageFragment.newInstance(), RequestCode.SELECT_SENDER_ADDR.ordinal());
                break;
            case R.id.btn_select_receiver_address:
                startForResult(AddrManageFragment.newInstance(), RequestCode.SELECT_RECEIVER_ADDR.ordinal());
                break;
            case R.id.btn_package_type_daily_use:
            case R.id.btn_package_type_digital:
            case R.id.btn_package_type_clothes:
            case R.id.btn_package_type_food:
            case R.id.btn_package_type_file:
            case R.id.btn_package_type_medicine:
                TextView tvPackageType = (TextView) v;
                String packageType = tvPackageType.getText().toString();
                if (v.isSelected()) {
                    tvPackageType.setTextColor(_mActivity.getColor(R.color.tw_black));
                    packageTypeSet.remove(packageType);
                } else {
                    tvPackageType.setTextColor(_mActivity.getColor(R.color.tw_blue));
                    packageTypeSet.add(packageType);
                }
                v.setSelected(!v.isSelected());
                break;

            case R.id.btn_commit:
                doCommit();
                break;

            default:
                break;
        }
    }

    private void doCommit() {
        // 校驗收發貨地址信息

        if (StringUtil.isEmpty(senderName) || senderAreaId1 == 0 || senderAreaId == 0 || StringUtil.isEmpty(senderAreaInfo) ||
                StringUtil.isEmpty(senderAddress) || StringUtil.isEmpty(senderMobile) || StringUtil.isEmpty(senderMobileAreaCode)) {
            ToastUtil.error(_mActivity, "請填寫有效的發貨信息");
            return;
        }

        if (StringUtil.isEmpty(receiverName) || receiverAreaId1 == 0 || receiverAreaId == 0 || StringUtil.isEmpty(receiverAreaInfo) ||
                StringUtil.isEmpty(receiverAddress) || StringUtil.isEmpty(receiverMobile) || StringUtil.isEmpty(receiverMobileAreaCode)) {
            SLog.info("receiverName[%s], receiverAreaId1[%d], receiverAreaId[%d], receiverAreaInfo[%s], receiverAddress[%s], receiverMobile[%s], receiverMobileAreaCode[%s]",
                    receiverName, receiverAreaId1, receiverAreaId, receiverAreaInfo, receiverAddress, receiverMobile, receiverMobileAreaCode);
            ToastUtil.error(_mActivity, "請填寫有效的收貨信息");
            return;
        }

        StringBuilder packageType = new StringBuilder();
        boolean first = true;
        for (String item: packageTypeSet) {
            if (!first) {
                packageType.append(",");
            }
            packageType.append(item);
            first = false;
        }

        String customPackageType = etCustomPackageType.getText().toString().trim();
        if (!StringUtil.isEmpty(customPackageType)) {
            if (!first) {
                packageType.append(",");
            }
            packageType.append(customPackageType);
            first = false;
        }

        SLog.info("packageType[%s]", packageType);

        String packageWeight = etPackageWeight.getText().toString().trim();
        String packageLength = etPackageLength.getText().toString().trim();
        String packageWidth = etPackageWidth.getText().toString().trim();
        String packageHeight = etPackageHeight.getText().toString().trim();
        String remark = etRemark.getText().toString().trim();

        if (StringUtil.isEmpty(packageWeight)) {
            ToastUtil.error(_mActivity, getString(R.string.input_package_weight_hint));
            return;
        }

        Double weight = Double.valueOf(packageWeight);
        Double length = Double.valueOf(packageLength);
        Double width = Double.valueOf(packageWidth);
        Double height = Double.valueOf(packageHeight);

        String token = User.getToken();
        // token要附在url中
        String path = Api.PATH_PACKAGE_DELIVERY + Api.makeQueryString(EasyJSONObject.generate("token", token));
        SLog.info("url[%s]", path);

        EasyJSONObject receiverAddressInfo = EasyJSONObject.generate(
                "realName", receiverName,
                "areaId1", receiverAreaId1,
                "areaId2", receiverAreaId2,
                "areaId3", receiverAreaId3,
                "areaId4", receiverAreaId4,
                "areaId", receiverAreaId,
                "areaInfo", receiverAreaInfo,
                "address", receiverAddress,
                "mobPhone", receiverMobile,
                "mobileAreaCode", receiverMobileAreaCode);

        EasyJSONObject cargo = EasyJSONObject.generate(
                "name", packageType,
                "quantity", "1",
                "weight", weight,
                "height", height,
                "width", width,
                "cargoLong", length);

        EasyJSONObject entityVo = EasyJSONObject.generate(
                "consigneeAddress", mergeAddress(receiverAreaInfo, receiverAddress),
                "consigneeName", receiverName,
                "consigneePhone", receiverMobileAreaCode + receiverMobile,
                "consignerAddress", mergeAddress(senderAreaInfo, senderAddress),
                "consignerName", senderName,
                "consignerPhone", senderMobileAreaCode + senderMobile,
                "remarks", remark,
                "cargoList", EasyJSONArray.generate(cargo));

        EasyJSONObject params = EasyJSONObject.generate(
                "addressList", EasyJSONArray.generate(receiverAddressInfo),
                "entityVo", entityVo
        );

        SLog.info("params[%s]", params.toString());

        Api.postJsonUi(path, params.toString(), new UICallback() {
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

                    ToastUtil.success(_mActivity, "提交成功");

                    hideSoftInputPop();
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
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

        SLog.info("requestCode[%d], resultCode[%d]", requestCode, resultCode);
        if (resultCode != RESULT_OK ) {
            return;
        }

        if (requestCode == RequestCode.SELECT_SENDER_ADDR.ordinal()) {
            AddrItem addrItem = data.getParcelable("addrItem");
            if (addrItem == null) {
                return;
            }
            SLog.info("addrItem[%s]", addrItem.toString());
            senderName = addrItem.realName;
            senderAreaId1 = addrItem.areaIdList.get(0);
            senderAreaId2 = addrItem.areaIdList.get(1);
            senderAreaId3 = addrItem.areaIdList.get(2);
            senderAreaId4 = addrItem.areaIdList.get(3);
            senderAreaId = addrItem.areaId;
            senderAreaInfo = addrItem.areaInfo;
            senderAddress = addrItem.address;
            senderMobile = addrItem.mobPhone;
            senderMobileAreaCode = addrItem.mobileAreaCode;

            SLog.info("senderName[%s], senderAreaId1[%s], senderAreaId2[%s], senderAreaId3[%s], senderAreaId4[%s], senderAreaId[%s], senderAreaInfo[%s], senderAddress[%s], senderMobile[%s], senderMobileAreaCode[%s]",
                    senderName, senderAreaId1, senderAreaId2, senderAreaId3, senderAreaId4, senderAreaId, senderAreaInfo, senderAddress, senderMobile, senderMobileAreaCode);

            tvSenderName.setText(senderName);
            btnInputSenderInfo.setText(mergeAddress(senderAreaInfo, senderAddress));
        } else if (requestCode == RequestCode.SELECT_RECEIVER_ADDR.ordinal()) {
            AddrItem addrItem = data.getParcelable("addrItem");
            if (addrItem == null) {
                return;
            }
            SLog.info("addrItem[%s]", addrItem.toString());

            receiverName = addrItem.realName;
            receiverAreaId1 = addrItem.areaIdList.get(0);
            receiverAreaId2 = addrItem.areaIdList.get(1);
            receiverAreaId3 = addrItem.areaIdList.get(2);
            receiverAreaId4 = addrItem.areaIdList.get(3);
            receiverAreaId = addrItem.areaId;
            receiverAreaInfo = addrItem.areaInfo;
            receiverAddress = addrItem.address;
            receiverMobile = addrItem.mobPhone;
            receiverMobileAreaCode = addrItem.mobileAreaCode;

            SLog.info("receiverName[%s], receiverAreaId1[%s], receiverAreaId2[%s], receiverAreaId3[%s], receiverAreaId4[%s], receiverAreaId[%s], receiverAreaInfo[%s], receiverAddress[%s], receiverMobile[%s], receiverMobileAreaCode[%s]",
                    receiverName, receiverAreaId1, receiverAreaId2, receiverAreaId3, receiverAreaId4, receiverAreaId, receiverAreaInfo, receiverAddress, receiverMobile, receiverMobileAreaCode);

            tvReceiverName.setText(receiverName);
            btnInputReceiverInfo.setText(mergeAddress(receiverAreaInfo, receiverAddress));
        } else if (requestCode == RequestCode.INPUT_SENDER_INFO.ordinal()) {
            senderName = data.getString("name");
            senderAreaId1 = data.getInt("areaId1");
            senderAreaId2 = data.getInt("areaId2");
            senderAreaId3 = data.getInt("areaId3");
            senderAreaId4 = data.getInt("areaId4");
            senderAreaId = data.getInt("areaId");
            senderAreaInfo = data.getString("areaInfo");
            senderAddress = data.getString("address");
            senderMobile = data.getString("mobile");
            senderMobileAreaCode = data.getString("mobileAreaCode");

            SLog.info("senderName[%s], senderAreaId1[%s], senderAreaId2[%s], senderAreaId3[%s], senderAreaId4[%s], senderAreaId[%s], senderAreaInfo[%s], senderAddress[%s], senderMobile[%s], senderMobileAreaCode[%s]",
                    senderName, senderAreaId1, senderAreaId2, senderAreaId3, senderAreaId4, senderAreaId, senderAreaInfo, senderAddress, senderMobile, senderMobileAreaCode);

            tvSenderName.setText(senderName);
            btnInputSenderInfo.setText(mergeAddress(senderAreaInfo, senderAddress));
        } else if (requestCode == RequestCode.INPUT_RECEIVER_INFO.ordinal()) {
            receiverName = data.getString("name");
            receiverAreaId1 = data.getInt("areaId1");
            receiverAreaId2 = data.getInt("areaId2");
            receiverAreaId3 = data.getInt("areaId3");
            receiverAreaId4 = data.getInt("areaId4");
            receiverAreaId = data.getInt("areaId");
            receiverAreaInfo = data.getString("areaInfo");
            receiverAddress = data.getString("address");
            receiverMobile = data.getString("mobile");
            receiverMobileAreaCode = data.getString("mobileAreaCode");

            SLog.info("receiverName[%s], receiverAreaId1[%s], receiverAreaId2[%s], receiverAreaId3[%s], receiverAreaId4[%s], receiverAreaId[%s], receiverAreaInfo[%s], receiverAddress[%s], receiverMobile[%s], receiverMobileAreaCode[%s]",
                    receiverName, receiverAreaId1, receiverAreaId2, receiverAreaId3, receiverAreaId4, receiverAreaId, receiverAreaInfo, receiverAddress, receiverMobile, receiverMobileAreaCode);

            tvReceiverName.setText(receiverName);
            btnInputReceiverInfo.setText(mergeAddress(receiverAreaInfo, receiverAddress));
        }
    }

    private String mergeAddress(String areaInfo, String address) {
        return areaInfo + " " + address;
    }
}
