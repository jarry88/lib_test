package com.ftofs.twant.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.widget.TwTabButton;

/**
 * 測試用
 * @author zwm
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TwTabButton twTabButton = findViewById(R.id.btn_test);
        twTabButton.setTtbOnClickListener(new TwTabButton.TtbOnClickListener() {
            @Override
            public void onClick(TwTabButton tabButton) {
                SLog.info("ON CLICK");
            }
        });

        twTabButton.setTtbOnSelectListener(new TwTabButton.TtbOnSelectListener() {
            @Override
            public void onSelect(TwTabButton tabButton) {
                SLog.info("ON SELECT");
            }
        });
    }
}
