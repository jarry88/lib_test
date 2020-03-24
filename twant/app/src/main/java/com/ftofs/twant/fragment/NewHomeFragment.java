package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
import com.tmall.wireless.tangram.TangramEngine;

import org.json.JSONArray;

public class NewHomeFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvList;
    TangramEngine tangramEngine;

    public static NewHomeFragment newInstance() {
        Bundle args = new Bundle();

        NewHomeFragment fragment = new NewHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tangramEngine = ((MainActivity) _mActivity).getTangramEngine();
        rvList = view.findViewById(R.id.rv_list);

        // 绑定 RecyclerView
        tangramEngine.bindView(rvList);

        // 监听 RecyclerView 的滚动事件
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //在 scroll 事件中触发 engine 的 onScroll，内部会触发需要异步加载的卡片去提前加载数据
                tangramEngine.onScrolled();
            }
        });

        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        tangramEngine.unbindView();
    }

    private void loadData() {
        String json = AssetsUtil.loadText(_mActivity, "tangram/home.json");
        SLog.info("json[%s]", json);

        try {
            JSONArray data = new JSONArray(json);
            tangramEngine.setData(data);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }


}
