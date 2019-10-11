package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.WalletPayPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener, CommonCallback {

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
        Util.setOnClickListener(view, R.id.btn_test2, this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_test) {
            new XPopup.Builder(getContext())
                    .popupAnimation(PopupAnimation.TranslateFromBottom)
                    .hasStatusBarShadow(true)
                    .asCustom(new WalletPayPopup(_mActivity, 0, 25.13f, this))
                    .show();
        } else if (id == R.id.btn_test2) {

        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public String onSuccess(@Nullable String data) {
        SLog.info("onSuccess");
        return null;
    }

    @Override
    public String onFailure(@Nullable String data) {
        SLog.info("onFailure");
        return null;
    }
}
