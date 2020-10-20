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
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 保存銀行卡信息Fragment
 * @author zwm
 */
public class BankCardFragment extends BaseFragment implements View.OnClickListener {
    EditText etName;
    EditText etBankName;
    EditText etBankAccount;

    public static BankCardFragment newInstance() {
        BankCardFragment fragment = new BankCardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_name);
        etBankName = view.findViewById(R.id.et_bank_name);
        etBankAccount = view.findViewById(R.id.et_bank_account);
        etBankAccount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveData();
                }
                return false;
            }
        });

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_save, this);
    }


    private void saveData() {
        String url = Api.PATH_SAVE_DISTRIBUTION_WITHDRAW_ACCOUNT;

        String name = etName.getText().toString().trim();
        String bankName = etBankName.getText().toString().trim();
        String bankAccount = etBankAccount.getText().toString().trim();

        if (StringUtil.isEmpty(name)) {
            ToastUtil.error(_mActivity, "請填寫收件人的真實姓名");
            return;
        }

        if (StringUtil.isEmpty(bankName)) {
            ToastUtil.error(_mActivity, "請輸入開戶行名稱");
            return;
        }

        if (StringUtil.isEmpty(bankAccount)) {
            ToastUtil.error(_mActivity, "請輸入銀行帳號");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "payeeName", name,
                "bankAccountName", bankName,
                "accountNumber", bankAccount
        );
        SLog.info("url[%s], params[%s]", url, params);
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

                    ToastUtil.success(_mActivity, "保存成功");

                    Bundle bundle = new Bundle();
                    bundle.putBoolean("success", true);
                    setFragmentResult(RESULT_OK, bundle);

                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_save) {
            saveData();
        } else if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
