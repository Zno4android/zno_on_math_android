package com.example.jatcool.zno_on_math.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MailCheck {
    @SerializedName("verify")
    @Expose
    private boolean isEmail;

    public boolean isEmail() {
        return isEmail;
    }

    public void setEmail(boolean email) {
        isEmail = email;
    }
}
