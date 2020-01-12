package com.example.jatcool.zno_on_math.connection;

import com.example.jatcool.zno_on_math.entity.Group;
import com.example.jatcool.zno_on_math.entity.ResultQuestions;
import com.example.jatcool.zno_on_math.entity.Statistics;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.util.MailCheck;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ZnoApi {

    @POST("/api/user/register")
    public Call<User> CreateUsers(@Body User user);

    @POST("/api/user/login")
    public Call<User> getUser(@Body User user);

    @GET("/api/user/verifyEmail")
    public Call<MailCheck> isEmailExist(@Query("email") String email);

    @GET("/api/user/getGroup")
    public Call<Group> GetGroup();

    @POST("/api/user/updateStatistics")
    public Call<Statistics> updateStatistics(@Body Statistics statistics);

    @POST("/api/user/updateResultQuestion")
    public Call<ResultQuestions> updateResultQuestion(@Body ResultQuestions resultQuestions);
}
