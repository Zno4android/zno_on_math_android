package com.example.jatcool.zno_on_math.activity.user;

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

public class ChangePassword extends AppCompatActivity {

    EditText ApiEdit, newPasswordEdit;
    Button changeBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ApiEdit = findViewById(R.id.ApiKeyEdit);
        newPasswordEdit = findViewById(R.id.newPasswordEdit);
        changeBTN = findViewById(R.id.setNewPasswordButton);
        final JsonObject passwordObject = new JsonObject();
        changeBTN.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        passwordObject.addProperty("resetPasswordToken", ApiEdit.getText().toString());
                        passwordObject.addProperty("newPassword", newPasswordEdit.getText().toString());
                        NetworkService.getInstance()
                                .getJSONApi()
                                .resetPassword(passwordObject)
                                .enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(ChangePassword.this, "Ви успішно змінили пароль", Toast.LENGTH_LONG)
                                                    .show();
                                            finish();
                                        } else {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                Toast.makeText(ChangePassword.this, jsonObject.getString("message"), Toast.LENGTH_LONG)
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
                    }
                }
        );
    }
}
