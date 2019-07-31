package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 給ViewGroup容器動態添加子View
 * @param <T>
 * @author zwm
 */
public abstract class ViewGroupAdapter<T> {
    protected Context context;

    /**
     * 數據列表
     */
    private List<T> dataList;
    /**
     * 容器
     */
    protected ViewGroup container;
    /**
     * itemView的Layout Id
     */
    private int itemLayoutId;

    /**
     * itemView的點擊監聽器
     */
    private OnItemClickListener itemClickListener;

    /**
     * childView的點擊監聽器
     */
    private OnItemClickListener childClickListener;

    /**
     * 可點擊的childView的Id列表
     */
    private List<Integer> clickableChildrenIds = new ArrayList<>();


    /**
     * 構造方法
     * @param context
     * @param container 容器
     * @param itemLayoutId itemView的布局Id
     */
    public ViewGroupAdapter(Context context, ViewGroup container, int itemLayoutId) {
        this.context = context;
        this.container = container;
        this.itemLayoutId = itemLayoutId;
    }

    /**
     * 添加要監聽點擊事件的childView(!!!!!!注意：必須在Adapter的構造函數中調用)
     * @param ids
     */
    protected void addClickableChildrenId(int... ids) {
        for (int id : ids) {
            clickableChildrenIds.add(id);
        }
    }

    /**
     * 設置數據(!!!!!!注意：setData方法必須放在最后調用)
     * @param dataList
     */
    public void setData(List<T> dataList) {
        this.dataList = dataList;

        container.removeAllViews();
        for (int i = 0; i < dataList.size(); i++) {
            /*
            inflate(resource, root, attachToRoot) 方法的root參數設置為容器的參數，布局resource中的layout參數才
            會起作用，然后attachToRoot設置為false，inflate()方法才會返回itemView，不然會返回容器的View
             */
            final View itemView = LayoutInflater.from(context).inflate(itemLayoutId, container, false);
            container.addView(itemView);
            final int position = i;
            if (itemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onClick(ViewGroupAdapter.this, itemView, position);
                    }
                });
            }

            if (childClickListener != null) {
                for (int childViewId : clickableChildrenIds) {
                    final View childView = itemView.findViewById(childViewId);
                    if (childView == null) {
                        continue;
                    }
                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            childClickListener.onClick(ViewGroupAdapter.this, childView, position);
                        }
                    });
                }
            }

            bindView(i, itemView, dataList.get(i));
        }
    }

    /**
     * 設置itemView的點擊監聽器
     * @param onClickListener
     */
    public void setItemClickListener(OnItemClickListener onClickListener) {
        this.itemClickListener = onClickListener;
    }

    /**
     * 設置childView的點擊監聽器
     * @param onClickListener
     */
    public void setChildClickListener(OnItemClickListener onClickListener) {
        this.childClickListener = onClickListener;
    }

    /**
     * View和數據的綁定方法
     * @param position
     * @param itemView
     * @param itemData
     */
    public abstract void bindView(int position, View itemView, T itemData);

    /**
     * 點擊事件監聽器接口
     * itemView和childView都共用這個接口
     */
    public interface OnItemClickListener {
        void onClick(ViewGroupAdapter adapter, View view, int position);
    }


    public void setText(View itemView, int id, CharSequence text) {
        TextView textView = itemView.findViewById(id);
        if (textView != null) {
            textView.setText(text);
        }
    }

    public void setBackgroundResource(View itemView, int id, int resid) {
        View view = itemView.findViewById(id);
        if (view != null) {
            view.setBackgroundResource(resid);
        }
    }
}
