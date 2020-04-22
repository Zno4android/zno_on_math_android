package com.example.jatcool.zno_on_math.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Theme;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_theme extends AppCompatActivity {
EditText editText;
Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theme);
        editText = findViewById(R.id.theoryName_AddTheory);
        btn = findViewById(R.id.add_theme_button);
        final SharedPreferences pr = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml",""),MODE_PRIVATE);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!editText.getText().toString().equals("")){
                            NetworkService.getInstance()
                                    .getJSONApi()
                                    .addTheme(pr.getString("token",""), new Theme(editText.getText().toString()))
                                    .enqueue(
                                            new Callback<List<Theme>>() {
                                                @Override
                                                public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
                                                    if(response.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(),"Тему успішно доданно", Toast.LENGTH_LONG)
                                                                .show();
                                                        finish();
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(),"Щось пішло не так", Toast.LENGTH_LONG)
                                                                .show();
                                                        finish();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<List<Theme>> call, Throwable t) {
                                                         t.printStackTrace();
                                                }
                                            }
                                    );
                        }
                    }
                }
        );
    }
}
