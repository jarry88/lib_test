package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.UniversalMemberListAdapter;
import com.ftofs.twant.entity.UniversalMemberItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 【想粉】Fragment
 * @author zwm
 */
public class FollowMeFragment extends BaseFragment implements View.OnClickListener {
    List<UniversalMemberItem> followMeMemberList = new ArrayList<>();
    UniversalMemberListAdapter adapter;

    public static FollowMeFragment newInstance(List<UniversalMemberItem> followMeMemberList) {
        Bundle args = new Bundle();

        FollowMeFragment fragment = new FollowMeFragment();
        fragment.setArguments(args);
        fragment.setFollowMeMemberList(followMeMemberList);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_me, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new UniversalMemberListAdapter(R.layout.universal_member_list_item, followMeMemberList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UniversalMemberItem item = followMeMemberList.get(position);
                start(MemberInfoFragment.newInstance(item.memberName));
            }
        });
        rvList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    public void setFollowMeMemberList(List<UniversalMemberItem> followMeMemberList) {
        this.followMeMemberList = followMeMemberList;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
