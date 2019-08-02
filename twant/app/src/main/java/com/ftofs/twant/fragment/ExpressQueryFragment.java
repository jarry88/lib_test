package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

/**
 * 快遞信息查詢Fragment
 * @author zwm
 */
public class ExpressQueryFragment extends BaseFragment implements View.OnClickListener {
    EditText etOrderNumber;

    /**
     * Constructor
     * @param orderNumber  初始的單號
     * @return
     */
    public static ExpressQueryFragment newInstance(String orderNumber) {
        Bundle args = new Bundle();

        args.putString("orderNumber", orderNumber);
        ExpressQueryFragment fragment = new ExpressQueryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_express_query, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String orderNumber = args.getString("orderNumber");

        etOrderNumber = view.findViewById(R.id.et_order_number);
        etOrderNumber.setText(orderNumber);
        etOrderNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    Bundle bundle = new Bundle();
                    String orderNumber = etOrderNumber.getText().toString().trim();

                    bundle.putString("queryOrderNumber", orderNumber);
                    setFragmentResult(RESULT_OK, bundle);
                    pop();

                    return true;
                }
                return false;
            }
        });
        Util.setOnClickListener(view, R.id.btn_back, this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
