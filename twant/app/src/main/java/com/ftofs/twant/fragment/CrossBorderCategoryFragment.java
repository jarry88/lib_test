package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.gzp.lib_common.base.BaseFragment;

public class CrossBorderCategoryFragment extends BaseFragment implements View.OnClickListener {
    String categoryName;

    public static CrossBorderCategoryFragment newInstance(String categoryName) {
        CrossBorderCategoryFragment fragment = new CrossBorderCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.categoryName = categoryName;
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cross_border_category, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvText = view.findViewById(R.id.tv_text);
        tvText.setText(categoryName);
    }

    @Override
    public void onClick(View v) {

    }
}
