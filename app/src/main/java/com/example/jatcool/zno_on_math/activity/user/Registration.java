package com.example.jatcool.zno_on_math.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jatcool.zno_on_math.connection.NetworService;
import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.Group;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.util.MailCheck;
import com.example.jatcool.zno_on_math.util.Validation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {
    //возможно групу лучше сделать combobox, тогда нужно еще одну таблицу добавить длля групп
    //ошибки из-за нехватки компонентов, добавь их на форму
    //валидация исправлена в соответствии
    //а на отчество вообще нужна проверка, думаю да для украинцев делаем
    EditText email, login, password, repassword, firstname, lastname, ot;
    Button add_users;
    Spinner group;
    User user;
    ProgressBar email_chk;
    MailCheck isEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        group = (Spinner) findViewById(R.id.group);
        getGroup();
        isEmail = new MailCheck();
        email = (EditText) findViewById(R.id.email);
        lastname = (EditText) findViewById(R.id.lastname);
        firstname = (EditText) findViewById(R.id.firstname);
        ot = (EditText) findViewById(R.id.ot);

        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.re_password);
        add_users = (Button) findViewById(R.id.add_users);
        email_chk = (ProgressBar) findViewById(R.id.login_chk);
        email.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            checkEmail();
                        }

                    }
                }
        );
        add_users.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newUer();
                    }
                }
        );


    }

    private void getGroup() {
        NetworService.getInstance()
                .getJSONApi()
                .GetGroup()
                .enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_item, response.body().getName());
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        group.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {

                    }
                });
    }

    private void checkEmail() {
        if (checkValidEmail(email.getText().toString())) {
            email_chk.setVisibility(View.VISIBLE);
            NetworService.getInstance()
                    .getJSONApi()
                    .isEmailExist(email.getText().toString())
                    .enqueue(new Callback<MailCheck>() {
                        @Override
                        public void onResponse(Call<MailCheck> call, Response<MailCheck> response) {
                            isEmail = response.body();
                            if (!isEmail.isEmail()) {
                                Toast.makeText(Registration.this, "Користувач з такою поштою вже існує", Toast.LENGTH_LONG)
                                        .show();
                                email_chk.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(Registration.this, "Пошта вільна", Toast.LENGTH_SHORT)
                                        .show();
                                email_chk.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<MailCheck> call, Throwable t) {
                            Toast.makeText(Registration.this, t.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                            Log.d("Error", t.getMessage());
                            email_chk.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void newUer() {
        if (checkValdationData(email.getText().toString(), password.getText().toString(), repassword.getText().toString(),
                lastname.getText().toString(), firstname.getText().toString(), ot.getText().toString())) {
            if (isEmail.isEmail()) {
                user = new User(email.getText().toString(), password.getText().toString(), group.getSelectedItem().toString(),
                        lastname.getText().toString(), firstname.getText().toString(), ot.getText().toString());
                NetworService.getInstance()
                        .getJSONApi()
                        .CreateUsers(user)
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                Toast.makeText(getApplicationContext(), "Ви успішно зареєструвались!", Toast.LENGTH_LONG)
                                        .show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Ошибка!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            } else
                Toast.makeText(Registration.this, "Користувач з такою поштою вже існує", Toast.LENGTH_LONG)
                        .show();
        }


    }

    private boolean checkValidEmail(String str) {
        boolean flag = true;
        if (!Validation.isValidEmail(str)) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректний email", Toast.LENGTH_LONG)
                    .show();
        }
        return flag;
    }

    private boolean checkValdationData(String email, String password, String rePassword, String lastname, String firstname, String ot) {
        boolean flag = true;
        if (!Validation.isValidEmail(email)) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректний email", Toast.LENGTH_LONG)
                    .show();
            //show message incorrect email
        } else if (!Validation.isValidPassword(password)) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректний пароль", Toast.LENGTH_LONG)
                    .show();
            //show message incorrect password
        } else if (!Validation.isEqualsPassword(password, rePassword)) {
            flag = false;
            Toast.makeText(Registration.this, "Паролі не сбігаються", Toast.LENGTH_LONG)
                    .show();
            //show message password dont equals
        } else if (!Validation.isValidName(lastname)) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректне ім'я", Toast.LENGTH_LONG)
                    .show();
            //show message incorrect name
        } else if (!Validation.isValidName(firstname)) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректне ім'я", Toast.LENGTH_LONG)
                    .show();
            //show message incorrect name
        } else if (!Validation.isValidName(ot)) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректне ім'я", Toast.LENGTH_LONG)
                    .show();
            //show message incorrect name
        }
        return flag;
    }


}
