package com.example.jatcool.zno_on_math;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Authorization extends AppCompatActivity {
EditText email,login,name,password,repassword;
Button add_users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        email = (EditText)findViewById(R.id.email);
        login = (EditText)findViewById(R.id.login);
        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        repassword = (EditText)findViewById(R.id.re_password);
        add_users = (Button)findViewById(R.id.add_users);
        add_users.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NetworService.getInstance()
                                .getJSONApi()
                                .CreateUsers(login.getText().toString(),name.getText().toString(),email.getText().toString(),password.getText().toString())
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        Toast.makeText(getApplicationContext(),"Успешно зарегестрировались",Toast.LENGTH_LONG)
                                                .show();
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(),"Ошибка!",Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });
                    }
                }
        );

    }
}
