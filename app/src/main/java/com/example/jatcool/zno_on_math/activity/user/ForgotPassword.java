package com.example.jatcool.zno_on_math.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    EditText mail;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mail = findViewById(R.id.changePasswordMailEdit);
        send = findViewById(R.id.toNewPasswordActivity);
        final JsonObject object = new JsonObject();
        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mail.getText().toString().equals("")) {
                            object.addProperty("email", mail.getText().toString());
                            NetworkService.getInstance()
                                    .getJSONApi()
                                    .forgotPassword(object)
                                    .enqueue(new Callback<JsonObject>() {
                                        @Override
                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(ForgotPassword.this, "Перевірте пошту", Toast.LENGTH_LONG)
                                                        .show();
                                                startActivity(new Intent(ForgotPassword.this, ChangePassword.class));
                                                finish();
                                            } else {
                                                try {
                                                    JSONObject message = new JSONObject(response.errorBody().string());
                                                    Toast.makeText(ForgotPassword.this, message.getString("message"), Toast.LENGTH_SHORT)
                                                            .show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JsonObject> call, Throwable t) {

                                        }
                                    });
                        } else {
                            Toast.makeText(ForgotPassword.this, "Пошта повинна бути заповнена", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
        );
    }
}
