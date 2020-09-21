package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;

import org.jetbrains.annotations.NotNull;


/**
 * 选择出售中商品设置为镇店之宝列表页 
 * 需求913镇店之宝功能
 * @author gzp
 */
public class SetFeatureListFragment extends BaseFragment {


    void back() {
        hideSoftInputPop();
    }


    TextView tvTitle;

    public static SetFeatureListFragment newInstance() {
        return new SetFeatureListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_feature_fragment, container, false);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        tvTitle.setText("出售中的商品");
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadDate();
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

