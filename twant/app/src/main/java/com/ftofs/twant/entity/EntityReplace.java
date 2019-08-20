package com.ftofs.twant.entity;

public class EntityReplace {
    public int startIndex;
    public int stopIndex;
    public String replacement;

    public EntityReplace(int startIndex, int stopIndex, String replacement) {
        this.startIndex = startIndex;
        this.stopIndex = stopIndex;
        this.replacement = replacement;
    }
}

