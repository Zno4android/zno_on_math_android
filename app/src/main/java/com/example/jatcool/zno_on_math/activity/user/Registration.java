package com.example.jatcool.zno_on_math.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jatcool.zno_on_math.connection.NetworService;
import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.util.Validation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {
    //возможно групу лучше сделать combobox, тогда нужно еще одну таблицу добавить длля групп
    //ошибки из-за нехватки компонентов, добавь их на форму
    //валидация исправлена в соответствии
    //а на отчество вообще нужна проверка, думаю да для украинцев делаем
    EditText email,login,password,repassword,firstname,lastname,group;
    Button add_users;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email = (EditText)findViewById(R.id.email);
        lastname = (EditText)findViewById(R.id.lastname);
        firstname = (EditText)findViewById(R.id.firstname);
        ot = (EditText)findViewById(R.id.ot);
        group = (EditText)findViewById(R.id.group);
        password = (EditText)findViewById(R.id.password);
        repassword = (EditText)findViewById(R.id.re_password);
        add_users = (Button)findViewById(R.id.add_users);
        email.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(!b){
                            if(Validation.isExistEmail(email.getText().toString())){

                            }
                        }

                    }
                }
        );
        add_users.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkValdationData(email.getText().toString(),password.getText().toString(),repassword.getText().toString(),
                                lastname.getText().toString(), firstname.getText().toString(),ot.getText().toString()){
                            user =  new User(email.getText().toString(),password.getText().toString(),group.getText().toString(),
                                    lastname.getText().toString(), firstname.getText().toString(),ot.getText().toString());
                            NetworService.getInstance()
                                    .getJSONApi()
                                    .CreateUsers(user)
                                    .enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {

                                            finish();
                                        }

                                        @Override
                                        public void onFailure(Call<User> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(),"Ошибка!",Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                        }

                    }
                }
        );


    }
    private boolean checkValdationData(String email,String password,String rePassword,String lastname,String firstname,String ot){
        boolean flag=true;
        if(!Validation.isValidEmail(email)){
            flag=false;
            Toast.makeText(Registration.this,"Введіть коректний email",Toast.LENGTH_LONG)
                    .show();
            //show message incorrect email
        }else if(!Validation.isValidPasswor(password)){
            flag=false;
            Toast.makeText(Registration.this,"Введіть коректний пароль",Toast.LENGTH_LONG)
                    .show();
            //show message incorrect password
        }else if(!Validation.isEqualsPassword(password,rePassword)){
            flag=false;
            Toast.makeText(Registration.this,"Паролі не сбігаються",Toast.LENGTH_LONG)
                    .show();
            //show message password dont equals
        }else if(!Validation.isValidName(lastname)){
            flag=false;
            Toast.makeText(Registration.this,"Введіть коректне ім'я",Toast.LENGTH_LONG)
                    .show();
            //show message incorrect name
        }else if(!Validation.isValidName(firstname)){
            flag=false;
            Toast.makeText(Registration.this,"Введіть коректне ім'я",Toast.LENGTH_LONG)
                    .show();
            //show message incorrect name
        }else if(!Validation.isValidName(ot)){
            flag=false;
            Toast.makeText(Registration.this,"Введіть коректне ім'я",Toast.LENGTH_LONG)
                    .show();
            //show message incorrect name
        }
        return flag;
    }


}
