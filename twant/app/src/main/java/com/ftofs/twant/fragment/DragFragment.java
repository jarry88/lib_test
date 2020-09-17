package com.ftofs.twant.fragment;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SquareGridLayout;

public class DragFragment extends BaseFragment implements View.OnClickListener {
    SquareGridLayout imageContainer;

    int imageViewCount = 5;

    View currentDragView;
    View vwTest;

    public static DragFragment newInstance() {
        Bundle args = new Bundle();

        DragFragment fragment = new DragFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_test).setOnClickListener(this);

        vwTest = view.findViewById(R.id.vw_test);
        vwTest.setTag("aaa");
        vwTest.setTag(R.id.data_absolute_path, "bbb");
        vwTest.setTag(R.id.data_index, "ccc");

        imageContainer = view.findViewById(R.id.image_container);
        imageContainer.setOnDragListener((v, event) -> {
            // SLog.info("containerLayout", "event.getAction():" + event.getAction());
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENDED: //拖拽停止
                    //your operation
                    currentDragView.setVisibility(View.VISIBLE);
                    SLog.info("DragEvent.ACTION_DRAG_ENDED");
                    updateTagData();
                    break;
                case DragEvent.ACTION_DROP://拖拽中
                    //your operation
                    SLog.info("DragEvent.ACTION_DROP");
                    currentDragView.setVisibility(View.VISIBLE);
                    updateTagData();
                    break;
                default:
                    // SLog.info("DragEvent.ACTION_DEFAULT[%d]", event.getAction());
                    break;
            }
            return true;
        });

        for (int i = 0; i < imageViewCount; i++) {
            View child = imageContainer.getChildAt(i);
            child.setOnLongClickListener(v -> {
                View.DragShadowBuilder builder = new View.DragShadowBuilder(v);
                v.startDrag(null, builder, v, View.DRAG_FLAG_OPAQUE);//第三个参数是传入一个关于这个view信息的任意对象（getLocalState），它即你需要在拖拽监听中的调用event.getLocalState()获取到这个对象来操作用的(比如传入一个RecyclerView中的position)。如果不需要这个对象，传null
                SLog.info("startDrag");
                v.setVisibility(View.INVISIBLE);
                currentDragView = v;

                Vibrator vibrator = (Vibrator) _mActivity.getSystemService(Service.VIBRATOR_SERVICE);   //获取系统震动服务
                if (vibrator != null) {
                    vibrator.vibrate(70);   //震动70毫秒
                }

                return true;
            });

            child.setOnDragListener(((v, event) -> {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:// 拖拽进入
                        View dragView = (View) event.getLocalState();
                        //之前讲到的v.startDrag(null, builder, null, 0);第三个参数如果传入一个int 类型的值，在这里便可以 通过(int) event.getLocalState()取出来。如果传的是null，上面这行代码就去掉好了
                        int childIndex = (int) child.getTag();
                        int dragViewIndex = (int) dragView.getTag();
                        SLog.info("DragEvent.ACTION_DRAG_ENTERED, child[%s], v[%s]", childIndex, dragViewIndex);
                        Util.exchangeChild(imageContainer, childIndex, dragViewIndex);
                        updateTagData();
                        break;
                    case DragEvent.ACTION_DRAG_EXITED: // 拖拽退出
                        dragView = (View) event.getLocalState();
                        SLog.info("DragEvent.ACTION_DRAG_EXITED, child[%s], v[%s]", child.getTag(), dragView.getTag());
                        break;
                    case DragEvent.ACTION_DROP:
                        dragView = (View) event.getLocalState();
                        SLog.info("DragEvent.ACTION_DROP, child[%s], v[%s]", child.getTag(), dragView.getTag());
                        currentDragView.setVisibility(View.VISIBLE);
                        updateTagData();
                        break;
                        /*
                    case DragEvent.ACTION_DRAG_ENDED: //拖拽停止
                        //your operation
                        currentDragView.setVisibility(View.VISIBLE);
                        SLog.info("DragEvent.ACTION_DRAG_ENDED");
                        updateTagData();
                        break;

                         */
                    default:
                        break;
                }
                return true;
            }));
        } // END OF for

        updateTagData();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_test) {
            SLog.info("%s, %s, %s", vwTest.getTag(), vwTest.getTag(R.id.data_absolute_path), vwTest.getTag(R.id.data_index));
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void updateTagData() {
        for (int i = 0; i < imageViewCount; i++) {
            View child = imageContainer.getChildAt(i);
            child.setTag(i);
        }
    }
}


