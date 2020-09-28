package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.DistributionMemberAdapter;
import com.ftofs.twant.entity.DistributionMember;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的團隊頁面
 * @author zwm
 */
public class MyTeamFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvList;
    DistributionMemberAdapter adapter;
    List<DistributionMember> distributionMemberList = new ArrayList<>();

    public static MyTeamFragment newInstance() {
        MyTeamFragment fragment = new MyTeamFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_team, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        SimpleTabManager tabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }

                if (id == R.id.stb_all) { // 全部
                    SLog.info("全部");
                } else if (id == R.id.stb_first_level) { // 一級同伴
                    SLog.info("一級同伴");
                } else if (id == R.id.stb_second_level) { // 二級同伴
                    SLog.info("二級同伴");
                }
            }
        };
        tabManager.add(view.findViewById(R.id.stb_all));
        tabManager.add(view.findViewById(R.id.stb_first_level));
        tabManager.add(view.findViewById(R.id.stb_second_level));
        tabManager.onSelect(view.findViewById(R.id.stb_all));

        for (int i = 0; i < 10; i++) {
            distributionMemberList.add(new DistributionMember());
        }

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new DistributionMemberAdapter(R.layout.distribution_member_item, distributionMemberList);
        rvList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
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
