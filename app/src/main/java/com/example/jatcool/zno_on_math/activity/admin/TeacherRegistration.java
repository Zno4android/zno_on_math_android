package com.example.jatcool.zno_on_math.activity.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.entity.Status;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.entity.builder.UserBean;
import com.example.jatcool.zno_on_math.util.MailCheck;
import com.example.jatcool.zno_on_math.util.Validation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.REGISTRATION_CAN_NOT_REGISTRATION;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.REGISTRATION_EMAIL_ALREADY_EXIST;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.REGISTRATION_INPUT_CORRECT_CONFIRM_PASSWORD;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.REGISTRATION_INPUT_CORRECT_EMAIL;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.REGISTRATION_INPUT_CORRECT_FATHERNAME;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.REGISTRATION_INPUT_CORRECT_FIRSTNAME;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.REGISTRATION_INPUT_CORRECT_PASSWORD;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.REGISTRATION_INPUT_CORRECT_SURNAME;
import static com.example.jatcool.zno_on_math.constants.SuccessMessageConstants.REGISTRATION_EMAIL_IS_FREE;
import static com.example.jatcool.zno_on_math.constants.SuccessMessageConstants.REGISTRATION_EMAIL_SUCCESS_REGISTRATION;

public class TeacherRegistration extends AppCompatActivity {
    EditText etEmail, etPassword, etRepassword, etFirstname, etLastname, etOt;
    Button addUsers;
    User user;
    ProgressBar emailChk;
    MailCheck isEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registration);
        isEmail = new MailCheck();
        etEmail = findViewById(R.id.email_teacher);
        etLastname = findViewById(R.id.lastname_teacher);
        etFirstname = findViewById(R.id.firstname_teacher);
        etOt = findViewById(R.id.ot_teacher);

        etPassword = findViewById(R.id.password_teacher);
        etRepassword = findViewById(R.id.re_password_teacher);
        addUsers = findViewById(R.id.add_teacher);
        emailChk = findViewById(R.id.login_chk_teacher);
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
                                Toast.makeText(TeacherRegistration.this, REGISTRATION_EMAIL_ALREADY_EXIST, Toast.LENGTH_LONG)
                                        .show();
                                emailChk.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(TeacherRegistration.this, REGISTRATION_EMAIL_IS_FREE, Toast.LENGTH_SHORT)
                                        .show();
                                emailChk.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<MailCheck> call, Throwable t) {
                            Toast.makeText(TeacherRegistration.this, t.getMessage(), Toast.LENGTH_LONG)
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

        UserBean userBean = new UserBean();

        userBean.setEmail(email);
        userBean.setPassword(password);
        userBean.setConfirmPassword(repassword);
        userBean.setLastname(lastname);
        userBean.setFirstname(firstname);
        userBean.setOt(ot);
        userBean.setStatus(Status.Teacher);

        if (isEmail.isEmail()) {
            user = userBean.createUser();
            NetworkService.getInstance()
                    .getJSONApi()
                    .CreateUsers(user)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Toast.makeText(getApplicationContext(), REGISTRATION_EMAIL_SUCCESS_REGISTRATION, Toast.LENGTH_LONG)
                                    .show();

                            finish();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), REGISTRATION_CAN_NOT_REGISTRATION, Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        } else
            Toast.makeText(TeacherRegistration.this, REGISTRATION_EMAIL_ALREADY_EXIST, Toast.LENGTH_LONG)
                    .show();
    }

    private boolean checkValidEmail(String str) {
        boolean flag = true;
        if (!Validation.isValidEmail(str)) {
            flag = false;
            Toast.makeText(TeacherRegistration.this, REGISTRATION_INPUT_CORRECT_EMAIL, Toast.LENGTH_LONG)
                    .show();
        }
        return flag;
    }

    private boolean validationData(UserBean userBean) {
        boolean flag = true;

        if (!userBean.validateEmail()) {
            flag = false;
            Toast.makeText(TeacherRegistration.this, REGISTRATION_INPUT_CORRECT_EMAIL, Toast.LENGTH_LONG)
                    .show();
        } else if (!userBean.validatePassword()) {
            flag = false;
            Toast.makeText(TeacherRegistration.this, REGISTRATION_INPUT_CORRECT_PASSWORD, Toast.LENGTH_LONG)
                    .show();
        } else if (!userBean.validateConfirmPassword()) {
            flag = false;
            Toast.makeText(TeacherRegistration.this, REGISTRATION_INPUT_CORRECT_CONFIRM_PASSWORD, Toast.LENGTH_LONG)
                    .show();
        } else if (!userBean.validateFirstname()) {
            flag = false;
            Toast.makeText(TeacherRegistration.this, REGISTRATION_INPUT_CORRECT_FIRSTNAME, Toast.LENGTH_LONG)
                    .show();
        } else if (!userBean.validateLastname()) {
            flag = false;
            Toast.makeText(TeacherRegistration.this, REGISTRATION_INPUT_CORRECT_SURNAME, Toast.LENGTH_LONG)
                    .show();
        } else if (!userBean.validateOt()) {
            flag = false;
            Toast.makeText(TeacherRegistration.this, REGISTRATION_INPUT_CORRECT_FATHERNAME, Toast.LENGTH_LONG)
                    .show();
        }

        return flag;
    }
}
