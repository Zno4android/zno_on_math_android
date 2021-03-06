package com.example.jatcool.zno_on_math.connection;

import com.example.jatcool.zno_on_math.entity.Group;
import com.example.jatcool.zno_on_math.entity.ResultQuestions;
import com.example.jatcool.zno_on_math.entity.Statistics;
import com.example.jatcool.zno_on_math.entity.Test;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.entity.dbEntity.DBResultQuestion;
import com.example.jatcool.zno_on_math.entity.dbEntity.DBStatistics;
import com.example.jatcool.zno_on_math.util.MailCheck;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ZnoApi {

    @POST("/api/user/register")
    Call<User> CreateUsers(@Body User user);

    @POST("/api/user/login")
    Call<User> Log_in(@Body User user);

    @GET("/api/user/verifyEmail")
    Call<MailCheck> isEmailExist(@Query("email") String email);

    @GET("/api/user/getGroup")
    Call<Group> GetGroup();

    @PUT("/api/profile/updateUserData")
    Call<User> Change(@Header("auth-token") String token, @Body User user);

    @GET("/api/profile/getUserData")
    Call<User> getUserData(@Header("auth-token") String token);

    @POST("/api/statistics/generalStatistics")
    Call<DBStatistics> updateStatistics(@Body DBStatistics statistics);

    @POST("/api/statistics/questionStatistics")
    Call<DBResultQuestion> updateResultQuestion(@Body DBResultQuestion resultQuestions);

    @GET("/api/test/getTest")
    Call<Test> getTest(@Query("testId") String testId);
}
