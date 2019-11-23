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
    EditText email,login,name,password,repassword;
    Button add_users;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        repassword = (EditText)findViewById(R.id.re_password);
        add_users = (Button)findViewById(R.id.add_users);
        email.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(!b){
                            if(Validation.isExistEmail(user.getEmail())){

                            }
                        }

                    }
                }
        );
        add_users.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkValdationData(name.getText().toString(),email.getText().toString(),password.getText().toString(),
                                repassword.getText().toString())){
                            user =  new User(name.getText().toString(),email.getText().toString(),password.getText().toString());
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
    private boolean checkValdationData(String email,String name,String password,String rePassword){
        boolean flag=true;
        if(!Validation.isValidEmail(email)){
            flag=false;
            Toast.makeText(Registration.this,"Введіть коректний email",Toast.LENGTH_LONG)
                    .show();
            //show message incorrect email
        }else if(!Validation.isValidName(name)){
            flag=false;
            Toast.makeText(Registration.this,"Введіть коректне ім'я",Toast.LENGTH_LONG)
                    .show();
            //show message incorrect name
        } else if(!Validation.isValidPasswor(password)){
            flag=false;
            Toast.makeText(Registration.this,"Введіть коректний пароль",Toast.LENGTH_LONG)
                    .show();
            //show message incorrect password
        }else if(!Validation.isEqualsPassword(password,rePassword)){
            flag=false;
            Toast.makeText(Registration.this,"Паролі не сбігаються",Toast.LENGTH_LONG)
                    .show();
            //show message password dont equals
        }
        return flag;
    }


}
