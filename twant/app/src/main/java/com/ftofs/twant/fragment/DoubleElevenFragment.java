package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 雙11活動入口頁面
 * @author zwm
 */
public class DoubleElevenFragment extends BaseFragment implements View.OnClickListener {
    public static DoubleElevenFragment newInstance() {
        Bundle args = new Bundle();

        DoubleElevenFragment fragment = new DoubleElevenFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_double_eleven, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        view.findViewById(R.id.btn_go_shopping).setOnClickListener(this);
        view.findViewById(R.id.btn_play_game).setOnClickListener(this);

        ImageView preDoubleElevenBg = view.findViewById(R.id.pre_double_eleven_bg);
        Glide.with(_mActivity).load(R.drawable.pre_double_eleven_bg).into(preDoubleElevenBg);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_go_shopping) {
            EasyJSONObject params = EasyJSONObject.generate("is_double_eleven", true);
            start(SearchResultFragment.newInstance(SearchType.GOODS.name(), params.toString()));
        } else if (id == R.id.btn_play_game) {
            String url = Util.makeDoubleElevenH5GameUrl();
            if (url == null) {
                Util.showLoginFragment();
                return;
            }
            start(H5GameFragment.newInstance(url));
        } else if (id == R.id.btn_back) {
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


