package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;

import java.util.Calendar;
import java.util.Date;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    public static TestFragment newInstance() {
        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_test, this);

        SimpleTabManager manager = new SimpleTabManager(1) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                SLog.info("isRepeat[%s]", isRepeat);

                int id = v.getId();
                if (id == R.id.btn_my_like) {
                    SLog.info("btn_my_like");
                } else if (id == R.id.btn_my_follow) {
                    SLog.info("btn_my_follow");
                } else if (id == R.id.btn_my_comment) {
                    SLog.info("btn_my_comment");
                }
            }
        };
        manager.add(view.findViewById(R.id.btn_my_like));
        manager.add(view.findViewById(R.id.btn_my_follow));
        manager.add(view.findViewById(R.id.btn_my_comment));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_test) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new TwConfirmPopup(_mActivity, "確定要刪除這個地址嗎？", "廣東省珠海市香洲區人民東路", new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    }))
                    .show();

        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
