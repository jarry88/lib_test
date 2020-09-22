package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.base.Jarbon;
import com.ftofs.twant.util.Util;

import org.jetbrains.annotations.NotNull;

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
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_double_eleven, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        long now = System.currentTimeMillis();
        long doubleElevenTimestamp = Jarbon.parse("2019-11-11").getTimestampMillis();
        if (now < doubleElevenTimestamp) { // 未到雙十一
            ImageView preDoubleElevenBg = view.findViewById(R.id.pre_double_eleven_bg);
            Glide.with(_mActivity).load(R.drawable.pre_double_eleven_bg).into(preDoubleElevenBg);

            view.findViewById(R.id.rl_pre_double_eleven).setVisibility(View.VISIBLE);
            view.findViewById(R.id.rl_after_double_eleven).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.btn_go_shopping).setOnClickListener(this);
            view.findViewById(R.id.btn_play_game).setOnClickListener(this);
            view.findViewById(R.id.btn_view_award).setOnClickListener(this);

            view.findViewById(R.id.rl_pre_double_eleven).setVisibility(View.GONE);
            view.findViewById(R.id.rl_after_double_eleven).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_go_shopping) {
            Util.startActivityShopping();
        } else if (id == R.id.btn_play_game) {
            String url = Util.makeDoubleElevenH5Url(1);
            if (url == null) {
                Util.showLoginFragment();
                return;
            }
            start(H5GameFragment.newInstance(url, true));
        } else if (id == R.id.btn_view_award) {
            String url = Util.makeDoubleElevenH5Url(2);
            // url = "https://www.snailpad.cn/302.php";
            if (url == null) {
                Util.showLoginFragment();
                return;
            }
            start(H5GameFragment.newInstance(url, true));
        } else if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}


