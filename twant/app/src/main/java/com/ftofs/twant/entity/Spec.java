package com.ftofs.twant.entity;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 規格
 * @author zwm
 */
public class Spec {
    public int specId;
    public String specName;
    public List<SpecValue> specValueList = new ArrayList<>();

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("specId[%d], specName[%s]", specId, specName));
        for (SpecValue specValue : specValueList) {
            sb.append("\n" + specValue.toString());
        }
        return sb.toString();
    }
}
