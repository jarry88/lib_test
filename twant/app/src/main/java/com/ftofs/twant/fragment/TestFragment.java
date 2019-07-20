package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AdjustButton;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    AdjustButton abQuantity;
    public static TestFragment newInstance() {
        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.setOnClickListener(view, R.id.btn_test, this);

        abQuantity = view.findViewById(R.id.ab_quantity);
        abQuantity.setMinValue(1, new AdjustButton.OutOfValueCallback() {
            @Override
            public void outOfValue() {
                SLog.info("outOfMinValue, %d", abQuantity.getValue());
            }
        });

        abQuantity.setMaxValue(5, new AdjustButton.OutOfValueCallback() {
            @Override
            public void outOfValue() {
                SLog.info("outOfMaxValue, %d", abQuantity.getValue());
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_test) {
            MainFragment mainFragment = MainFragment.getInstance();
            mainFragment.start(ChatFragment.newInstance());
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
