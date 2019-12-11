package com.example.jatcool.zno_on_math.entity;

public enum Status {
    STUDENT,BANNED;

//    public static Status getStatus(User user) {
//        int statusId = user.getStatus();
//        return Status.values()[statusId];
//    }

    public String getName() {
        return name().toLowerCase();
    }
}
