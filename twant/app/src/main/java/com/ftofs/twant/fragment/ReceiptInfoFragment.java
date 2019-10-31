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
import com.ftofs.twant.entity.Receipt;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;


/**
 * 單據信息Fragment
 * @author zwm
 */
public class ReceiptInfoFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 沒作更改
     */
    public static final int ACTION_NO_CHANGE = 1;
    /**
     * 不開單據
     */
    public static final int ACTION_NO_RECEIPT = 2;
    /**
     * 保存使用
     */
    public static final int ACTION_SAVE_AND_USE = 3;

    EditText etReceiptHeader;
    EditText etReceiptContent;

    int position;
    Receipt receipt;

    /**
     * 工廠方法
     * @param position 訂單項在RecyclerView中的位置
     * @param receipt
     * @return
     */
    public static ReceiptInfoFragment newInstance(int position, Receipt receipt) {
        Bundle args = new Bundle();

        args.putInt("position", position);
        if (receipt != null) {
            args.putParcelable("receipt", receipt);
        }
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

        Bundle args = getArguments();
        position = args.getInt("position");
        receipt = args.getParcelable("receipt");

        etReceiptHeader = view.findViewById(R.id.et_receipt_header);
        etReceiptContent = view.findViewById(R.id.et_receipt_content);

        if (receipt != null) {
            etReceiptHeader.setText(receipt.header);
            etReceiptContent.setText(receipt.content);
        }

        Util.setOnClickListener(view, R.id.btn_back, this);
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
        bundle.putInt("position", position);

        if (action == ACTION_SAVE_AND_USE) {
            // 傳遞單據數據信息
            if (receipt == null) {
                receipt = new Receipt();
            }
            receipt.header = etReceiptHeader.getText().toString().trim();
            receipt.content = etReceiptContent.getText().toString().trim();
            receipt.taxPayerId = "";

            if (StringUtil.isEmpty(receipt.header)) {
                ToastUtil.error(_mActivity, getString(R.string.input_receipt_header_hint));
                return;
            }
            if (StringUtil.isEmpty(receipt.content)) {
                ToastUtil.error(_mActivity, getString(R.string.input_receipt_content_hint));
                return;
            }
            bundle.putParcelable("receipt", receipt);
        }
        setFragmentResult(RESULT_OK, bundle);
        hideSoftInput();
        pop();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop(ACTION_NO_CHANGE);
        return true;
    }
}
