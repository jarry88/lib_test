package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;


/**
 * 我的關注Fragment
 * @author zwm
 */
public class MyFollowFragment extends BaseFragment implements View.OnClickListener {
    public static final int TAB_INDEX_STORE = 0;
    public static final int TAB_INDEX_GOODS = 1;
    public static final int TAB_INDEX_ARTICLE = 2;
    public static final int TAB_INDEX_RECRUITMENT = 3;
    public static final int TAB_INDEX_MEMBER = 4;

    int currTabIndex = TAB_INDEX_STORE;

    public static MyFollowFragment newInstance() {
        Bundle args = new Bundle();

        MyFollowFragment fragment = new MyFollowFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_follow, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        SimpleTabManager simpleTabManager = new SimpleTabManager(TAB_INDEX_STORE) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                if (isRepeat) {
                    return;
                }

                int id = v.getId();
                if (id == R.id.btn_store) {
                    currTabIndex = TAB_INDEX_STORE;

                } else if (id == R.id.btn_goods) {
                    currTabIndex = TAB_INDEX_GOODS;

                } else if (id == R.id.btn_article) {
                    currTabIndex = TAB_INDEX_ARTICLE;

                } else if (id == R.id.btn_recruitment) {
                    currTabIndex = TAB_INDEX_RECRUITMENT;

                } else if (id == R.id.btn_member) {
                    currTabIndex = TAB_INDEX_MEMBER;
                    
                }
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_store));
        simpleTabManager.add(view.findViewById(R.id.btn_goods));
        simpleTabManager.add(view.findViewById(R.id.btn_article));
        simpleTabManager.add(view.findViewById(R.id.btn_recruitment));
        simpleTabManager.add(view.findViewById(R.id.btn_member));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
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
