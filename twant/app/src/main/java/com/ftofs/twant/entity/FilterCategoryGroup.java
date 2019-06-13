package com.ftofs.twant.entity;



import java.util.ArrayList;
import java.util.List;

public class FilterCategoryGroup {
    public FilterCategoryGroup() {
        this.list = new ArrayList<>();
    }

    public FilterCategoryItem head;
    public List<FilterCategoryItem> list;
}
