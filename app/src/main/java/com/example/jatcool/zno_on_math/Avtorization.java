package com.example.jatcool.zno_on_math;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Avtorization extends AppCompatActivity {
EditText mEmail,mPassword;
Button add_btn;
User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avtorization);
        mEmail = (EditText)findViewById(R.id.avt_email);
        mPassword = (EditText)findViewById(R.id.avt_password);
        add_btn = (Button)findViewById(R.id.avt_btn);
        add_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NetworService.getInstance()
                                .getJSONApi()
                                .getUser(new User(mEmail.getText().toString(),mPassword.getText().toString()))
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {

                                        if(response.body()!=null) {
                                            user = response.body();

                                            Toast.makeText(Avtorization.this, user.getName(), Toast.LENGTH_LONG)
                                            .show();
                                        }
                                        else Toast.makeText(Avtorization.this,"Неверный логин или пароль",Toast.LENGTH_LONG)
                                        .show();

                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                      Toast.makeText(Avtorization.this,t.getMessage(),Toast.LENGTH_LONG)
                                              .show();
                                    }
                                });
                    }
                }
        );

    }

    public void reg(View view){
       startActivity(new Intent(this,Registration.class));
    }
}
