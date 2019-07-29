package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

import java.util.HashSet;
import java.util.Set;


/**
 * 寄包裹Fragment
 * @author zwm
 */
public class SendPackageFragment extends BaseFragment implements View.OnClickListener {
    Set<String> packageTypeSet = new HashSet<>();
    EditText etCustomPackageType;

    EditText etPackageWeight;
    EditText etPackageLength;
    EditText etPackageWidth;
    EditText etPackageHeight;
    EditText etRemark;

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
        Util.setOnClickListener(view, R.id.btn_input_sender_info, this);
        Util.setOnClickListener(view, R.id.btn_input_receiver_info, this);

        etCustomPackageType = view.findViewById(R.id.et_custom_package_type);

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
                pop();
                break;
            case R.id.btn_query_package:
                Util.startFragment(QueryPackageFragment.newInstance());
                break;
            case R.id.btn_input_sender_info:
                Util.startFragment(SenderInfoFragment.newInstance(SenderInfoFragment.DATA_TYPE_SENDER_INFO));
                break;
            case R.id.btn_input_receiver_info:
                Util.startFragment(SenderInfoFragment.newInstance(SenderInfoFragment.DATA_TYPE_RECEIVER_INFO));
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


    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
