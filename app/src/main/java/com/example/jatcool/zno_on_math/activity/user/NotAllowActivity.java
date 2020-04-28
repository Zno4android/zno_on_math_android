package com.example.jatcool.zno_on_math.activity.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotAllowActivity extends AppCompatActivity {

    Button allow;
    AlertDialog.Builder alert;
    String token;
    Boolean isSend = false;
    AlertDialog waiting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_allow);
        allow = findViewById(R.id.acces_button_teacher);
        alert = new AlertDialog.Builder(this);
        alert.setTitle("Запит");
        alert.setMessage("Запит відправляется");
        alert.setCancelable(false);
        Bundle s = getIntent().getExtras();
        token = s.getString(SharedPreferencesConstants.TOKEN,"");
        allow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isSend) {
                           waiting = alert.create();
                            NetworkService.getInstance()
                                    .getJSONApi()
                                    .verifyTeacher(token)
                                    .enqueue(
                                            new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    if (response.isSuccessful()) {
                                                        alert.setMessage("Запит успішно відправлено. Чекайте на підтверждення");
                                                        alert.setPositiveButton("Ок", null);
                                                        waiting.dismiss();
                                                        waiting = alert.create();
                                                        waiting.show();
                                                        return;
                                                    }
                                                    alert.setMessage("Щось пішло не так");
                                                    alert.setPositiveButton("Ок", null);
                                                }

                                                @Override
                                                public void onFailure(Call<String> call, Throwable t) {
                                                    alert.setMessage("Щось пішло не так");
                                                    alert.setPositiveButton("Ок", null);
                                                }
                                            }
                                    );
                        }
                        else {
                            alert.setMessage("Ви вже відправили запит!");
                            alert.setPositiveButton("Ок", null);
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Authorization.class));
        finish();
    }
}
