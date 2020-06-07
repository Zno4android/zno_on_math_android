package com.example.jatcool.zno_on_math.activity.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.EMAIL;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.FATHERNAME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.FIRSTNAME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.GROUP;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.IMAGE;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.LASTNAME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STATUS;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;

public class Authorization extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button add_btn;
    User user;
    ProgressBar waiter;
    Context ctx;

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
            in.putExtra(IMAGE,sharedPreferences.getString(IMAGE,""));
            startActivity(in);
            finish();
        }

        mEmail = findViewById(R.id.avt_email);
        mPassword = findViewById(R.id.avt_password);
        add_btn = findViewById(R.id.avt_btn);
        waiter = findViewById(R.id.Connect);
        ctx = this;
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

    public void forgotPassword(View view) {
        startActivity(new Intent(this, ForgotPassword.class));
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
                        if(!user.getActivateAccount()){
                            AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                            alert.setTitle("Повідомлення");
                            alert.setMessage("Вам треба підтвердити пошту для авторизації");
                            alert.setPositiveButton("Добре",null);
                            alert.setCancelable(false);
                            alert.show();
                            return;
                        }
                        boolean isTeacher = user.getStatus() == Status.Teacher;
                        if (!user.isVerifyed() && isTeacher) {
                            Intent intent = new Intent(Authorization.this, NotAllowActivity.class);
                            intent.putExtra(TOKEN,token);
                            startActivity(intent);
                            finish();
                            return;
                        }
                        Intent in = authorization();
                        in.putExtra(FIRSTNAME, user.getFirstname());
                        in.putExtra(LASTNAME, user.getLastname());
                        in.putExtra(TOKEN, token);
                        in.putExtra(GROUP, user.getGroup());
                        in.putExtra(STATUS, user.getStatus().getName());
                        in.putExtra(EMAIL, user.getEmail());
                        in.putExtra(IMAGE,user.getImage());
                        waiter.setVisibility(View.INVISIBLE);
                        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(FIRSTNAME, user.getFirstname());
                        editor.putString(FATHERNAME, user.getFathername());
                        editor.putString(LASTNAME, user.getLastname());
                        editor.putString(TOKEN, token);
                        editor.putString(GROUP, user.getGroup());
                        editor.putString(STATUS, user.getStatus().getName());
                        editor.putString(EMAIL, user.getEmail());
                        editor.putString(IMAGE,user.getImage());
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

