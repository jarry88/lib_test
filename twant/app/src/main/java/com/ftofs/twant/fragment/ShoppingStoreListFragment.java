package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ftofs.twant.R;

/**
 * 購物專場店鋪列表子頁面
 * @author gzp
 */
public class ShoppingStoreListFragment extends BaseFragment {

    private ShoppingSpecialFragment parentFragment;

    public static ShoppingStoreListFragment newInstance(ShoppingSpecialFragment shoppingSpecialFragment) {
        ShoppingStoreListFragment fragment=new ShoppingStoreListFragment();
        fragment.parentFragment = shoppingSpecialFragment;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_rv_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
