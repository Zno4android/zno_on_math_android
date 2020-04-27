package com.example.jatcool.zno_on_math.entity;

import android.graphics.Bitmap;

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
    @SerializedName("fathername")
    @Expose
    private String fathername;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("verifyed")
    @Expose
    private Boolean verifyed;
    @SerializedName("image")
    @Expose
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isVerifyed() {
         return verifyed;
    }

    public void setVerifyed(boolean verifyed) {
        this.verifyed = verifyed;
    }

    @Override
    public String toString() {
        return "{" +
                "email:'" + email + '\'' +
                ", password:'" + password + '\'' +
                ", group:'" + group + '\'' +
                ", firstname:'" + firstname + '\'' +
                ", lastname:'" + lastname + '\'' +
                ", ot:'" + fathername + '\'' +
                '}';
    }
}
