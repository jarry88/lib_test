package com.ftofs.twant.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ftofs.twant.R;
/*
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
*/

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FBShareActivity extends BaseActivity {
    // UMShareListener umShareListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);

        /*
        umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Toast.makeText(FBShareActivity.this, share_media + " 分享開始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Toast.makeText(FBShareActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(FBShareActivity.this, platform + " 分享失敗啦", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(FBShareActivity.this, platform + " 分享取消啦", Toast.LENGTH_SHORT).show();
            }
        };

        Button btnShare = findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(FBShareActivity.class.getSimpleName(), "SHARE...");
                String url = "https://img.twant.com/image/32/72/3272e13d3a4619aafdb59074cdb04226.jpg";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        UMImage umImage = new UMImage(getApplicationContext(), bitmap);
                        UMWeb umWeb = new UMWeb("http://www.twant.com/web/store/78", "title", "desc", umImage);
                        new ShareAction(FBShareActivity.this)
                                .setPlatform(SHARE_MEDIA.FACEBOOK)
                                .setCallback(umShareListener)
                                .withMedia(umWeb)
                                .share();
                    }
                });
            }
        });
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
