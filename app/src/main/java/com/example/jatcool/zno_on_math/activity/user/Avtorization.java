package com.example.jatcool.zno_on_math.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jatcool.zno_on_math.connection.NetworService;
import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Avtorization extends AppCompatActivity {
EditText mEmail,mPassword;
Button add_btn;
User user;
ProgressBar waiter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avtorization);
        mEmail = (EditText)findViewById(R.id.avt_email);
        mPassword = (EditText)findViewById(R.id.avt_password);
        add_btn = (Button)findViewById(R.id.avt_btn);
        waiter = (ProgressBar)findViewById(R.id.Connect);
        add_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        waiter.setVisibility(View.VISIBLE);
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
                                            waiter.setVisibility(View.INVISIBLE);
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
       startActivity(new Intent(this, Registration.class));
    }
}
