package com.ftofs.twant.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.widget.ScaledButton;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class TwantCaptureActivity extends BaseActivity {
    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_capture);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initView();
    }
    public static boolean isOpen = false;

    private void initView() {
        LinearLayout linearLayout = findViewById(R.id.linear1);
        ScaledButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v->{
            TwantCaptureActivity.this.finish();
        });
        linearLayout.setOnClickListener(v -> {
            if (!isOpen) {
                CodeUtils.isLightEnable(true);
                isOpen = true;
            } else {
                CodeUtils.isLightEnable(false);
                isOpen = false;
            }

        });
    }


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            TwantCaptureActivity.this.setResult(RESULT_OK, resultIntent);
            TwantCaptureActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            TwantCaptureActivity.this.setResult(RESULT_OK, resultIntent);
            TwantCaptureActivity.this.finish();
        }
    };
}
