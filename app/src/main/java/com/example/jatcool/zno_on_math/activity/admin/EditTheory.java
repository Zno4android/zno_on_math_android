package com.example.jatcool.zno_on_math.activity.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.example.jatcool.zno_on_math.entity.Theoretics;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.THEORY;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;

public class EditTheory extends AppCompatActivity {

    Spinner themeSpinner;
    EditText editTheoryEdit, theoryTextEdit;
    Button editBtn;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_theory);
        Bundle ar = getIntent().getExtras();
        String js = ar.getString(THEORY);
        SharedPreferences s = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml",""),MODE_PRIVATE);
        token = s.getString(TOKEN, "");
        final Theoretics theory = new Gson().fromJson(js, Theoretics.class);
        editTheoryEdit = findViewById(R.id.editTheoryEdit);
        theoryTextEdit = findViewById(R.id.theoryTextEdit);
        editBtn = findViewById(R.id.editTheoryButton);
        themeSpinner = findViewById(R.id.editTheorySpiner);
        editTheoryEdit.setText(theory.getName());
        theoryTextEdit.setText(theory.getText());

        NetworkService.getInstance()
                .getJSONApi()
                .getAllTheme(token)
                .enqueue(
                        new Callback<List<Theme>>() {
                            @Override
                            public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
                                if(response.isSuccessful()){
                                    ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,response.body());
                                    themeSpinner.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Theme>> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Щось пішло не так!", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                );


        editBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!editTheoryEdit.getText().toString().equals("") && !theoryTextEdit.getText().toString().equals("")){
                            theory.setText(theoryTextEdit.getText().toString());
                            theory.setName(editTheoryEdit.getText().toString());
                            theory.setTheme(themeSpinner.getSelectedItem().toString());
                            NetworkService.getInstance()
                                    .getJSONApi()
                                    .updateTheory(token,theory.getId(),theory)
                                    .enqueue(
                                            new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    if(response.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(), "Теорію успішно змінено", Toast.LENGTH_SHORT)
                                                                .show();
                                                        finish();
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(), "Щось пішло не так!", Toast.LENGTH_SHORT)
                                                                .show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<String> call, Throwable t) {
                                                    Toast.makeText(getApplicationContext(), "Щось пішло не так!", Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            }
                                    );
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Одне з полів порожне",Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
        );
    }
}
