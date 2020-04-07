package com.example.jatcool.zno_on_math.entity;

public enum Status {
    ADMIN, Student, Teacher;

//    public static Status getStatus(User user) {
//        int statusId = user.getStatus();
//        return Status.values()[statusId];
//    }

    public String getName() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return this.getName().toLowerCase();
    }
}
