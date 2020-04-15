package com.example.jatcool.zno_on_math.activity.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.entity.Group;
import com.example.jatcool.zno_on_math.entity.Status;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.entity.builder.UserBean;
import com.example.jatcool.zno_on_math.util.MailCheck;
import com.example.jatcool.zno_on_math.util.Validation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {
    EditText etEmail, etPassword, etRepassword, etFirstname, etLastname, etOt;
    Button addUsers;
    Spinner group;
    User user;
    ProgressBar emailChk;
    MailCheck isEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        group = findViewById(R.id.group);
        getGroup();
        isEmail = new MailCheck();
        etEmail = findViewById(R.id.email);
        etLastname = findViewById(R.id.lastname);
        etFirstname = findViewById(R.id.firstname);
        etOt = findViewById(R.id.ot);

        etPassword = findViewById(R.id.password);
        etRepassword = findViewById(R.id.re_password);
        addUsers = findViewById(R.id.add_users);
        emailChk = findViewById(R.id.login_chk);
        etEmail.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            checkEmail();
                        }

                    }
                }
        );
        addUsers.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newUser();
                    }
                }
        );
    }

    private void getGroup() {
        NetworkService.getInstance()
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
        if (checkValidEmail(etEmail.getText().toString())) {
            emailChk.setVisibility(View.VISIBLE);
            NetworkService.getInstance()
                    .getJSONApi()
                    .isEmailExist(etEmail.getText().toString())
                    .enqueue(new Callback<MailCheck>() {
                        @Override
                        public void onResponse(Call<MailCheck> call, Response<MailCheck> response) {
                            isEmail = response.body();
                            if (!isEmail.isEmail()) {
                                Toast.makeText(Registration.this, "Користувач з такою поштою вже існує", Toast.LENGTH_LONG)
                                        .show();
                                emailChk.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(Registration.this, "Пошта вільна", Toast.LENGTH_SHORT)
                                        .show();
                                emailChk.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<MailCheck> call, Throwable t) {
                            Toast.makeText(Registration.this, t.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                            Log.d("Error", t.getMessage());
                            emailChk.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void newUser() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String repassword = etRepassword.getText().toString();
        String lastname = etLastname.getText().toString();
        String firstname = etFirstname.getText().toString();
        String ot = etOt.getText().toString();
        String groups = group.getSelectedItem().toString();

        UserBean userBean = new UserBean();

        userBean.setEmail(email);
        userBean.setPassword(password);
        userBean.setGroup(groups);
        userBean.setConfirmPassword(repassword);
        userBean.setLastname(lastname);
        userBean.setFirstname(firstname);
        userBean.setOt(ot);
        userBean.setStatus(Status.Student);

        if (isEmail.isEmail()) {
            user = userBean.createUser();
            NetworkService.getInstance()
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

    private boolean checkValidEmail(String str) {
        boolean flag = true;
        if (!Validation.isValidEmail(str)) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректний email", Toast.LENGTH_LONG)
                    .show();
        }
        return flag;
    }

    private boolean validationData(UserBean userBean) {
        boolean flag = true;

        if (!userBean.validateEmail()) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректний email", Toast.LENGTH_LONG)
                    .show();
        } else if (!userBean.validatePassword()) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректний пароль", Toast.LENGTH_LONG)
                    .show();
        } else if (!userBean.validateConfirmPassword()) {
            flag = false;
            Toast.makeText(Registration.this, "Паролі не сбігаються", Toast.LENGTH_LONG)
                    .show();
        } else if (!userBean.validateFirstname()) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректне ім'я", Toast.LENGTH_LONG)
                    .show();
        } else if (!userBean.validateLastname()) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректне прізвище", Toast.LENGTH_LONG)
                    .show();
        } else if (!userBean.validateOt()) {
            flag = false;
            Toast.makeText(Registration.this, "Введіть коректне ім'я по батькові", Toast.LENGTH_LONG)
                    .show();
        }

        return flag;
    }


}
