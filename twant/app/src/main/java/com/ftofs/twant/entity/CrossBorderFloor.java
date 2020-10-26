package com.ftofs.twant.entity;

import java.util.ArrayList;
import java.util.List;

public class CrossBorderFloor {
    /*
                    "floorHeadline": "標題1",
                            "floorSubhead": "副標題1",
                            "floorType": "banner",

     */
    public int floorId;
    public String floorHeadline;
    public String floorSubhead;
    public String floorType;
    public List<FloorItem> floorItemList = new ArrayList<>();
}
