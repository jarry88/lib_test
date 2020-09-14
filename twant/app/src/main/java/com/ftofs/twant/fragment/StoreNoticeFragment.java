package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.entity.StoreAnnouncement;
import com.gzp.lib_common.utils.SLog;
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
    List<StoreAnnouncement> storeAnnouncementList;

    boolean viewStubInflated;

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
        this.storeAnnouncementList = storeAnnouncementList;
        if (storeAnnouncementList == null || storeAnnouncementList.size() == 0) {
            View announcementBlackItemView = LayoutInflater.from(_mActivity).inflate(R.layout.store_black_announcement_item, llContainer, false);
            llContainer.addView(announcementBlackItemView);
        }
        for (StoreAnnouncement announcement : storeAnnouncementList) {
            View announcementItemView = LayoutInflater.from(_mActivity).inflate(R.layout.store_announcement_item, llContainer, false);
            TextView tvAnnouncementTitle = announcementItemView.findViewById(R.id.tv_announcement_title);
            tvAnnouncementTitle.setText(announcement.title);
            TextView tvAnnouncementTime = announcementItemView.findViewById(R.id.tv_announcement_time);
            tvAnnouncementTime.setText(Time.fromMillisUnixtime(announcement.createTime,"Y-m-d H:i:s"));

            tvAnnouncementTitle.setOnClickListener(v -> {
                SLog.info("onClick, announcement.id[%d], announcement.title[%s], announcement.content[%s]",
                        announcement.id, announcement.title, announcement.content);
                // Util.startFragment(StoreAnnouncementDetailFragment.newInstance(announcement));
                String url = Config.WEB_BASE_URL + "/store/announcement/" + announcement.id;
                Util.startFragment(H5GameFragment.newInstance(url, announcement.title));
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

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        View contentView = getView();

        if (!viewStubInflated && contentView != null && (storeAnnouncementList == null || storeAnnouncementList.size() == 0)) {

            ViewStub vsEmptyView = getView().findViewById(R.id.vs_empty_view);
            if (vsEmptyView != null) {
                vsEmptyView.inflate();
                viewStubInflated = true;

                TextView tvEmptyHint = contentView.findViewById(R.id.tv_empty_hint);
                tvEmptyHint.setText("暂时还没有公告哦~");
            }
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
