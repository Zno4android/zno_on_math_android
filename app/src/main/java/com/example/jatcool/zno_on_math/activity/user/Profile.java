package com.example.jatcool.zno_on_math.activity.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {
    EditText Fname, Name, LastName;
    TextView group;
    Button save_btn, cancel_btn;
    ProgressBar pr;
    String token;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Профіль");
        Fname = findViewById(R.id.edFname);
        Name = findViewById(R.id.edName);
        LastName = findViewById(R.id.edLastName);
        group = findViewById(R.id.tvGroup);
        setActivityData(group, Fname, Name, LastName);
        pr = findViewById(R.id.Timer);
        save_btn = findViewById(R.id.pr_save_btn);
        cancel_btn = findViewById(R.id.pr_cancel_btn);
        cancel_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setActivityData(group, Fname, Name, LastName);

                    }
                }
        );

        save_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Fname.getText().toString().equals("") && !Name.getText().toString().equals("") && !LastName.getText().toString().equals("")) {
                            user = new User(Fname.getText().toString(), Name.getText().toString(), LastName.getText().toString());
                            Change(user);
                        } else
                            Toast.makeText(Profile.this, "Одне з полів порожнє", Toast.LENGTH_SHORT)
                                    .show();

                    }
                }
        );

    }



    public void setActivityData(TextView t, EditText... ed) {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        ed[0].setText(sharedPreferences.getString("Fname", ""));
        ed[1].setText(sharedPreferences.getString("FirstName", ""));
        ed[2].setText(sharedPreferences.getString("LastName", ""));
        t.setText(sharedPreferences.getString("Group", ""));
        token = sharedPreferences.getString("token", "");
    }


    public void Change(final User user) {
        pr.setVisibility(View.VISIBLE);
        NetworkService.getInstance()
                .getJSONApi()
                .Change(token, user)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Fname", user.getOt());
                            editor.putString("FirstName", user.getFirstname());
                            editor.putString("LastName", user.getLastname());
                            editor.commit();
                            Toast.makeText(Profile.this, "Данні успішно змінено", Toast.LENGTH_SHORT)
                                    .show();
                            pr.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        pr.setVisibility(View.GONE);
                    }
                });
//        if (userService.changeUser(token, user)) {
//            SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("Fname", user.getOt());
//            editor.putString("FirstName", user.getFirstname());
//            editor.putString("LastName", user.getLastname());
//            editor.commit();
//            Toast.makeText(Profile.this, "Данні успішно змінено", Toast.LENGTH_SHORT)
//                    .show();
//
//
//        }
//        pr.setVisibility(View.GONE);
    }
}
