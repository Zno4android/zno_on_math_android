package com.example.jatcool.zno_on_math.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.jatcool.zno_on_math.R;

public class NotAllowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_allow);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Authorization.class));
        finish();
    }
}
