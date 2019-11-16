package com.example.jatcool.zno_on_math;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Authorization extends AppCompatActivity {
EditText email,login,name,password,repassword;
Button add_users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        repassword = (EditText)findViewById(R.id.re_password);
        add_users = (Button)findViewById(R.id.add_users);
        add_users.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        NetworService.getInstance()
                                .getJSONApi()
                                .CreateUsers(new User(name.getText().toString(),email.getText().toString(),md5Custom(password.getText().toString())))
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {

                                   //тут нужно будет добавить вывод сообщения о удачной регистрации и очищать поля
                                        // желательно еще перекидывать на активити с авторизацией
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(),"Ошибка!",Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });
                    }
                }
        );

    }
    public boolean isExistEmail(String str){
        return false;
    }
    public boolean isValidEmail(String str){
        return false;
    }
    public boolean isEqualsPassword(String str1,String srt2){
        return  false;
    }
    public static String md5Custom(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }
}
