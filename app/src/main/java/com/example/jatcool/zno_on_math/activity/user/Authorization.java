package com.example.jatcool.zno_on_math.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.activity.admin.TeacherRegistration;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Status;
import com.example.jatcool.zno_on_math.entity.User;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.AUTHORIZATION_INCORRECT_INPUT;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.FATHERNAME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.FIRSTNAME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.GROUP;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.LASTNAME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STATUS;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;

public class Authorization extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button add_btn;
    User user;
    ProgressBar waiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avtorization);
        if (new File(ConstFile.SHARED_PREFENCES_START_PATH + ConstFile.FILE_NAME).exists()) {
            SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
            Intent in = authorization();
            in.putExtra(FIRSTNAME, sharedPreferences.getString(FIRSTNAME, ""));
            in.putExtra(LASTNAME, sharedPreferences.getString(LASTNAME, ""));
            in.putExtra(TOKEN, sharedPreferences.getString(TOKEN, ""));
            in.putExtra(GROUP, sharedPreferences.getString(GROUP, ""));
            in.putExtra(STATUS, sharedPreferences.getString(STATUS, ""));
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
                                            Toast.makeText(Authorization.this, AUTHORIZATION_INCORRECT_INPUT, Toast.LENGTH_LONG)
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

    public Intent authorization() {
        return new Intent(this, Zno.class);

    }

    public void reg(View view) {
        startActivity(new Intent(this, Registration.class));
    }

    public void red_teacher(View view) {
        startActivity(new Intent(this, TeacherRegistration.class));
    }

    public void user_by_token(final String token) {
        NetworkService.getInstance()
                .getJSONApi()
                .getUserData(token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        user = response.body();
                        boolean isTeacher = user.getStatus() == Status.Teacher;
                        if(!user.isVerifyed() && isTeacher){
                            startActivity(new Intent(Authorization.this, NotAllowActivity.class));
                            finish();
                            return;
                        }
                        Intent in = authorization();
                        in.putExtra(FIRSTNAME, user.getFirstname());
                        in.putExtra(LASTNAME, user.getLastname());
                        in.putExtra(TOKEN, token);
                        in.putExtra(GROUP, user.getGroup());
                        in.putExtra(STATUS, user.getStatus().getName());
                        waiter.setVisibility(View.INVISIBLE);
                        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(FIRSTNAME, user.getFirstname());
                        editor.putString(FATHERNAME, user.getFathername());
                        editor.putString(LASTNAME, user.getLastname());
                        editor.putString(TOKEN, token);
                        editor.putString(GROUP, user.getGroup());
                        editor.putString(STATUS, user.getStatus().getName());
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

