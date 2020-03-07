package com.example.jatcool.zno_on_math.dao;

import android.widget.ProgressBar;

import com.example.jatcool.zno_on_math.callback.getTokenByEmailCallback;
import com.example.jatcool.zno_on_math.callback.getUserByToken;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.entity.Group;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.util.MailCheck;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class UserDAOImpl implements UserDAO {
    private User user;
    private String token;
    private boolean userChange = false;
    private List<String> groups;
    private MailCheck isEmail;
    private boolean userAdded;
    private ProgressBar waiter;

    public ProgressBar getWaiter() {
        return waiter;
    }

    public void setWaiter(ProgressBar waiter) {
        this.waiter = waiter;
    }

    @Override
    public User getUserByToken(final String token, final ProgressBar waiter, User user) {
        NetworkService.getInstance()
                .getJSONApi()
                .getUserData(token)
                .enqueue(new getUserByToken(user, waiter));


        //        NetworkService.getInstance()
//                .getJSONApi()
//                .getUserData(token)
//                .enqueue(new Callback<User>() {
//                    @Override
//                    public void onResponse(Call<User> call, Response<User> response) {
//                        user = response.body();
//                        waiter.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable t) {
//                        waiter.setVisibility(View.GONE);
//                    }
//                });
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Response<User> response = NetworkService.getInstance()
//                            .getJSONApi()
//                            .getUserData(token)
//                            .execute();
//
//                    if (response.body() != null) {
//                        user = response.body();
//                    }
//
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


        return user;
    }

    @Override
    public String getTokenByEmailAndPassword(final String email, final String password, final ProgressBar waiter, String token) {


        NetworkService.getInstance()
                .getJSONApi()
                .getUserData(token)
                .enqueue(new getTokenByEmailCallback(token, waiter));

//        NetworkService.getInstance()
//                .getJSONApi()
//                .Log_in(new User(email, password))
//                .enqueue(new Callback<User>() {
//                    @Override
//                    public void onResponse(Call<User> call, Response<User> response) {
//                        if (response.body() != null) {
//                            token = response.body().getToken();
//                            //user = response.body();
//                        }
//                        waiter.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable t) {
//                        waiter.setVisibility(View.GONE);
//                    }
//                });

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

    @Override
    public boolean changeUser(final String token, final User user) {
        userChange = false;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<User> response = NetworkService.getInstance()
                            .getJSONApi()
                            .Change(token, user)
                            .execute();

                    if (response.isSuccessful()) {
                        userChange = true;
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

        return userChange;
    }

    @Override
    public List<String> getGroup() {
        groups = null;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Group> response = NetworkService.getInstance()
                            .getJSONApi()
                            .GetGroup()
                            .execute();

                    if (response.isSuccessful()) {
                        groups = response.body().getName();
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

        return groups;
    }

    @Override
    public MailCheck checkEmail(final String email) {
        isEmail = null;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<MailCheck> response = NetworkService.getInstance()
                            .getJSONApi()
                            .isEmailExist(email)
                            .execute();

                    if (response.isSuccessful()) {
                        isEmail = response.body();
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

        return isEmail;
    }

    @Override
    public boolean createUser(final User user) {
        userAdded = false;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<User> response = NetworkService.getInstance()
                            .getJSONApi()
                            .CreateUsers(user)
                            .execute();

                    if (response.isSuccessful()) {
                        userAdded = true;
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

        return userAdded;
    }
}
