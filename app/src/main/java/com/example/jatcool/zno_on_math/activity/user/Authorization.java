package com.example.jatcool.zno_on_math.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.User;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Authorization extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button add_btn;
    User user;
    ProgressBar waiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avtorization);

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

        mEmail = findViewById(R.id.avt_email);
        mPassword = findViewById(R.id.avt_password);
        add_btn = findViewById(R.id.avt_btn);
        waiter = findViewById(R.id.Connect);
        add_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        waiter.setVisibility(View.VISIBLE);
                        User user = new User();
                        user.setEmail(mEmail.getText().toString());
                        user.setPassword(mPassword.getText().toString());

                        NetworkService.getInstance()
                                .getJSONApi()
                                .Log_in(user)
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {

                                        if (response.body() != null) {
                                            user_by_token(response.body().getToken());
                                        } else
                                            Toast.makeText(Authorization.this, "Неверный логин или пароль", Toast.LENGTH_LONG)
                                                    .show();
                                        waiter.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toast.makeText(Authorization.this, t.getMessage(), Toast.LENGTH_LONG)
                                                .show();
                                        waiter.setVisibility(View.GONE);
                                    }
                                });
                    }
                });
    }

    public Intent authorization(View view) {
        return new Intent(this, Zno.class);

    }

    public void reg(View view) {
        startActivity(new Intent(this, Registration.class));
    }

    public void user_by_token(final String token) {
        NetworkService.getInstance()
                .getJSONApi()
                .getUserData(token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        user = response.body();
                        Intent in = authorization(add_btn);
                        in.putExtra("FirstName", user.getFirstname());
                        in.putExtra("LastName", user.getLastname());
                        in.putExtra("token", token);
                        in.putExtra("Group", user.getGroup());
                        waiter.setVisibility(View.INVISIBLE);
                        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("FirstName", user.getFirstname());
                        editor.putString("Fname", user.getFathername());
                        editor.putString("LastName", user.getLastname());
                        editor.putString("token", token);
                        editor.putString("Group", user.getGroup());
                        editor.apply();
                        editor.commit();
                        startActivity(in);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
    }
}
