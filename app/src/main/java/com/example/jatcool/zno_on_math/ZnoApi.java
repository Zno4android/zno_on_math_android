package com.example.jatcool.zno_on_math;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ZnoApi {

    @POST("/api/user/register")
    public Call<User> CreateUsers(@Body User user);
    @POST("/api/user/login")
    public Call<User> getUser(@Body User user);
}
