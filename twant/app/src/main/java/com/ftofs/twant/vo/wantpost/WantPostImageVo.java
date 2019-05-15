package com.ftofs.twant.vo.wantpost;

import com.ftofs.twant.domain.wantpost.WantPostImage;

/**
 * @Description: 貼文圖片視圖對象
 * @Auther: yangjian
 * @Date: 2019/3/21 16:00
 */
public class WantPostImageVo extends WantPostImage{

    public WantPostImageVo(){}

    public WantPostImageVo(WantPostImage w){
        this.setImageUrl(w.getImageUrl());
    }

}
