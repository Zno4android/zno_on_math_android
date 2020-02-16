package com.example.jatcool.zno_on_math.dao;

import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.entity.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDAOImpl implements UserDAO {
    private User user;
    private String token;

    @Override
    public User getUserByToken(final String token) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkService.getInstance()
                        .getJSONApi()
                        .getUserData(token)
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                user = response.body();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public String getUserByEmailAndPassword(String email, String password) {
        NetworkService.getInstance()
                .getJSONApi()
                .Log_in(new User(email, password))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() != null) {
                            token = response.body().getToken();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
        return token;
    }
}
