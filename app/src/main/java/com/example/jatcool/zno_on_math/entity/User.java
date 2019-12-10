package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User extends Entity {
//    @SerializedName("_id")
//    @Expose
//    private String id;
//    @SerializedName("name")
//    @Expose
//    private String name;
//    @SerializedName("email")
//    @Expose
//    private String email;
//    @SerializedName("password")
//    @Expose
//    String password;
//    @SerializedName("status")
//    @Expose
   // boolean status;

//    public boolean isStatus() {
//        return status;
//    }
//
//    public void setStatus(boolean status) {
//        this.status = status;
//    }

//    public User(String name, String email, String password){
////    this.name = name;
////    this.email = email;
////    this.password = password;
////}
////public User(String email, String password){
////    this.email = email;
////    this.password = password;
////}
////    public String getId() {
////        return id;
////    }
////
////    public void setId(String id) {
////        this.id = id;
////    }
////
////    public String getName() {
////        return name;
////    }
////
////    public void setName(String name) {
////        this.name = name;
////    }
////
////    public String getEmail() {
////        return email;
////    }
////
////    public void setEmail(String email) {
////        this.email = email;
////    }
////
////    public String getPassword() {
////        return password;
////    }
////
////    public void setPassword(String password) {
////        this.password = password;
////    }
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("ot")
    @Expose
    private String ot;

    public User(Long id,String email, String password, String group, String firstname, String lastname, String ot) {
        this.setId(id);
        this.email = email;
        this.password = password;
        this.status = 0;
        this.group = group;
        this.firstname = firstname;
        this.lastname = lastname;
        this.ot = ot;
    }
    public User(String email, String password, String group, String firstname, String lastname, String ot) {
        this.email = email;
        this.password = password;
        this.status = 0;
        this.group = group;
        this.firstname = firstname;
        this.lastname = lastname;
        this.ot = ot;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOt() {
        return ot;
    }

    public void setOt(String ot) {
        this.ot = ot;
    }
}
