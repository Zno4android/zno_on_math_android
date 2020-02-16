package com.example.jatcool.zno_on_math.dao;

import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.entity.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDAOImpl implements UserDAO {
    User user;
    private String token;

    @Override
    public User getUserByToken(final String token) {
        final User[] user = new User[1];
//        NetworkService.getInstance()
//                .getJSONApi()
//                .getUserData(token)
//                .enqueue(new Callback<User>() {
//                    @Override
//                    public void onResponse(Call<User> call, Response<User> response) {
//                        user = response.body();
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable t) {
//
//                    }
//                });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<User> response = NetworkService.getInstance()
                            .getJSONApi()
                            .getUserData(token)
                            .execute();

                    if (response.body() != null) {
                        user[0] = response.body();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return user[0];
    }

    @Override
    public String getUserByEmailAndPassword(final String email, final String password, User outuser) {
        NetworkService.getInstance()
                .getJSONApi()
                .Log_in(new User(email, password))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() != null) {
                            token = response.body().getToken();
                            user = response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
        if (user != null) {
            outuser = user;
        }

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Response<User> response = NetworkService.getInstance()
//                            .getJSONApi()
//                            .Log_in(new User(email, password))
//                            .execute();
//
//                    if (response.body() != null) {
//                        token = response.body().getToken();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return token;
    }
}
