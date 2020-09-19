package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ImageListAdapter;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImageFragment extends BaseFragment implements View.OnClickListener {
    int initIndex; // 初始显示哪一张，从0开始
    List<String> imageList;
    RecyclerView rvList;

    ImageListAdapter adapter;

    int currGalleryPosition;
    TextView tvPageIndex;

    public static ImageFragment newInstance(int initIndex, List<String> imageList) {
        Bundle args = new Bundle();

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);

        fragment.initIndex = initIndex;
        fragment.imageList = imageList;

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvPageIndex = view.findViewById(R.id.tv_page_index);

        currGalleryPosition = initIndex;
        rvList = view.findViewById(R.id.rv_list);

        // 使RecyclerView像ViewPager一样的效果，一次只能滑一页，而且居中显示
        // https://www.jianshu.com/p/e54db232df62
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        (new PagerSnapHelper()).attachToRecyclerView(rvList);

        adapter = new ImageListAdapter(_mActivity, R.layout.image_list_item, imageList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("ImageListAdapter::onItemClick()");
                pop();
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("ImageListAdapter::onItemChildClick()");
                pop();
            }
        });

        rvList.setAdapter(adapter);
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    currGalleryPosition = getCurrPosition();
                    SLog.info("currPosition[%d]", currGalleryPosition);

                    updatePageIndexView();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        updatePageIndexView();
        rvList.scrollToPosition(initIndex);
    }

    private void updatePageIndexView() {
        String pageIndexStr = (currGalleryPosition + 1) + "/" + imageList.size();
        tvPageIndex.setText(pageIndexStr);
    }

    /**
     * 获取当前显示的图片的position
     *
     * @return
     */
    private int getCurrPosition() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvList.getLayoutManager();
        int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        return position;
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
        hideSoftInputPop();
        return true;
    }
}

