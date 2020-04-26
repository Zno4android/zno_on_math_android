package com.example.jatcool.zno_on_math.connection;

import com.example.jatcool.zno_on_math.entity.Group;
import com.example.jatcool.zno_on_math.entity.Statistics;
import com.example.jatcool.zno_on_math.entity.Test;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.example.jatcool.zno_on_math.entity.Theoretics;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.entity.dbEntity.DBResultQuestion;
import com.example.jatcool.zno_on_math.entity.wrapper.StatisticsWrapper;
import com.example.jatcool.zno_on_math.entity.wrapper.TestWrapper;
import com.example.jatcool.zno_on_math.util.MailCheck;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
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

    @POST("/api/statistics/saveStatistics")
    Call<StatisticsWrapper> saveStatistics(@Header("auth-token") String token, @Body StatisticsWrapper statistics);

    @POST("/api/statistics/questionStatistics")
    Call<DBResultQuestion> updateResultQuestion(@Body DBResultQuestion resultQuestions);

    @GET("/api/tests/getTest")
    Call<TestWrapper> getTest(@Header("auth-token") String token, @Query("_id") String testId);

    @POST("/api/tests/createTest")
    Call<Test> createTest(@Header("auth-token") String token, @Body TestWrapper testWrapper);

    @GET("api/theory")
    Call<List<Theoretics>> getTheory(@Header("auth-token") String token, @Query("theme") String theme);

    @PUT("/api/theory/update")
    Call<Theoretics> editTheory(@Header("auth-token") String token, @Body Theoretics theoretics);

    @POST("/api/theory/add")
    Call<Theoretics> addTheory(@Header("auth-token") String token, @Body Theoretics theoretics);

    @GET("/api/students/getAllStudents")
    Call<List<User>> getAllStudents(@Header("auth-token") String token, @Query("search") String search, @Query("group") String group);

    @GET("/api/statistics/getStudentStatistic/{id}")
    Call<List<Statistics>> getStudentStatistic(@Header("auth-token") String token, @Path("id") String id);

    @GET("/api/statistics/getStudentStatistic/{id}")
    Call<List<Statistics>> getStudentStatisticByTestId(@Header("auth-token") String token, @Path("id") String id);

    @GET("/api/statistics/getMyStatistic")
    Call<List<Statistics>> getMyStatistic(@Header("auth-token") String token);

    @GET("/api/themes/getAllThemes")
    Call<List<Theme>> getAllTheme(@Header("auth-token") String token);

    @POST("api/themes/addTheme")
    Call<String> addTheme(@Header("auth-token") String token, @Body Theme theme);

    @GET("api/tests/getAllTest")
    Call<List<Test>> getAllTest(@Header("auth-token") String token);

    @DELETE("/api/tests/deleteTestById/{id}")
    Call<String> deleteTest(@Header("auth-token") String token, @Path("id") String id);

    @PUT("/api/tests/updateTestById/{id}")
    Call<String> updateTest(@Header("auth-token") String token, @Path("id") String id, @Body Test test);

    @DELETE("api/theory/delete/{id}")
    Call<String> deleteTheory(@Header("auth-token") String token, @Path("id") String id);

    @POST("/api/tests/checkOwner/{id}")
    Call<String> checkTestOwner(@Header("auth-token") String token, @Path("id") String id);

    @PUT("api/theory/update/{id}")
    Call<String> updateTheory(@Header("auth-token") String token, @Path("id") String id, @Body Theoretics theoretics);
}
