package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SizeAssistantFragment extends BaseFragment implements View.OnClickListener {
    EditText etMemberHeight;
    double memberHeight;
    EditText etMemberWight;
    double memberWight;
    EditText etMemberFootSize;
    double memberFootSize;
    EditText etMemberBust;
    double memberBust;
    EditText etMemberWaistline;
    double memberWaistline;
    EditText etMemberBicep;
    double memberBicep;
    TextView tvMemberLeftEye;
    int memberLeftEye;
    TextView tvMemberRightEye;
    int memberRightEye;
    public static SizeAssistantFragment newInstance(){
        SizeAssistantFragment item =new SizeAssistantFragment();
        Bundle args =new Bundle();
        item.setArguments(args);

        return item;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_size_assistant,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etMemberHeight = view.findViewById(R.id.et_height);
        etMemberWight = view.findViewById(R.id.et_weight);
        etMemberFootSize = view.findViewById(R.id.et_foot_size);
        etMemberBust = view.findViewById(R.id.et_memberBust);
        etMemberWaistline = view.findViewById(R.id.et_memberWaistline);
        etMemberBicep = view.findViewById(R.id.et_memberBicep);
        tvMemberLeftEye = view.findViewById(R.id.tv_memberLeftEye);
        tvMemberRightEye = view.findViewById(R.id.tv_memberRightEye);

        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_size_compare_table, this);
        Util.setOnClickListener(view, R.id.btn_eyes_radios, this);
        loadMemberInfo();
    }

    private void loadMemberInfo() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_MEMBER_DETAIL, params, new UICallback() {
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

                    EasyJSONObject memberInfo = responseObj.getSafeObject("datas.memberInfo");
                    double height =memberInfo.getDouble("memberHeight");
                    double weight =memberInfo.getDouble("memberWight");
                    double memberFootSize =memberInfo.getDouble("memberFootSize");
                    double memberBust =memberInfo.getDouble("memberBust");
                    double memberWaistline =memberInfo.getDouble("memberWaistline");
                    double memberBicep =memberInfo.getDouble("memberBicep");
                    int memberLeftEye = memberInfo.getInt("memberLeftEye");
                    int memberRightEye = memberInfo.getInt("memberRightEye");
                    etMemberHeight.setText(String.valueOf(height));
                    etMemberWight.setText(String.valueOf(weight));
                    etMemberFootSize.setText(String.valueOf(memberFootSize));
                    etMemberBust.setText(String.valueOf(memberBust));
                    etMemberWaistline.setText(String.valueOf(memberWaistline));
                    etMemberBicep.setText(String.valueOf(memberBicep));
                    tvMemberLeftEye.setText(String.valueOf(memberLeftEye));
                    tvMemberRightEye.setText(String.valueOf(memberRightEye));

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id =view.getId();
        switch (id){
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_ok:
                saveData();
                break;
            case R.id.btn_size_compare_table:
                start(ImageViewerFragment.newInstance("/android_asset/size_compare/img_size_compare.png",true));
                break;
            case R.id.btn_eyes_radios:
                List<ListPopupItem> list1 = new ArrayList();
                List<ListPopupItem> list2 = new ArrayList();
                for (int i=0;i<21;i++){
                    list1.add(new ListPopupItem(i, String.valueOf(i*50), null));
                    list2.add(new ListPopupItem(i, String.valueOf(i*50), null));
                }

                int index1=Integer.parseInt(tvMemberLeftEye.getText().toString())/50;
                int index2=Integer.parseInt(tvMemberRightEye.getText().toString())/50;

                Util.showPickerView(_mActivity, "眼睛度數", list1, list2, null,index1,index2,0, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int id1, int id2, int id3, View v) {
                    tvMemberLeftEye.setText(list1.get(id1).title);
                    tvMemberRightEye.setText(list2.get(id2).title);
                }
            });
                break;
                default:
                    break;

        }
    }

    private void saveData() {
        String token= User.getToken();
        if (StringUtil.isEmpty(token)){
            return;
        }
        memberHeight = Double.parseDouble(etMemberHeight.getText().toString());
        memberWight = Double.parseDouble(etMemberWight.getText().toString());
        memberFootSize = Double.parseDouble(etMemberFootSize.getText().toString());
        memberBust = Double.parseDouble(etMemberBust.getText().toString());
        memberWaistline = Double.parseDouble(etMemberWaistline.getText().toString());
        memberBicep = Double.parseDouble(etMemberBicep.getText().toString());
        memberLeftEye = Integer.parseInt(tvMemberLeftEye.getText().toString());
        memberRightEye = Integer.parseInt(tvMemberRightEye.getText().toString());
        EasyJSONObject params=EasyJSONObject.generate(
                "token",token,
                "memberHeight",memberHeight,
                "memberWight",memberWight,
                "memberFootSize",memberFootSize,
                "memberBust",memberBust,
                "memberWaistline",memberWaistline,
                "memberBicep",memberBicep,
                "memberLeftEye",memberLeftEye,
                "memberRightEye",memberRightEye);
        SLog.info("params[%s]",params.toString());
        Api.postUI(Api.PATH_MEASUREMENT_EDIT, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]",responseStr);
                    ToastUtil.success(_mActivity,"保存成功");
                } catch (Exception e){
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("__onSupportVisible");
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SLog.info("__onSupportInvisible");
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
