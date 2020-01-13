package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group extends Entity {
    @SerializedName("group")
    @Expose
    List<String> name;

    public Group(String id, List<String> name) {
        this.setId(id);
        this.name = name;
    }


    public String getNameforIndex(int index) {
        return name.get(index);
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name.add(name);
    }
}
