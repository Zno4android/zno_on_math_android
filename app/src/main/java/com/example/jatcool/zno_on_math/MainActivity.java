package com.example.jatcool.zno_on_math;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = (TextView)findViewById(R.id.TextView);
        t2 = (TextView)findViewById(R.id.textView);
        t3 = (TextView)findViewById(R.id.textView2);
        t4 = (TextView)findViewById(R.id.textView3);
         NetworService.getInstance()
                 .getJSONApi()
                 .getAllUsers()
                 .enqueue(new Callback<List<User>>() {
                     @Override
                     public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                         List<User> test = response.body();
                         User t = test.get(0);
                         t1.append(t.getId());
                         t2.append(t.getName());
                         t3.append(t.getEmail());
                         //t4.append(t.getHz());
                     }

                     @Override
                     public void onFailure(Call<List<User>> call, Throwable t) {
                    t1.append(t.getMessage());
                     }
                 });

    }
}
