package com.ftofs.twant.entity;

import java.util.List;

public class CalcFreightResult {
    public boolean success;
    public String errorMessage;

    public CalcFreightResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }
}
