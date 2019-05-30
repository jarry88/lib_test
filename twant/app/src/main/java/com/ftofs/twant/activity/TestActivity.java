package com.ftofs.twant.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.adapter.ViewGroupTestAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.widget.TwTabButton;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

/**
 * 測試用
 * @author zwm
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        List<String> stringList = new ArrayList<>();
        stringList.add("aa");
        stringList.add("bb");
        stringList.add("cc");

        LinearLayout llContainer = findViewById(R.id.ll_container);
        ViewGroupAdapter adapter = new ViewGroupTestAdapter(this, llContainer, R.layout.store_voucher_item);
        adapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                SLog.info("POSITION[%d]", position);
            }
        });
        adapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                int id = view.getId();
                SLog.info("POSITION[%d], viewId[%d]", position, view.getId());
                TextView textView = (TextView) view;

                if (id == R.id.btn_receive_voucher_now) {
                    textView.setTextColor(Color.parseColor("#000000"));
                } else if (id == R.id.tv_store_name) {
                    textView.setTextColor(Color.parseColor("#00ff00"));
                }
            }
        });
        adapter.setData(stringList);
    }
}
