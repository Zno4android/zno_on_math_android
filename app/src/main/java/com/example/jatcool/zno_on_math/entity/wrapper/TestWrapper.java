package com.example.jatcool.zno_on_math.entity.wrapper;

import com.example.jatcool.zno_on_math.entity.Test;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestWrapper {
    @SerializedName("test")
    @Expose
    private Test test;

    public TestWrapper(Test test) {
        this.test = test;
    }

    public TestWrapper() {
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
