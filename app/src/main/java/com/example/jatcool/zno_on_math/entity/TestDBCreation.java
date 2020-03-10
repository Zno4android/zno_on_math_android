package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestDBCreation {
    @SerializedName("test")
    @Expose
    private Test test;

    public TestDBCreation(Test test) {
        this.test = test;
    }
}
