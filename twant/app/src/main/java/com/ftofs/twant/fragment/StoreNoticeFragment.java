package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreAnnouncement;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.LockableNestedScrollView;

import java.util.List;

/**
 * @author gzp
 * 店鋪公告
 */
public class StoreNoticeFragment extends ScrollableBaseFragment {
    LinearLayout llContainer;
    LockableNestedScrollView nestedScrollView;
    public static StoreNoticeFragment newInstance() {
        StoreNoticeFragment fragment = new StoreNoticeFragment();
        return fragment;
    }

    // TabFragment是否可以滚动
    boolean scrollable = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_announcement, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llContainer = view.findViewById(R.id.ll_container);
        nestedScrollView = view.findViewById(R.id.sv_container);
        nestedScrollView.setScrollable(scrollable);
    }

    public void setAnnouncementData(List<StoreAnnouncement> storeAnnouncementList) {
        for (StoreAnnouncement announcement : storeAnnouncementList) {
            View announcementItemView = LayoutInflater.from(_mActivity).inflate(R.layout.store_announcement_item, llContainer, false);
            TextView tvAnnouncementTitle = announcementItemView.findViewById(R.id.tv_announcement_title);
            tvAnnouncementTitle.setText(announcement.title);
            tvAnnouncementTitle.setOnClickListener(v -> {
                SLog.info("onClick, announcement.title[%s], announcement.content[%s]", announcement.title, announcement.content);
                Util.startFragment(StoreAnnouncementDetailFragment.newInstance(announcement.title, announcement.content));
            });

            llContainer.addView(announcementItemView);
        }
    }


    @Override
    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
        if (nestedScrollView != null) {
            nestedScrollView.setScrollable(scrollable);
        }
    }
}
