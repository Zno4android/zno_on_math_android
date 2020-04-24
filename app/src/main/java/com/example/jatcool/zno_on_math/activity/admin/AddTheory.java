package com.example.jatcool.zno_on_math.activity.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Theoretics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.ADD_THEORY_CAN_NOT_ADD_THEORY;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.ADD_THEORY_NOT_FILL_FIELD;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.THEME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;
import static com.example.jatcool.zno_on_math.constants.SuccessMessageConstants.ADD_THEORY_SUCCESS_ADD_THEORY;

public class AddTheory extends AppCompatActivity {

    Button btnAddTheory;
    EditText etTheoryName;
    EditText etTheoryText;
    TextView tvThemeTheory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theory);

        btnAddTheory = findViewById(R.id.add_theory);
        etTheoryName = findViewById(R.id.et_name_theory);
        etTheoryText = findViewById(R.id.text_theory);
        tvThemeTheory = findViewById(R.id.theme_theory);

        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        final String token = sharedPreferences.getString(TOKEN, "");
        Bundle values = getIntent().getExtras();
        final String theme = values.getString(THEME);

        tvThemeTheory.setText(theme);

        btnAddTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etTheoryName.getText().toString();
                String text = etTheoryText.getText().toString();

                if (name.isEmpty() || text.isEmpty()) {
                    Toast.makeText(AddTheory.this, ADD_THEORY_NOT_FILL_FIELD, Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                Theoretics theoretics = new Theoretics();
                theoretics.setText(text);
                theoretics.setName(name);
                theoretics.setTheme(theme);

                NetworkService.getInstance()
                        .getJSONApi()
                        .addTheory(token, theoretics)
                        .enqueue(new Callback<Theoretics>() {
                            @Override
                            public void onResponse(Call<Theoretics> call, Response<Theoretics> response) {
                                Toast.makeText(AddTheory.this, ADD_THEORY_SUCCESS_ADD_THEORY, Toast.LENGTH_LONG)
                                        .show();
                            }

                            @Override
                            public void onFailure(Call<Theoretics> call, Throwable t) {
                                Toast.makeText(AddTheory.this, ADD_THEORY_CAN_NOT_ADD_THEORY, Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            }
        });
    }
}
