package com.example.jatcool.zno_on_math.connection;

import com.example.jatcool.zno_on_math.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ZnoApi {

    @POST("/api/user/register")
    public Call<User> CreateUsers(@Body User user);
    @POST("/api/user/login")
    public Call<User> getUser(@Body User user);
    @POST ("/api/user/emailValidation")
    public Call<User> isEmailExist(@Query ("email") String email);
}
