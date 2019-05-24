package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;


/**
 * 發票信息Fragment
 * @author zwm
 */
public class ReceiptInfoFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 沒作更改
     */
    public static final int ACTION_NO_CHANGE = 1;
    /**
     * 不開發票
     */
    public static final int ACTION_NO_RECEIPT = 2;
    /**
     * 保存使用
     */
    public static final int ACTION_SAVE_AND_USE = 3;

    EditText etReceiptHeader;
    EditText etReceiptContent;
    EditText etTaxPayerId;

    public static ReceiptInfoFragment newInstance(int position) {
        Bundle args = new Bundle();

        ReceiptInfoFragment fragment = new ReceiptInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etReceiptHeader = view.findViewById(R.id.et_receipt_header);
        etReceiptContent = view.findViewById(R.id.et_receipt_content);
        etTaxPayerId = view.findViewById(R.id.et_taxpayer_id);

        Util.setOnClickListener(view, R.id.btn_no_receipt, this);
        Util.setOnClickListener(view, R.id.btn_save_and_use, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop(ACTION_NO_CHANGE);
                break;
            case R.id.btn_no_receipt:
                pop(ACTION_NO_RECEIPT);
                break;
            case R.id.btn_save_and_use:
                pop(ACTION_SAVE_AND_USE);
                break;
            default:
                break;
        }
    }

    private void pop(int action) {
        Bundle bundle = new Bundle();

        bundle.putString("from", ReceiptInfoFragment.class.getName());
        bundle.putInt("action", action);

        if (action == ACTION_SAVE_AND_USE) {
            // 傳遞發票數據信息
            bundle.putString("receiptHeader", etReceiptHeader.getText().toString());
            bundle.putString("receiptContent", etReceiptContent.getText().toString());
            bundle.putString("taxPayerId", etTaxPayerId.getText().toString());
        }
        setFragmentResult(RESULT_OK, bundle);
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop(ACTION_NO_CHANGE);
        return true;
    }
}
