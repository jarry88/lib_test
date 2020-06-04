package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.log.SLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 商家鎮店之寶添加編輯頁面 需求913
 * @author gzp
 */
public class SellerFeaturesFragment extends BaseFragment {
    private Unbinder unbinder;

    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;

    public static SellerFeaturesFragment newInstance() {
        return new SellerFeaturesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_edit_features_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        tvTitle.setText("鎮店之寶");
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void loadDate() {

    }

    @Override
    public boolean onBackPressedSupport() {
    SLog.info("onBackPressedSupport");
    hideSoftInputPop();
    return true;
    }
}

