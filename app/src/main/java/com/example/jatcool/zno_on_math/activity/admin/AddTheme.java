package com.example.jatcool.zno_on_math.activity.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.ADD_THEME_CAN_NOT_GET_THEMES;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;
import static com.example.jatcool.zno_on_math.constants.SuccessMessageConstants.ADD_THEME_SUCCESS_ADD_THEME;

public class AddTheme extends AppCompatActivity {
    EditText editText;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theme);
        editText = findViewById(R.id.theoryName_AddTheory);
        btn = findViewById(R.id.add_theme_button);
        final SharedPreferences pr = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editText.getText().toString().equals("")) {
                            NetworkService.getInstance()
                                    .getJSONApi()
                                    .addTheme(pr.getString(TOKEN, ""), new Theme(editText.getText().toString()))
                                    .enqueue(
                                            new Callback<JsonObject>() {
                                                @Override
                                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                    Toast.makeText(getApplicationContext(), ADD_THEME_SUCCESS_ADD_THEME, Toast.LENGTH_LONG)
                                                            .show();
                                                    finish();
                                                }

                                                @Override
                                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                                    Toast.makeText(getApplicationContext(), ADD_THEME_CAN_NOT_GET_THEMES, Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            }
                                    );
                        }
                    }
                }
        );

    }
}
