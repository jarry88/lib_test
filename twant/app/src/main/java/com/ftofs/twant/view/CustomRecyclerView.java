package com.ftofs.twant.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * @author gzp
 */
public class CustomRecyclerView extends RecyclerView {
    private int mTouchSlop;
    public CustomRecyclerView(Context context) {
        super(context);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }


    int move_x,move_y;
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){

            case MotionEvent.ACTION_DOWN:
                move_x = (int) e.getX();
                move_y = (int) e.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                Log.e("motion_event","down   x==y  "+move_x+" ==== "+move_y);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("motion_event","move   x==y  "+move_x+" ==== "+move_y);
                int y = (int) e.getY();
                int x = (int) e.getX();
                if(Math.abs(y-move_y)>mTouchSlop&&Math.abs(x-move_x)<mTouchSlop*2){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("motion_event","up   x==y  "+move_x+" ==== "+move_y);
                break;
            default:
                break;
        }
        return super.onTouchEvent(e);
    }
}
