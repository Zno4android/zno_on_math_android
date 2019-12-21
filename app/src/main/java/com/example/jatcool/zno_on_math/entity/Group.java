package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group extends Entity {
    @SerializedName("name")
    @Expose
    String name;

    public Group(Long id,String name) {
        this.setId(id);
        this.name = name;
    }

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
