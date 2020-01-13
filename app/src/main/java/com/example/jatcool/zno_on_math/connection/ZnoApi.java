package com.example.jatcool.zno_on_math.connection;

import com.example.jatcool.zno_on_math.entity.Group;
import com.example.jatcool.zno_on_math.entity.ResultQuestions;
import com.example.jatcool.zno_on_math.entity.Statistics;
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
    public Call<User> CreateUsers(@Body User user);

    @POST("/api/user/login")
    public Call<User> Log_in(@Body User user);

    @GET("/api/user/verifyEmail")
    public Call<MailCheck> isEmailExist(@Query("email") String email);

    @GET("/api/user/getGroup")
    public Call<Group> GetGroup();

    @PUT("/api/profile/updateUserData")
    public Call<User> Change(@Header("auth-token") String token,@Body User user);

    @GET("/api/profile/getUserData")
    public Call<User> getUserData(@Header("auth-token") String token);

    @POST("/api/user/updateStatistics")
    public Call<DBStatistics> updateStatistics(@Body DBStatistics statistics);

    @POST("/api/user/updateResultQuestion")
    public Call<DBResultQuestion> updateResultQuestion(@Body DBResultQuestion resultQuestions);
}
