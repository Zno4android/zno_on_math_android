package com.example.jatcool.zno_on_math;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ZnoApi {
    @GET("/allUsers")
    public Call<List<User>> getAllUsers();
//    @POST("/createUser")
//    public Call<User> CreateUsers(@Query("Login") String Login,@Query("Name") String Name,@Query("Email") String Email,@Query("Password") String Password);
////нужно подправить
    @POST("/createUser")
    public Call<User> CreateUsers(@Body User user);
}
