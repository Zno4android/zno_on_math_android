package com.example.jatcool.zno_on_math.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.dao.UserDAO;
import com.example.jatcool.zno_on_math.dao.UserDAOImpl;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.service.UserService;
import com.example.jatcool.zno_on_math.service.UserServiceImpl;

import java.io.File;

public class Authorization extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button add_btn;
    User user;
    ProgressBar waiter;

    UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avtorization);

        initService();

        Log.d("PathFile", ConstFile.SHARED_PREFENCES_START_PATH + ConstFile.FILE_NAME);
        if (new File(ConstFile.SHARED_PREFENCES_START_PATH + ConstFile.FILE_NAME).exists()) {

            Intent in = authorization(add_btn);
            SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
            in.putExtra("FirstName", sharedPreferences.getString("FirstName", ""));
            in.putExtra("LastName", sharedPreferences.getString("LastName", ""));
            in.putExtra("token", sharedPreferences.getString("token", ""));
            in.putExtra("Group", sharedPreferences.getString("Group", ""));
            startActivity(in);
            finish();

        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mEmail = findViewById(R.id.avt_email);
        mPassword = findViewById(R.id.avt_password);
        add_btn = findViewById(R.id.avt_btn);
        waiter = findViewById(R.id.Connect);
        add_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        waiter.setVisibility(View.VISIBLE);
//                        NetworkService.getInstance()
//                                .getJSONApi()
//                                .Log_in(new User(mEmail.getText().toString(), mPassword.getText().toString()))
//                                .enqueue(new Callback<User>() {
//                                    @Override
//                                    public void onResponse(Call<User> call, Response<User> response) {
//
//                                        if (response.body() != null) {
//                                            user_by_token(response.body().getToken());
//                                        } else
//                                            Toast.makeText(Authorization.this, "Неверный логин или пароль", Toast.LENGTH_LONG)
//                                                    .show();
//                                        waiter.setVisibility(View.GONE);
//
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<User> call, Throwable t) {
//                                        Toast.makeText(Authorization.this, t.getMessage(), Toast.LENGTH_LONG)
//                                                .show();
//                                        waiter.setVisibility(View.GONE);
//                                    }
//                                });
                        String token = userService.getUserByEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString());
                        if (token != null) {
                            user_by_token(token);
                        } else {
                            Toast.makeText(Authorization.this, "Неверный логин или пароль", Toast.LENGTH_LONG)
                                    .show();
                        }
                        waiter.setVisibility(View.GONE);
                    }
                }
        );

    }

    private void initService() {
        UserDAO userDAO = new UserDAOImpl();
        userService = new UserServiceImpl(userDAO);
    }

    public Intent authorization(View view) {
        return new Intent(this, Zno.class);

    }

    public void reg(View view) {
        startActivity(new Intent(this, Registration.class));
    }

    public void user_by_token(String token) {
//        NetworkService.getInstance()
//                .getJSONApi()
//                .getUserData(token)
//                .enqueue(new Callback<User>() {
//                    @Override
//                    public void onResponse(Call<User> call, Response<User> response) {
//                        user = response.body();
//                        Intent in = authorization(add_btn);
//                        in.putExtra("FirstName", user.getFirstname());
//                        in.putExtra("LastName", user.getLastname());
//                        in.putExtra("token", user.getToken());
//                        in.putExtra("Group", user.getGroup());
//                        waiter.setVisibility(View.INVISIBLE);
//                        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("FirstName", user.getFirstname());
//                        editor.putString("Fname",user.getOt());
//                        editor.putString("LastName", user.getLastname());
//                        editor.putString("token", user.getToken());
//                        editor.putString("Group", user.getGroup());
//                        editor.apply();
//                        editor.commit();
//                        startActivity(in);
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable t) {
//
//                    }
//                });
        user = userService.getUserByToken(token);
        Intent in = authorization(add_btn);
        in.putExtra("FirstName", user.getFirstname());
        in.putExtra("LastName", user.getLastname());
        in.putExtra("token", user.getToken());
        in.putExtra("Group", user.getGroup());
        waiter.setVisibility(View.INVISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FirstName", user.getFirstname());
        editor.putString("Fname", user.getOt());
        editor.putString("LastName", user.getLastname());
        editor.putString("token", user.getToken());
        editor.putString("Group", user.getGroup());
        editor.apply();
        editor.commit();
        startActivity(in);
    }
}
