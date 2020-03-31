package com.example.jatcool.zno_on_math.entity.builder;

import com.example.jatcool.zno_on_math.entity.User;

import static com.example.jatcool.zno_on_math.util.Validation.isEqualsPassword;
import static com.example.jatcool.zno_on_math.util.Validation.isValidEmail;
import static com.example.jatcool.zno_on_math.util.Validation.isValidName;
import static com.example.jatcool.zno_on_math.util.Validation.isValidPassword;

public class UserBean {
    private String email;
    private String password;
    private String confirmPassword;
    private String group;
    private String firstname;
    private String lastname;
    private String ot;

    public UserBean() {
    }

    public User createUser() {
        User user = new User();

        user.setEmail(email);
        user.setGroup(group);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setOt(ot);
        user.setPassword(password);

        return user;
    }

    public boolean validateEmail() {
        return isValidEmail(email);
    }

    public boolean validatePassword() {
        return isValidPassword(password);
    }

    public boolean validateConfirmPassword() {
        return isEqualsPassword(password, confirmPassword);
    }

    public boolean validateFirstname() {
        return isValidName(firstname);
    }

    public boolean validateLastname() {
        return isValidName(lastname);
    }

    public boolean validateOt() {
        return isValidName(ot);
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
