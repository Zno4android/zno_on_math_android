package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User extends Entity {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
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
    @SerializedName("token")
    @Expose
    private String token;

    public User(String id, String email, String password, String group, String firstname, String lastname, String ot) {
        this.setId(id);
        this.email = email;
        this.password = password;
        //this.status = 0;
        this.group = group;
        this.firstname = firstname;
        this.lastname = lastname;
        this.ot = ot;
    }

    public User(String email, String password, String group, String firstname, String lastname, String ot) {
        this.email = email;
        this.password = password;
        this.group = group;
        this.firstname = firstname;
        this.lastname = lastname;
        this.ot = ot;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String ot,String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
        this.ot = ot;

    }

    public User() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    @Override
    public String toString() {
        return "{" +
                "email:'" + email + '\'' +
                ", password:'" + password + '\'' +
                ", group:'" + group + '\'' +
                ", firstname:'" + firstname + '\'' +
                ", lastname:'" + lastname + '\'' +
                ", ot:'" + ot + '\'' +
                '}';
    }
}
