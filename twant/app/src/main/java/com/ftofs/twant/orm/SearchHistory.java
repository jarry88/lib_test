package com.ftofs.twant.orm;

import org.litepal.crud.LitePalSupport;

/**
 * 搜索历史
 * @author zwm
 */
public class SearchHistory extends LitePalSupport {
    public String keyword;
    public int createTime;
}
