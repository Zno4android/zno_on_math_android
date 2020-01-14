package com.example.jatcool.zno_on_math.activity.user;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;

import java.text.DecimalFormat;

public class ResultTest extends AppCompatActivity{

        TextView tvResultTest;
        ListView answers;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_test);
        Bundle values=getIntent().getExtras();
        int countCorrect=values.getInt("countCorrect");
        int countInCorrect=values.getInt("countInCorrect");

        tvResultTest=findViewById(R.id.Result_Test);
        answers=findViewById(R.id.Result_Test_Questions);

        tvResultTest.setText(new DecimalFormat("#0.00").format((countCorrect+countInCorrect)/(double)countCorrect));


    }
}
