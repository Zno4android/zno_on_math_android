package com.example.jatcool.zno_on_math.connection;

import com.example.jatcool.zno_on_math.entity.Group;
import com.example.jatcool.zno_on_math.entity.StatisticsWrapper;
import com.example.jatcool.zno_on_math.entity.Test;
import com.example.jatcool.zno_on_math.entity.TestWrapper;
import com.example.jatcool.zno_on_math.entity.Theoretics;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.entity.dbEntity.DBResultQuestion;
import com.example.jatcool.zno_on_math.util.MailCheck;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @POST("/api/statistics/saveStatistics")
    Call<StatisticsWrapper> saveStatistics(@Header("auth-token") String token, @Body StatisticsWrapper statistics);

    @POST("/api/statistics/questionStatistics")
    Call<DBResultQuestion> updateResultQuestion(@Body DBResultQuestion resultQuestions);

    @GET("/api/tests/getTest")
    Call<TestWrapper> getTest(@Header("auth-token") String token, @Query("name") String testId);

    @POST("/api/tests/createTest")
    Call<Test> createTest(@Header("auth-token") String token, @Body TestWrapper testWrapper);

    @GET("api/theory")
    Call<List<Theoretics>> getTheory (@Header("auth-token") String token);

    @PUT("/api/theory/update")
    Call<Theoretics> editTheory (@Header("auth-token") String token, @Body Theoretics theoretics);

    @POST("/api/theory/add")
    Call<Theoretics> addTheory (@Header("auth-token") String token, @Body Theoretics theoretics);
}
