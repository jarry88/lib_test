package com.ftofs.twant.util;

import android.content.Intent;

public class IntentUtil {
    public static Intent makeOpenSystemAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        // intent.setType("video/*;image/*"); // 视频和照片类型
        intent.setType("image/*"); // 只需要照片類型
        return intent;
    }
}
